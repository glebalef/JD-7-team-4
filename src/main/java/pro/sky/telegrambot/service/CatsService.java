package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.repository.CatsRepository;

import java.util.Optional;

@Service
public class CatsService {
    private final CatsRepository catsRepository;

    public CatsService(CatsRepository catsRepository) {
        this.catsRepository = catsRepository;
    }

    /**
     * Метод добавления кошки
     *
     * @param cat создается объект кошка
     * @return кошка
     */
    public Cat addCat(Cat cat) {
        return catsRepository.save(cat);
    }

    /**
     * Метод поиска кошки по ее идентификатору в БД.
     * <br>
     * Используется метод репозитория {@link org.springframework.data.jpa.repository.JpaRepository#findById(Object)}
     *
     * @param id идентификатор искомой кошки
     * @return найденная кошка
     */
    public Cat getCat(Long id) {
        return catsRepository.findById(id).orElse(null);
    }

    /**
     * Метод редактирования характеристик кошки.
     *
     * @param cat кошка, которая нуждается в редактировании
     * @return исправленные характеристики кошки
     */
    public Cat editCat(Long id, Cat cat) {
        Optional<Cat> optional = catsRepository.findById(id);
        if (optional.isPresent()) {
            Cat fromDB = optional.get();
            fromDB.setName(cat.getName());
            fromDB.setAge(cat.getAge());
            fromDB.setBreed(cat.getBreed());
            return catsRepository.save(fromDB);
        }
        return null;
    }

    /**
     * Метод удаления кошки из БД
     *
     * @param id идентификатор удаляемой кошки
     */
    public void deleteCat(Long id) {
        catsRepository.deleteById(id);
    }
}
