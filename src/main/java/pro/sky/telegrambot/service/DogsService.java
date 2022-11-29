package pro.sky.telegrambot.service;

import pro.sky.telegrambot.Models.Dog;
import pro.sky.telegrambot.Repository.DogsRepository;

import java.util.Optional;

public class DogsService {

private final DogsRepository dogsRepository;

    public DogsService(DogsRepository dogsRepository) {
        this.dogsRepository = dogsRepository;
    }

    public Dog addDog(Dog dog) {
        return dogsRepository.save(dog);
    }

    public Dog getDog(Long id) {
        return dogsRepository.findById(id).orElse(null);
    }

    public Dog editDog(Dog dog) {
        Optional<Dog> optional = dogsRepository.findById(dog.getId());
        if (optional.isPresent()) {
            Dog fromDB = optional.get();
            fromDB.setName(fromDB.getName());
            fromDB.setAge(fromDB.getAge());
            return dogsRepository.save(fromDB);
        }
        return null;
    }

    public void deleteDog(Long id) {
        dogsRepository.deleteById(id);
    }
}
