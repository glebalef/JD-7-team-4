package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.PersonCat;
import pro.sky.telegrambot.service.PersonCatService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/personCat")
public class PersonCatController {

    private final PersonCatService personCatService;

    public PersonCatController(PersonCatService personCatService) {
        this.personCatService = personCatService;
    }

    @Operation(
            summary = "Внесение в БД нового усыновителя кошки, если пришел не из телеграм бота",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Созданный усыновитель кошки",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @PostMapping
    public PersonCat addPersonCat(@RequestBody PersonCat personCat) {
        return personCatService.addPersonCat(personCat);
    }

    @Operation(
            summary = "Поиск необходимого усыновителя кошки по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденный усыновитель кошки",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @GetMapping("{id}")
    public ResponseEntity<PersonCat> getPersonCat(@PathVariable Long id) {
        PersonCat personCat = personCatService.getPersonCat(id);
        if (personCat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personCat);
    }

    @Operation(
            summary = "Присвоение кошки усыновителю и редактирование необходимых данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Присвоенная кошка усыновителю, отредактированные данные",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @PutMapping("{id}")
    public ResponseEntity<PersonCat> EditPersonCatAndAssignCat(@PathVariable Long id, @RequestBody PersonCat personCat) {
        PersonCat parent = personCatService.EditPersonCatAndAssignCat(id, personCat);
        if (parent == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(parent);
    }

    @Operation(
            summary = "Удаление необходимого усыновителя кошки по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель кошки удален",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePersonCat(@PathVariable Long id) {
        personCatService.deletePersonCat(id);
        return ResponseEntity.ok().build();
    }
}
