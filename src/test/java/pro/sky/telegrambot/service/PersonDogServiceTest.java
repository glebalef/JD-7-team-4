package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.exception.WrongPhoneNumberException;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.PersonDog;
import pro.sky.telegrambot.repository.PersonDogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonDogServiceTest {
    @Mock
    PersonDogRepository repositoryMock;

    @Mock
    TelegramBotUpdatesListener listener;
    @Mock
    Dog dog;
    @Mock
    TelegramBot telegramBot;
    @InjectMocks
    PersonDogService out;

    final String updateStr = "{\"update_id\":1231231231,\n" + "\"message\":{\"message_id\":1231231231,\"from\":{\"id\":1231231231,\"first_name\":\"John\", \"last_name\":\"Smith\"},\"chat\":{\"id\":1231231231},\"date\":1579958705,\"text\":\"89242255556\"}}";
    final String updateStrWrongNumber = "{\"update_id\":1231231231,\n" + "\"message\":{\"message_id\":1231231231,\"from\":{\"id\":1231231231,\"first_name\":\"John\", \"last_name\":\"Smith\"},\"chat\":{\"id\":1231231231},\"date\":1579958705,\"text\":\"111G\"}}";
    final PersonDog personDog = new PersonDog(1L, "John", "Smith", null);
    Dog testDog = new Dog(1L, "Шарик", 2, "лайка");

    @Test
    void shouldReturnPersonWhenGetPersonById() {


        Update update = BotUtils.parseUpdate(updateStr);

        List<Update> updates = new ArrayList<>();
        updates.add(update);
        listener.process(updates);

        PersonDog newPerson = new PersonDog(update.message().chat().id(), update.message().chat().firstName(), update.message().chat().lastName(), null);
        when(repositoryMock.findByChatId(update.message().chat().id())).thenReturn(null);

        assertThat(out.getPersonByChatId(update)).isEqualTo(newPerson);

    }

    @Test
    void shouldReturnNullWhenGetPersonById() {


        Update update = BotUtils.parseUpdate(updateStr);

        List<Update> updates = new ArrayList<>();
        updates.add(update);
        listener.process(updates);

        PersonDog newPerson = new PersonDog(update.message().chat().id(), update.message().chat().firstName(), update.message().chat().lastName(), null);
        when(repositoryMock.findByChatId(update.message().chat().id())).thenReturn(newPerson);

        assertThat(out.getPersonByChatId(update)).isEqualTo(null);

    }

    @Test
    void phoneNumberAdd() throws WrongPhoneNumberException {
        Update update = BotUtils.parseUpdate(updateStr);

        List<Update> updates = new ArrayList<>();
        updates.add(update);
        listener.process(updates);

        PersonDog newPerson = new PersonDog(update.message().chat().id(), update.message().chat().firstName(), update.message().chat().lastName(), "89638273147");
        when(repositoryMock.findByChatId(update.message().chat().id())).thenReturn(newPerson);

        assertThat(out.phoneNumberAdd(update)).isEqualTo(newPerson.getPhone());
    }

    @Test
    void shouldThrowWrongNumberExceptionWhenCallPhoneNumberAdd() {
        Update update = BotUtils.parseUpdate(updateStrWrongNumber);

        assertThrows(WrongPhoneNumberException.class, () -> out.phoneNumberAdd(update));
    }

    @Test
    void addPersonDog() {
        when(out.addPersonDog(personDog)).thenReturn(personDog);
        assertThat(out.addPersonDog(personDog)).isEqualTo(personDog);
    }

    @Test
    void getPersonDog() {
        personDog.setId(10L);
        Optional<PersonDog> person = Optional.of(personDog);
        when(repositoryMock.findById(10L)).thenReturn(person);
        assertThat(out.getPersonDog(10L)).isEqualTo(personDog);
    }

    @Test
    void editPersonDogAndAssignDog() {
        PersonDog editedPerson = new PersonDog(2L, "Peter", "Smith", null);
        Optional<PersonDog> person = Optional.of(editedPerson);
        editedPerson.setId(10L);
        when(repositoryMock.findById(10L)).thenReturn(person);
        when(out.EditPersonDogAndAssignDog(10L, editedPerson)).thenReturn(editedPerson);
        out.EditPersonDogAndAssignDog(10L, editedPerson).setDog(testDog);
        assertThat(out.EditPersonDogAndAssignDog(10L, editedPerson).getFirstName()).isEqualTo("Peter");
        assertThat(out.EditPersonDogAndAssignDog(10L, editedPerson).getDog().getName()).isEqualTo("Шарик");
    }

    @Test
    void deletePersonDog() {
        personDog.setId(10L);
        out.deleteDog(10L);
        verify(repositoryMock, times(1)).deleteById(10L);
    }
}