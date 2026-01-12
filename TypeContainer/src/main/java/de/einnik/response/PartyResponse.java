package de.einnik.response;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Response class, this formats the information
 * the Requests needs into JSON format and handles
 * Null Checks directly in Here
 */
public class PartyResponse {

    @Getter
    private boolean inParty;

    @Getter
    private UUID partyID;
    @Getter
    private UUID leaderID;

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
    public PartyResponse() {}

    /**
     * Returns the PartyResponse if the requested user
     * does not have a party
     */
    public static @NotNull PartyResponse none(){
        PartyResponse partyResponse = new PartyResponse();
        partyResponse.inParty = false;
        return partyResponse;
    }

    /**
     * Returns a Party Response in JSON Format with all
     * needed Parameters
     */
    public static @NotNull PartyResponse of(
            UUID partyID,
            UUID partyLeader,
            Set<UUID> partyMembers,
            int maxMembers,
            long timeStamp
    ) {
        PartyResponse partyResponse = new PartyResponse();
        partyResponse.inParty = true;
        partyResponse.partyID = partyID;
        partyResponse.leaderID = partyLeader;
        partyResponse.partyMembers = partyMembers;
        partyResponse.maxMembers = maxMembers;
        partyResponse.timeStamp = timeStamp;
        return partyResponse;
    }
}