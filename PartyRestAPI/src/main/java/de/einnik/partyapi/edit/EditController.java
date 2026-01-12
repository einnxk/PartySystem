package de.einnik.partyapi.edit;

import de.einnik.partyapi.edit.join.JoinService;
import de.einnik.partyapi.edit.leave.LeaveService;
import de.einnik.response.JoinResponse;
import de.einnik.response.LeaveResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * Spring Controller which handles the Request about
 * a performed Action Edit Information and returns them as a
 * Join-/Leave Response, which is prepared in the
 * LeaveService/JoinService class
 */
@RestController
@RequestMapping("/party-edit")
public class EditController {

    private final JoinService joinService;
    private final LeaveService leaveService;

    public EditController(JoinService joinService, LeaveService leaveService) {
        this.joinService = joinService;
        this.leaveService = leaveService;
    }

    /**
     * Handles Party Join Responses asynchronously
     */
    @GetMapping("/join/{partyID}/{playerUUID}")
    public CompletableFuture<JoinResponse> getJoinResponseByPlayerUUID(@PathVariable String partyID, @PathVariable String playerUUID) {
        return joinService.getJoinResponse(playerUUID, partyID);
    }

    /**
     * Handles Party Leave Responses asynchronously
     */
    @GetMapping("/leave/{playerUUID}")
    public CompletableFuture<LeaveResponse> getLeaveResponseByPlayerUUID(@PathVariable String playerUUID) {
        return leaveService.getResponse(playerUUID);
    }
}