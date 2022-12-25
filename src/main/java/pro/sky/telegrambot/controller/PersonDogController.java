package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.PersonDog;
import pro.sky.telegrambot.service.PersonDogService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/personDog")
public class PersonDogController {
    private final PersonDogService personDogService;

    public PersonDogController(PersonDogService personDogService) {
        this.personDogService = personDogService;
    }

    @Operation(
            summary = "Внесение в БД нового усыновителя собаки, если пришел не из телеграм бота",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Созданный усыновитель собаки",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @PostMapping
    public PersonDog addPersonDog(@RequestBody PersonDog personDog) {
        return personDogService.addPersonDog(personDog);
    }

    @Operation(
            summary = "Поиск необходимого усыновителя собаки по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденный усыновитель собаки",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @GetMapping("{id}")
    public ResponseEntity<PersonDog> getPersonDog(@PathVariable Long id) {
        PersonDog personDog = personDogService.getPersonDog(id);
        if (personDog == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personDog);
    }

    @Operation(
            summary = "Присвоение собаки усыновителю и редактирование необходимых данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Метод присвоения собаки усыновителю и при необходимости, редактирование данных",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @PutMapping("{id}")
    public ResponseEntity<PersonDog> EditPersonDogAndAssignDog (@RequestBody PersonDog personDog) {
        PersonDog parent = personDogService.EditPersonDogAndAssignDog(personDog);
        if (parent == null) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удаление необходимого усыновителя собаки по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель собаки удален",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePersonDog(@PathVariable Long id) {
        personDogService.deleteDog(id);
        return ResponseEntity.ok().build();
    }
}
