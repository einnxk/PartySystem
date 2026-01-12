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
public class JoinResponse {

    @Getter
    private boolean success;
    @Getter
    private String message;

    @Getter
    private UUID partyID;
    @Getter
    private UUID partyLeader;
    @Getter
    private Set<UUID> partyMembers;

    @Getter
    private int maxMembers;

    @Getter
    private long timeStamp;

    /**
     * Empty Constructor for the JSON Format
     * When the values should be null there should not be
     * null everywhere in the JSON Values
     */
    public JoinResponse() {}

    /**
     * Returns the JoinResponse if the requested user
     * is not able to join the party at a time, which
     * the Reason why he cant
     */
    public static @NotNull JoinResponse none(@NotNull EditFailed failed){
        JoinResponse response = new JoinResponse();
        response.success =  false;
        response.message = failed.string();
        return response;
    }

    /**
     * Returns a Join Response in JSON Format with all
     * needed Parameters and gives basically the party
     * back to the request
     */
    public static @NotNull JoinResponse of(
            UUID partyID,
            UUID partyLeader,
            Set<UUID> partyMembers,
            int maxMembers,
            long timeStamp
    ) {
        JoinResponse response = new JoinResponse();
        response.partyID = partyID;
        response.partyLeader = partyLeader;
        response.partyMembers = partyMembers;
        response.maxMembers = maxMembers;
        response.timeStamp = timeStamp;
        return response;
    }
}