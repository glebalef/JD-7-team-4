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
import pro.sky.telegrambot.model.PersonCat;
import pro.sky.telegrambot.repository.PersonCatRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonCatServiceTest {
    @Mock
    PersonCatRepository repositoryMock;

    @Mock
    TelegramBotUpdatesListener listener;
    @Mock
    TelegramBot telegramBot;
    @InjectMocks
    PersonCatService out;

    final String updateStr = "{\"update_id\":1231231231,\n" + "\"message\":{\"message_id\":1231231231,\"from\":{\"id\":1231231231,\"first_name\":\"John\", \"last_name\":\"Smith\"},\"chat\":{\"id\":1231231231},\"date\":1579958705,\"text\":\"89242255556\"}}";
    final String updateStrWrongNumber = "{\"update_id\":1231231231,\n" + "\"message\":{\"message_id\":1231231231,\"from\":{\"id\":1231231231,\"first_name\":\"John\", \"last_name\":\"Smith\"},\"chat\":{\"id\":1231231231},\"date\":1579958705,\"text\":\"111G\"}}";

    @Test
    void shouldReturnPersonWhenGetPersonById() {


        Update update = BotUtils.parseUpdate(updateStr);

        List<Update> updates = new ArrayList<>();
        updates.add(update);
        listener.process(updates);

        PersonCat newPerson = new PersonCat(update.message().chat().id(), update.message().chat().firstName(), update.message().chat().lastName(), null);
        when(repositoryMock.findByChatId(update.message().chat().id())).thenReturn(null);

        assertThat(out.getPersonByChatId(update)).isEqualTo(newPerson);

    }

    @Test
    void shouldReturnNullWhenGetPersonById() {


        Update update = BotUtils.parseUpdate(updateStr);

        List<Update> updates = new ArrayList<>();
        updates.add(update);
        listener.process(updates);

        PersonCat newPerson = new PersonCat(update.message().chat().id(), update.message().chat().firstName(), update.message().chat().lastName(), null);
        when(repositoryMock.findByChatId(update.message().chat().id())).thenReturn(newPerson);

        assertThat(out.getPersonByChatId(update)).isEqualTo(null);

    }

    @Test
    void phoneNumberAdd() throws WrongPhoneNumberException {
        Update update = BotUtils.parseUpdate(updateStr);

        List<Update> updates = new ArrayList<>();
        updates.add(update);
        listener.process(updates);

        PersonCat newPerson = new PersonCat(update.message().chat().id(), update.message().chat().firstName(), update.message().chat().lastName(), "89638273147");
        when(repositoryMock.findByChatId(update.message().chat().id())).thenReturn(newPerson);

        assertThat(out.phoneNumberAdd(update)).isEqualTo(newPerson.getPhone());
    }

    @Test
    void shouldThrowWrongNumberExceptionWhenCallPhoneNumberAdd() {
        Update update = BotUtils.parseUpdate(updateStrWrongNumber);

        assertThrows(WrongPhoneNumberException.class, () -> out.phoneNumberAdd(update));
    }

}