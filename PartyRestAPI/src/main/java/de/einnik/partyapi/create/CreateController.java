package de.einnik.partyapi.create;

import de.einnik.response.CreateResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Spring Controller which handles the Request about
 * Creating a Party, creating a Party which returns
 * a CreateResponse to the request
 */
@RestController
@RequestMapping("/party-create")
public class CreateController {

    private final CreateService service;

    public CreateController(CreateService service) {
        this.service = service;
    }

    @GetMapping("{playerUUID}/{maxMember}")
    public CompletableFuture<@NotNull CreateResponse> getCreateResponseByPlayerUUID(@PathVariable String playerUUID, @PathVariable int maxMember) {
        return service.createResponse(UUID.fromString(playerUUID), maxMember);
    }
}