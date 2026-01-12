package de.einnik.party.data;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Class representing a Party during a time
 * period
 */
public class PartySnapshot {

    @Getter
    private UUID partyID;

    @Getter
    private UUID leaderID;
    @Getter
    private Set<UUID> members;
    @Getter
    private int maxMembers;
    @Getter
    private long timeStamp;

    /**
     * Empty Constructor
     */
    public PartySnapshot() {}

    /**
     * Creating a new PartySnapshot with the needed
     * Parameters
     */
    public static @NotNull PartySnapshot of(
            @NotNull UUID partyID,
            @NotNull UUID leaderID,
            @NotNull Set<UUID> members,
            int maxMembers,
            long timeStamp
    ) {
        PartySnapshot snapshot = new PartySnapshot();
        snapshot.partyID = partyID;
        snapshot.leaderID = leaderID;
        snapshot.members = members;
        snapshot.maxMembers = maxMembers;
        snapshot.timeStamp = timeStamp;
        return snapshot;
    }
}