package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Context;
import pro.sky.telegrambot.repository.ContextRepository;
import pro.sky.telegrambot.repository.PersonCatRepository;
import pro.sky.telegrambot.repository.PersonDogRepository;
/**
 * сервис-класс для работы с сущностью Context
 *
 * @author Евгений Фисенко
 */
@Service
public class ContextService {
    private final ContextRepository contextRepository;
    private final PersonDogRepository personDogRepository;
    private final PersonCatRepository personCatRepository;

    public ContextService(ContextRepository contextRepository, PersonDogRepository personDogRepository, PersonCatRepository personCatRepository) {
        this.contextRepository = contextRepository;

        this.personDogRepository = personDogRepository;
        this.personCatRepository = personCatRepository;
    }
    /**
     * получает пользователя и, если это новый пользователь, заносит пользователя в базу
     *
     * @param update - данные о сообщении из класса TelegramBotUpdateListener
     */

    public Context getContextByChatId(Update update) {
        Long chatId = update.message().chat().id();
        if (contextRepository.findByChatId(chatId) == null) {
            Context context = new Context();
            context.setChatId(chatId);
            context.setTestOff(false);
            contextRepository.save(context);
            return context;
        }
        return null;
    }
}
