package de.einnik.response;

import de.einnik.enums.DestroyFailed;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Response class, this formats the information
 * the Requests needs into JSON format and handles
 * Null Checks directly in Here
 */
public class DestroyResponse {

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
    public DestroyResponse() {}

    /**
     * Returns the DestroyResponse if the requested user
     * cannot destroy his party with the reason
     */
    public static @NotNull DestroyResponse none(@NotNull DestroyFailed failed) {
        DestroyResponse response = new DestroyResponse();
        response.success = false;
        response.message = failed.key();
        return  response;
    }

    /**
     * Returns a Destroy Response in JSON Format with all
     * needed Parameters
     */
    public static @NotNull DestroyResponse of(
            @NotNull Set<UUID> members
    ) {
        DestroyResponse response = new DestroyResponse();
        response.success = true;
        response.message = null;
        response.members = members;
        return response;
    }
}