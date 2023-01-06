package pro.sky.telegrambot.service;

import liquibase.pro.packaged.A;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.repository.DogsRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DogsServiceTest {

    @Mock
    DogsRepository dogsRepository;

    @InjectMocks
    DogsService out;
    Dog testDog = new Dog(1L, "Бобик",2, "дворняга");
    Dog editedDog = new Dog(1L, "Шарик", 7, "овчарка");
    Long id = 1L;
    String name = "Бобик";
    int age = 2;
    String breed = "дворняга";

    @Test
    void shouldReturnTestDog() {

        Mockito.when(dogsRepository.findById(1L)).thenReturn(Optional.ofNullable(testDog));
        Mockito.when(dogsRepository.save(testDog)).thenReturn(testDog);
        Dog newDog = new Dog(id,name,age,breed);

        Dog resultDog = out.getDog(1L);
        Dog resultDog2 = out.getDog(2L);
        Dog resultDog3 = out.addDog(testDog);

        Assertions.assertEquals(testDog,resultDog);
        Assertions.assertNull(resultDog2);
        Assertions.assertEquals(testDog.getName(),newDog.getName());
        Assertions.assertEquals(testDog.getAge(),newDog.getAge());
        Assertions.assertEquals(testDog.getBreed(),newDog.getBreed());
    }

    @Test
    void shouldChangeDogData() {
        Optional<Dog> optionalResult = Optional.ofNullable(editedDog);
        Optional<Dog> optionalInitial = Optional.ofNullable(testDog);
        Mockito.when(dogsRepository.findById(1L)).thenReturn(optionalInitial);
        Mockito.when(dogsRepository.save(optionalInitial.get())).thenReturn(optionalResult.get());

        Dog result = out.editDog(1L, editedDog);

        Assertions.assertEquals(result.getName(), editedDog.getName());
        Assertions.assertEquals(result.getBreed(), editedDog.getBreed());
        Assertions.assertEquals(result.getAge(), editedDog.getAge());
        Assertions.assertEquals(result.getAge(), 7);

    }
}
