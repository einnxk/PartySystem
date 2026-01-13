package de.einnik.party.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.einnik.response.*;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Class to send request to the backend and
 * await the response asynchronously
 */
public class PartyApplication {

    private final HttpClient CLIENT = HttpClientProvider.CLIENT;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080";

    /**
     * Gets Party Information and formats them back
     * into their source class
     */
    public CompletableFuture<PartyResponse> getPartyByPlayer(@NotNull UUID playerUuid) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/party-get/" + playerUuid))
                .header("api-key", "123") // Key is normally of course not 123
                .GET()
                .build();

        return send(request, PartyResponse.class);
    }

    /**
     * Tries to create a Party and then returns the response
     * which the reason it can be created, can't be created
     */
    public CompletableFuture<CreateResponse> createPartyForPlayer(@NotNull UUID playerUuid, int maxMembers) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/party-create/" + playerUuid + "/" + maxMembers))
                .header("api-key", "123") // Key is normally of course not 123
                .GET()
                .build();

        return send(request, CreateResponse.class);
    }

    /**
     * Tries to delete a Party and the returns the response
     * with the reason it can be created, can't be created
     */
    public CompletableFuture<DestroyResponse> destroyPartyByPlayer(@NotNull UUID playerUuid) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/party-destroy/" + playerUuid))
                .header("api-key", "123") // Key is normally of course not 123
                .GET()
                .build();

        return send(request, DestroyResponse.class);
    }

    /**
     * Leaves a Partys by a player UUID or disband the party
     * if the user has his own Party, or returns the reason a
     * party cant be left
     */
    public CompletableFuture<LeaveResponse> leavePartyByPlayer(@NotNull UUID playerUuid) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/party-edit/leave/" + playerUuid))
                .header("api-key", "123") // Key is normally of course not 123
                .GET()
                .build();

        return send(request, LeaveResponse.class);
    }

    /**
     * Joins a Party by the Party ID and a User UUD and if
     * not possibly returns the reason why
     */
    public CompletableFuture<JoinResponse> joinPartyForPlayer(@NotNull UUID playerUuid, @NotNull UUID partyID) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/party-edit/join/" + partyID + "/" + playerUuid))
                .header("api-key", "123") // Key is normally of course not 123
                .GET()
                .build();

        return send(request, JoinResponse.class);
    }

    /**
     * Helper Method
     */
    private <T> @NonNull CompletableFuture<T> send(HttpRequest request, Class<T> type) {
        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, type);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}