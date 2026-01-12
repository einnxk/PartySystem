package de.einnik.response;

import de.einnik.enums.EditFailed;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Response class, this formats the information
 * the Requests needs into JSON format and handles
 * Null Checks directly in Here
 */
public class LeaveResponse {

    @Getter
    private boolean success;
    @Getter
    private String message;

    @Getter
    private Set<UUID> members;

    /**
     * Empty Constructor for the JSON Format
     * When the values should be null there should not be
     * null everywhere in the JSON Values
     */
    public LeaveResponse() {}

    /**
     * Returns the LeaveResponse if the requested user
     * is not able to leave the party at a time, which
     * the Reason why he cant
     */
    public static @NotNull LeaveResponse none(){
        LeaveResponse response = new LeaveResponse();
        response.success = false;
        response.message = EditFailed.NOT_IN_PARTY.string();
        return response;
    }

    /**
     * Returns a Leave Response in JSON Format with all
     * needed Parameters and gives the members of the
     * party back to the request
     */
    public static @NotNull LeaveResponse of(
            @NotNull Set<UUID> members
    ) {
        LeaveResponse response = new LeaveResponse();
        response.success = true;
        response.members = members;
        return response;
    }
}