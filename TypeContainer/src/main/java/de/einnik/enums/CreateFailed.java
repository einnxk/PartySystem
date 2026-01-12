package de.einnik.enums;

/**
 * Reasons a Party Create can fail, the String is
 * later returned in the JSON format as the Reason it failed
 */
public enum CreateFailed {

    ALREADY_IN_PARTY("already_in_party"),
    ALREADY_PARTY_LEADER("already_party_leader"),
    UNKNOWN("unknown");

    private  final String string;

    CreateFailed(String string) {
        this.string = string;
    }

    public String string() {
        return string;
    }
}