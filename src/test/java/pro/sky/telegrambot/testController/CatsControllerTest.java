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
import pro.sky.telegrambot.controller.CatsController;
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.repository.CatsRepository;
import pro.sky.telegrambot.service.CatsService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CatsController.class)
public class CatsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatsRepository catsRepository;

    @SpyBean
    private CatsService catsService;

    @InjectMocks
    private CatsController catsController;

    @Test
    public void testCats() throws Exception {
        final Long id = 1L;
        final String name = "Charly";
        final int age = 2;
        final String breed = "Persian";
        Cat cat = new Cat(id, name, age, breed);

        JSONObject catsObject = new JSONObject();
        catsObject.put("id", id);
        catsObject.put("name", name);
        catsObject.put("age", age);
        catsObject.put("breed", breed);

        Mockito.when(catsRepository.save(any(Cat.class))).thenReturn(cat);
        Mockito.when(catsRepository.findById(any(Long.class))).thenReturn(Optional.of(cat));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cat")
                        .content(catsObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.breed").value(breed));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/cat/" + id)
                        .content(catsObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.breed").value(breed));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.breed").value(breed));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
