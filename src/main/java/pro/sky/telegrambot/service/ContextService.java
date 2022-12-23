package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Context;
import pro.sky.telegrambot.repository.ContextRepository;
import pro.sky.telegrambot.repository.PersonCatRepository;
import pro.sky.telegrambot.repository.PersonDogRepository;

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

    public Context getContextByChatId(Update update) {
        Long chatId = update.message().chat().id();
        if (contextRepository.findByChatId(chatId) == null) {
            Context context = new Context();
            context.setChatId(chatId);
            context.setTestOff(false);
            context.setPersonDog(personDogRepository.findByChatId(chatId));
            context.setPersonCat(personCatRepository.findByChatId(chatId));
            contextRepository.save(context);
            return context;
        }
        return null;
    }
}
