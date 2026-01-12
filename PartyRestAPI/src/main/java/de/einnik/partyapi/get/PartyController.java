package de.einnik.partyapi.get;

import de.einnik.response.PartyResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Spring Controller which handles the Request about
 * Party Information and returns them as a PartyResponse,
 * which is prepared in the PartyService class
 */
@RestController
@RequestMapping("/party-get")
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping("/{requestUUID}")
    public CompletableFuture<PartyResponse> getPlayerPartyInformation(@PathVariable UUID requestUUID){
        return partyService.getPartyResponse(requestUUID);
    }
}