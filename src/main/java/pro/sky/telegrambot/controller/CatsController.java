package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.service.CatsService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/cat")
public class CatsController {

    private final CatsService catsService;

    public CatsController(CatsService catsService) {
        this.catsService = catsService;
    }

    @Operation(
            summary = "Внесение в БД новой кошки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Созданная кошка",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @PostMapping
    public Cat addCat(@RequestBody Cat cat) {
        return catsService.addCat(cat);
    }

    @Operation(
            summary = "Поиск необходимой кошки по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденная кошка",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @GetMapping("{id}")
    public ResponseEntity<Cat> getCat(@PathVariable Long id) {
        Cat cat = catsService.getCat(id);
        if (cat == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cat);
    }

    @Operation(
            summary = "Редактирование необходимых характеристик у кошки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденная кошка для изменения характеристик",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @PutMapping("{id}")
    public ResponseEntity<Cat> editCat(@RequestBody Cat cat) {
        Cat editorialCat = catsService.editCat(cat);
        if (editorialCat == null) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(editorialCat);
    }

    @Operation(
            summary = "Удаление необходимой кошки по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Кошка удалена",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long id) {
        catsService.deleteCat(id);
        return ResponseEntity.ok().build();
    }
}
