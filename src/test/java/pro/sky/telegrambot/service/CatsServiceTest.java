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
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.repository.CatsRepository;
import pro.sky.telegrambot.repository.DogsRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CatsServiceTest {

    @Mock
    CatsRepository catsRepository;

    @InjectMocks
    CatsService out;
    Cat testCat = new Cat(1L, "Мурзик",2, "британец");
    Cat editedCat = new Cat(1L, "Мяучелло", 4, "мейн-кун");
    Long id = 1L;
    String name = "Мурзик";
    int age = 2;
    String breed = "британец";

    @Test
    void shouldReturnTestDog() {

        Mockito.when(catsRepository.findById(1L)).thenReturn(Optional.ofNullable(testCat));
        Mockito.when(catsRepository.save(testCat)).thenReturn(testCat);
        Cat newCat = new Cat(id,name,age,breed);

        Cat resultCat = out.getCat(1L);
        Cat resultCat2 = out.getCat(2L);
        Cat resultCat3 = out.addCat(testCat);

        Assertions.assertEquals(testCat,resultCat);
        Assertions.assertNull(resultCat2);
        Assertions.assertEquals(testCat.getName(),newCat.getName());
        Assertions.assertEquals(testCat.getAge(),newCat.getAge());
        Assertions.assertEquals(testCat.getBreed(),newCat.getBreed());
    }

    @Test
    void shouldChangeDogData() {
        Optional<Cat> optionalResult = Optional.ofNullable(editedCat);
        Optional<Cat> optionalInitial = Optional.ofNullable(testCat);
        Mockito.when(catsRepository.findById(1L)).thenReturn(optionalInitial);
        Mockito.when(catsRepository.save(optionalInitial.get())).thenReturn(optionalResult.get());

        Cat result = out.editCat(1L, editedCat);

        Assertions.assertEquals(result.getName(), editedCat.getName());
        Assertions.assertEquals(result.getBreed(), editedCat.getBreed());
        Assertions.assertEquals(result.getAge(), editedCat.getAge());
        Assertions.assertEquals(result.getAge(), 4);

    }
}
