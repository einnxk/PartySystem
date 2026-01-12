package de.einnik.enums;

public enum EditFailed {

    PARTY_NOT_EXIST("party_not_exist"),
    NOT_IN_PARTY("not_in_party"),
    ALREADY_IN_PARTY("already_in_party"),
    ALREADY_PARTY_LEADER("already_party_leader"),
    PARTY_FULL("party_full"),
    UNKNOWN("unknown");

    private final String string;

    EditFailed(String string) {
        this.string = string;
    }

    public String string() {
        return string;
    }
}