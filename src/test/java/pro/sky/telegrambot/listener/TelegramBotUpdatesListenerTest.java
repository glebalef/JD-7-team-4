package pro.sky.telegrambot.listener;


import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.reply.Keyboards;
import pro.sky.telegrambot.reply.ReplyMessages;
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

    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("start_update.json").toURI()));
        Update update = getUpdate(json, "/start");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ReplyMessages replyMessages = new ReplyMessages();
        Keyboards keyboards = new Keyboards();


        // тут мы считаем, что должен при команде /start выхывается следующая конструкция:
        ArgumentCaptor<ReplyMessages> argumentCaptor1 = ArgumentCaptor.forClass(ReplyMessages.class);
        Mockito.verify(telegramBot).execute(argumentCaptor1.capture()
                .initialMessage(update).replyMarkup(keyboards.getInitialKeyboard()));
        SendMessage actual = argumentCaptor1.getValue().initialMessage(update);


        // что должно получится:
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Вас приветствует бот Кожуховского приюта. Пожалуйста, выберете интересующий Вас приют");
        Assertions.assertThat(actual.getParameters().get("parse_mode")).isEqualTo(ParseMode.Markdown.name());
        // но в итоге получается:
        // java.lang.NullPointerException: Cannot invoke "pro.sky.telegrambot.reply.ReplyMessages.initialMessage(com.pengrad.telegrambot.model.Update)" because the return value of "org.mockito.ArgumentCaptor.capture()" is null
    }

    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }

}
