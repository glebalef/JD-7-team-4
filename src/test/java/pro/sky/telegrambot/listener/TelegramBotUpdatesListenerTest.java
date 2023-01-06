package pro.sky.telegrambot.listener;


import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import liquibase.pro.packaged.M;
import liquibase.pro.packaged.N;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.constant.Shelter;
import pro.sky.telegrambot.model.Context;
import pro.sky.telegrambot.model.PersonDog;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.reply.ReplyMessages;
import pro.sky.telegrambot.repository.ContextRepository;
import pro.sky.telegrambot.repository.PersonDogRepository;
import pro.sky.telegrambot.service.ContextService;
import pro.sky.telegrambot.service.PersonDogService;
import pro.sky.telegrambot.service.SchedulerService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    @Mock
    private TelegramBot telegramBot;
    @Mock
    SchedulerService schedulerService;
    @Mock
    private ContextRepository contextRepository;
    @Mock
    private ContextService contextService;
    @Mock
    private Context context;
    @Mock
    private PersonDogService personDogService;
    @Mock
    private PersonDogRepository personDogRepository;
    @Mock
    private PersonDog personDogMock;

    Shelter shelter = new Shelter();

    @Test
    public void handleStartTest() throws URISyntaxException, IOException {

        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "/start");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor1 = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor1.capture());
        SendMessage actual = argumentCaptor1.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Пожалуйста, выберете интересующий Вас приют");
    }

    @Test
    public void handle_BACK_TO_INITIAL_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "назад к меню приюта");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Пожалуйста, выберете интересующий Вас раздел");
    }

    @Test
    public void handle_MENU_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Узнать о приюте");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Какую информацию Вы хотели бы узнать?");
    }

    @Test
    public void handle_SCHEDULE_DOG_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "График работы приюта");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getSchedule());
    }

    @Test
    public void handle_SCHEDULE_CAT_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "График работы приюта");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getScheduleCat());
    }

    @Test
    public void handle_DOG_SHELTER_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Приют для собак");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(personDogService.getPersonByChatId(update)).thenReturn(personDogMock);
        Mockito.when(personDogRepository.findByChatId(123L)).thenReturn(personDogMock);
        telegramBotUpdatesListener.process(Collections.singletonList(update));



        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getInfo());
    }

    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }
}
