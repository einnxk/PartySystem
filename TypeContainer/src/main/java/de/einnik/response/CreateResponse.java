package de.einnik.response;

import de.einnik.enums.CreateFailed;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Response class, this formats the information
 * the Requests needs into JSON format and handles
 * Null Checks directly in Here
 */
public class CreateResponse {

    @Getter
    private boolean success;

    @Getter
    private String message;

    /**
     * Empty Constructor for the JSON Format
     * When the values should be null there should not be
     * null everywhere in the JSON Values
     */
    public CreateResponse(){}

    /**
     * Returns a Create Response in JSON Format with all
     * needed Parameters when the creation failed
     */
    public static @NotNull CreateResponse of(@NotNull CreateFailed failed){
        CreateResponse response = new CreateResponse();
        response.success = false;
        response.message = failed.string();
        return response;
    }

    /**
     * Returns a Create Response in JSON Format with all
     * needed Parameters
     */
    public static  @NotNull CreateResponse none(){
        CreateResponse response = new CreateResponse();
        response.success = true;
        return response;
    }
}