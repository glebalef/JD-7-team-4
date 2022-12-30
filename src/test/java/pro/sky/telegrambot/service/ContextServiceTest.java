package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.Context;
import pro.sky.telegrambot.repository.ContextRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextServiceTest {
    @Mock
    ContextRepository repositoryMock;
    @Mock
    TelegramBotUpdatesListener listener;
    @Mock
    TelegramBot telegramBot;
    @InjectMocks
    ContextService contextService;
    final String updateStr = "{\"update_id\":1231231231,\n" + "\"message\":{\"message_id\":1231231231,\"from\":{\"id\":1231231231,\"first_name\":\"John\", \"last_name\":\"Smith\"},\"chat\":{\"id\":1231231231},\"date\":1579958705,\"text\":\"/start\"}}";

    @Test
    void shouldReturnPersonWhenGetContextByChatId() {
        Update update = BotUtils.parseUpdate(updateStr);

        List<Update> updates = new ArrayList<>();
        updates.add(update);
        listener.process(updates);

        Context newContext = new Context(update.message().chat().id(), null, null, false);
        when(repositoryMock.findByChatId(update.message().chat().id())).thenReturn(null);

        assertThat(contextService.getContextByChatId(update)).isEqualTo(newContext);

    }

    @Test
    void shouldReturnNullWhenGetContextByChatId() {
        Update update = BotUtils.parseUpdate(updateStr);

        List<Update> updates = new ArrayList<>();
        updates.add(update);
        listener.process(updates);

        Context newContext = new Context(update.message().chat().id(), null, null, false);
        when(repositoryMock.findByChatId(update.message().chat().id())).thenReturn(newContext);

        assertThat(contextService.getContextByChatId(update)).isEqualTo(null);

    }
}