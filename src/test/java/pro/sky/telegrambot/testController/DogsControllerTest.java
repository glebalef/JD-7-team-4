package pro.sky.telegrambot.testController;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.controller.DogsController;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.repository.DogsRepository;
import pro.sky.telegrambot.service.DogsService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DogsController.class)
public class DogsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogsRepository dogsRepository;

    @SpyBean
    private DogsService dogsService;

    @InjectMocks
    private DogsController dogsController;

    @Test
    public void testDogs() throws Exception {
        final Long id = 1L;
        final String name = "David";
        final int age = 5;
        final String breed = "labrador";
        Dog dog = new Dog(id, name, age, breed);

        JSONObject dogsObject = new JSONObject();
        dogsObject.put("id", id);
        dogsObject.put("name", name);
        dogsObject.put("age", age);
        dogsObject.put("breed", breed);

        Mockito.when(dogsRepository.save(any(Dog.class))).thenReturn(dog);
        Mockito.when(dogsRepository.findById(any(Long.class))).thenReturn(Optional.of(dog));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/dog")
                        .content(dogsObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.breed").value(breed));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/dog/" + id)
                        .content(dogsObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.breed").value(breed));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.breed").value(breed));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/dog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
