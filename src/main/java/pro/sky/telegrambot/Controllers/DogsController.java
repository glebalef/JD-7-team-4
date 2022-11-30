package pro.sky.telegrambot.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.Models.Dog;
import pro.sky.telegrambot.service.DogsService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/dog")
public class DogsController {

    private final DogsService dogsService;

    public DogsController(DogsService dogsService) {
        this.dogsService = dogsService;
    }

    @Operation(
            summary = "Внесение в БД новой собаки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Созданная собака",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @PostMapping
    public Dog addDog(@RequestBody Dog dog) {
        return dogsService.addDog(dog);
    }

    @Operation(
            summary = "Поиск необходимой собаки по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденная собака",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @GetMapping("{id}")
    public ResponseEntity<Dog> getDod(@PathVariable Long id) {
        Dog dog = dogsService.getDog(id);
        if (dog == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dog);
    }

    @Operation(
            summary = "Редактирование необходимых характеристик у собаки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденная собака для изменения характеристик",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @PutMapping("{id}")
    public ResponseEntity<Dog> editDog(@RequestBody Dog dog) {
        Dog editorialDog = dogsService.editDog(dog);
        if (editorialDog == null) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(editorialDog);
    }

    @Operation(
            summary = "Удаление необходимой собаки по идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Собака удалена",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id) {
        dogsService.deleteDog(id);
        return ResponseEntity.ok().build();
    }
}
