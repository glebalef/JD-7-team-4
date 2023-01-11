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
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.constant.Shelter;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.reply.ReplyMessages;
import pro.sky.telegrambot.repository.*;
import pro.sky.telegrambot.service.ContextService;
import pro.sky.telegrambot.service.PersonCatService;
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
    private PersonCatService personCatService;
    @Mock
    private PersonDogRepository personDogRepository;
    @Mock
    private PersonCatRepository personCatRepository;
    @Mock
    private PersonDog personDogMock;
    @Mock
    private PersonCat personCatMock;
    @Mock
    private DogReportRepository dogReportRepositoryMock;
    @Mock
    private CatReportRepository catReportRepositoryMock;

    Shelter shelter = new Shelter();

    PersonDog PERSONDOGWITHNODOG = new PersonDog(
            123L,
            "Иван",
            "Иванов",
            "222-22-22");

    PersonCat PERSONCATWITHNOCAT = new PersonCat(
            123L,
            "Иван",
            "Иванов",
            "222-22-22");
    Dog TESTDOG = new Dog(1L, "Шарик", 3, "корги");
    Cat TESTCAT = new Cat(1L, "Мурка", 3, "вислоухий");

    DogReport DOGREPORTTEST = new DogReport(null, null, null, null, null, null, null);
    CatReport CATREPORTTEST = new CatReport(null, null, null, null, null, null, null);

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

    @Test
    public void handle_CAT_SHELTER_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Приют для кошек");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(personCatService.getPersonByChatId(update)).thenReturn(personCatMock);
        Mockito.when(personCatRepository.findByChatId(123L)).thenReturn(personCatMock);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getInfoCat());
    }

    @Test
    public void handle_DOG_SCHEDULE_Test() throws URISyntaxException, IOException {
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
    public void handle_CAT_SCHEDULE_Test() throws URISyntaxException, IOException {
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
    public void handle_CAT_ADRESS_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Показать адрес приюта");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");
        telegramBotUpdatesListener.process(Collections.singletonList(update));



        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getAddressCat());
    }

    @Test
    public void handle_DOG_ADRESS_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Показать адрес приюта");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        telegramBotUpdatesListener.process(Collections.singletonList(update));



        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getAddress());
    }

    @Test
    public void handle_PHONE_REQUEST_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Поделитесь контактными данными");

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Введите номер телефона для связи");
    }

    @Test
    public void handle_CAR_PASS_DOG_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Оформить пропуск на машину");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getSecurityPass());
    }

    @Test
    public void handle_CAR_PASS_CAT_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Оформить пропуск на машину");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getSecurityPassCat());
    }

    @Test
    public void handle_CAR_SAFETY_DOG_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Техника безопасности");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getSafety());
    }

    @Test
    public void handle_CAR_SAFETY_CAT_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Техника безопасности");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getSafetyCat());
    }

    @Test
    public void handle_DOCS_TO_ADOPT_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Список необходимых документов");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getDocsToAdopt());
    }
    @Test
    public void handle_TRANSPORTATION_CAT_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Рекомендации по транспортировке");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getTransportationCat());
    }

    @Test
    public void handle_TRANSPORTATION_DOG_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Рекомендации по транспортировке");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getTransportation());
    }

    @Test
    public void handle_RECOMENDATIONS_DOG_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Рекомендации");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Пожалуйста, выберете интересующий Вас раздел");
    }

    @Test
    public void handle_REJECTS_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Возможные причины отказа");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getRejectCauses());
    }

    @Test
    public void handle_RECOMMENDATIONS_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "Рекомендации по обустройству дома");

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Пожалуйста, выберете интересующий Вас раздел");
    }

    @Test
    public void handle_DOG_HANDLER_ADVICE_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.DOG_HANDLER_ADVICE.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getDogHandlerAdvice());
    }

    @Test
    public void handle_BEST_DOG_HANDLERS_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.BEST_DOG_HANDLERS.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getBestDogHandlers());
    }

    @Test
    public void handle_HOME_ADULT_DOG_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.HOME_ADULT.getMessage());

        Mockito.when(context.getType()).thenReturn("dog");
        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getHomeAdult());
    }

    @Test
    public void handle_HOME_ADULT_CAT_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.HOME_ADULT.getMessage());

        Mockito.when(context.getType()).thenReturn("cat");
        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getHomeAdultCat());
    }

    @Test
    public void handle_KITTEN_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.KITTEN.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getKitten());
    }

    @Test
    public void handle_PUPPY_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.PUPPY.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getPuppy());
    }

    @Test
    public void handle_BLIND_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.BLIND.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getBlind());
    }

    @Test
    public void handle_DISABLED_Test() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.DISABLED.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(shelter.getDisabled());
    }

    @Test
    public void shouldReturnNoDogResponseDog() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.REPORT_REQUEST.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        Mockito.when(personDogRepository.findByChatId(123L)).thenReturn(PERSONDOGWITHNODOG);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Чтобы отправить отчет, у Вас должен быть питомец!");
    }

    @Test
    public void shouldReturnNoDogResponseCat() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.REPORT_REQUEST.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");
        Mockito.when(personCatRepository.findByChatId(123L)).thenReturn(PERSONCATWITHNOCAT);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Чтобы отправить отчет, у Вас должен быть питомец!");
    }

    @Test
    public void shouldSetNegativeOldHabitsDog() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.OLD_HABITS_NEGATIVE.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        Mockito.when(personDogRepository.findByChatId(123L)).thenReturn(PERSONDOGWITHNODOG);
        Mockito.when(dogReportRepositoryMock.findDogReportByFileIdAndPersonDogId(null, null)).thenReturn(DOGREPORTTEST);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(DOGREPORTTEST.getOldHabitsRefuse()).isFalse();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("А теперь расскажите, ведет ли себя питомец как-то по-новому?");
    }

    @Test
    public void shouldSetPositiveOldHabitsDog() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.OLD_HABITS_POSITIVE.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        Mockito.when(personDogRepository.findByChatId(123L)).thenReturn(PERSONDOGWITHNODOG);
        Mockito.when(dogReportRepositoryMock.findDogReportByFileIdAndPersonDogId(null, null)).thenReturn(DOGREPORTTEST);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(DOGREPORTTEST.getOldHabitsRefuse()).isTrue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("А теперь расскажите, ведет ли себя питомец как-то по-новому?");
    }

    @Test
    public void shouldSetNegativeOldHabitsCat() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.OLD_HABITS_NEGATIVE.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");
        Mockito.when(personCatRepository.findByChatId(123L)).thenReturn(PERSONCATWITHNOCAT);
        Mockito.when(catReportRepositoryMock.findCatReportByFileIdAndPersonCatId(null, null)).thenReturn(CATREPORTTEST);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(CATREPORTTEST.getOldHabitsRefuse()).isFalse();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("А теперь расскажите, ведет ли себя питомец как-то по-новому?");
    }

    @Test
    public void shouldSetPositiveOldHabitsCat() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.OLD_HABITS_POSITIVE.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");
        Mockito.when(personCatRepository.findByChatId(123L)).thenReturn(PERSONCATWITHNOCAT);
        Mockito.when(catReportRepositoryMock.findCatReportByFileIdAndPersonCatId(null, null)).thenReturn(CATREPORTTEST);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(CATREPORTTEST.getOldHabitsRefuse()).isTrue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("А теперь расскажите, ведет ли себя питомец как-то по-новому?");
    }

    @Test
    public void shouldSetNegativeNewHabitsDog() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.NEW_HABITS_NEGATIVE.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        Mockito.when(personDogRepository.findByChatId(123L)).thenReturn(PERSONDOGWITHNODOG);
        Mockito.when(dogReportRepositoryMock.findDogReportByFileIdAndPersonDogId(null, null)).thenReturn(DOGREPORTTEST);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(DOGREPORTTEST.getNewHabitsAppear()).isFalse();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Спасибо! Теперь направьте, пожалуйста, фотографию Вашего питомца, чтобы мы убедились, что с ним все хорошо!");
    }

    @Test
    public void shouldSetPositiveNewHabitsDog() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.NEW_HABITS_POSITIVE.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("dog");
        Mockito.when(personDogRepository.findByChatId(123L)).thenReturn(PERSONDOGWITHNODOG);
        Mockito.when(dogReportRepositoryMock.findDogReportByFileIdAndPersonDogId(null, null)).thenReturn(DOGREPORTTEST);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(DOGREPORTTEST.getNewHabitsAppear()).isTrue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Спасибо! Теперь направьте, пожалуйста, фотографию Вашего питомца, чтобы мы убедились, что с ним все хорошо!");
    }

    @Test
    public void shouldSetPositiveNewHabitsCat() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.NEW_HABITS_POSITIVE.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");
        Mockito.when(personCatRepository.findByChatId(123L)).thenReturn(PERSONCATWITHNOCAT);
        Mockito.when(catReportRepositoryMock.findCatReportByFileIdAndPersonCatId(null, null)).thenReturn(CATREPORTTEST);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(CATREPORTTEST.getNewHabitsAppear()).isTrue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Спасибо! Теперь направьте, пожалуйста, фотографию Вашего питомца, чтобы мы убедились, что с ним все хорошо!");
    }

    @Test
    public void shouldSetNegativeNewHabitsCat() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, Commands.NEW_HABITS_NEGATIVE.getMessage());

        Mockito.when(contextRepository.findByChatId(123L)).thenReturn(context);
        Mockito.when(context.getType()).thenReturn("cat");
        Mockito.when(personCatRepository.findByChatId(123L)).thenReturn(PERSONCATWITHNOCAT);
        Mockito.when(catReportRepositoryMock.findCatReportByFileIdAndPersonCatId(null, null)).thenReturn(CATREPORTTEST);

        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(CATREPORTTEST.getNewHabitsAppear()).isFalse();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Спасибо! Теперь направьте, пожалуйста, фотографию Вашего питомца, чтобы мы убедились, что с ним все хорошо!");
    }



    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }
}
