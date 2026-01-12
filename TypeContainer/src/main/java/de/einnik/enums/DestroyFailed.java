package de.einnik.enums;

import org.jetbrains.annotations.NotNull;

/**
 * Reasons a Party Destroy can fail, the String is
 * later returned in the JSON format as the Reason it failed
 */
public enum DestroyFailed {

    NOT_IN_PARTY("not_in_party"),
    NOT_PARTY_LEADER("not_leader"),
    UNKNOWN("unknown");

    private final String key;

    DestroyFailed(String key) {
        this.key = key;
    }

    public @NotNull String key(){
        return this.key;
    }
}