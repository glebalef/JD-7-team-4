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
import pro.sky.telegrambot.controller.PersonCatController;
import pro.sky.telegrambot.model.PersonCat;
import pro.sky.telegrambot.repository.CatsRepository;
import pro.sky.telegrambot.repository.PersonCatRepository;
import pro.sky.telegrambot.service.PersonCatService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonCatController.class)
public class PersonCatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonCatRepository personCatRepository;

    @SpyBean
    private PersonCatService personCatService;

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private CatsRepository catsRepository;

    @InjectMocks
    private PersonCatController personCatController;

    @Test
    public void testPersonCat() throws Exception {
        final Long id = 1L;
        final Long chatId = 2L;
        final String firstName = "Evgeniy";
        final String lastName = "Onegin";
        final String phone = "88005553535";
        PersonCat personCat = new PersonCat(chatId, firstName, lastName, phone);
        personCat.setId(id);

        JSONObject personCatObject = new JSONObject();
        personCatObject.put("id", id);
        personCatObject.put("chatId", chatId);
        personCatObject.put("firstName", firstName);
        personCatObject.put("lastName", lastName);
        personCatObject.put("phone", phone);

        Mockito.when(personCatRepository.save(any(PersonCat.class))).thenReturn(personCat);
        Mockito.when(personCatRepository.findById(any(Long.class))).thenReturn(Optional.of(personCat));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/personCat")
                        .content(personCatObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.phone").value(phone));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/personCat/" + id)
                        .content(personCatObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.phone").value(phone));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/personCat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.chatId").value(chatId))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.phone").value(phone));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/personCat/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
