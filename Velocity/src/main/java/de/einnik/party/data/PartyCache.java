package de.einnik.party.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Caches the current state of a Party in
 * the PartySnapshot Class format
 */
public class PartyCache {

    private final Map<UUID, PartySnapshot> MAP = new HashMap<>();

    /**
     * Empty Constructor
     */
    public PartyCache() {}

    /**
     * Get Back the PartySnapshot by a player UUID
     * if the value is not null else null will be
     * returned
     */
    public @Nullable PartySnapshot get(@NotNull UUID uuid) {
        return MAP.get(uuid);
    }

    /**
     * Caches the current Party Data and overrides
     * the current value if existent
     */
    public void put(@NotNull UUID uuid, @NotNull PartySnapshot snapshot) {
        MAP.put(uuid, snapshot);
    }

    /**
     * Deletes the current Party Data if existent
     */
    public void remove(@NotNull UUID uuid) {
        MAP.remove(uuid);
    }

    /**
     * Deletes all current cached data from Parties
     */
    public void clear() {
        MAP.clear();
    }
}