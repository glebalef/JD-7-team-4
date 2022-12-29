package pro.sky.telegrambot.testController;

import com.pengrad.telegrambot.TelegramBot;
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
import pro.sky.telegrambot.controller.PersonDogController;
import pro.sky.telegrambot.model.PersonDog;
import pro.sky.telegrambot.repository.DogsRepository;
import pro.sky.telegrambot.repository.PersonDogRepository;
import pro.sky.telegrambot.service.PersonDogService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonDogController.class)
public class PersonDogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDogRepository personDogRepository;

    @SpyBean
    private PersonDogService personDogService;

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private DogsRepository dogsRepository;

    @InjectMocks
    private PersonDogController personDogController;

    @Test
    public void testPersonDog() throws Exception {
        final Long id = 1L;
        final Long chatId = 2L;
        final String firstName = "Evgeniy";
        final String lastName = "Onegin";
        final String phone = "88005553535";
        PersonDog personDog = new PersonDog(chatId, firstName, lastName, phone);
        personDog.setId(id);

        JSONObject personDogObject = new JSONObject();
        personDogObject.put("id", id);
        personDogObject.put("chatId", chatId);
        personDogObject.put("firstName", firstName);
        personDogObject.put("lastName", lastName);
        personDogObject.put("phone", phone);

        Mockito.when(personDogRepository.save(any(PersonDog.class))).thenReturn(personDog);
        Mockito.when(personDogRepository.findById(any(Long.class))).thenReturn(Optional.of(personDog));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/personDog")
                        .content(personDogObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.phone").value(phone));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/personDog/" + id)
                        .content(personDogObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.phone").value(phone));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/personDog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.phone").value(phone));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/personDog/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
