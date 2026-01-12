package de.einnik.partyapi.destroy;

import de.einnik.response.DestroyResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Spring Controller which handles the Request about
 * Deleting a Party, leaving a Party which returns
 * a DestroyResponse to the request
 */
@RestController
@RequestMapping("party-destroy")
public class DestroyController {

    private final DestroyService destroyService;

    public DestroyController(DestroyService destroyService) {
        this.destroyService = destroyService;
    }

    @GetMapping("/{playerUUID}")
    public CompletableFuture<@NotNull DestroyResponse> getDestroyResponseByPlayerUUID(@PathVariable UUID playerUUID) {
        return destroyService.getDestroyResponse(playerUUID);
    }
}