package de.einnik.partyapi.create;

import de.einnik.enums.CreateFailed;
import de.einnik.partyapi.script.LuaCreateScript;
import de.einnik.response.CreateResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Spring Service where the information is gotten
 * by the inbuild Spring Redis System and then transformed
 * in the CreateResponse, later JSON, format
 */
@Service
public class CreateService {

    private final StringRedisTemplate redis;
    private final LuaCreateScript script;

    public CreateService(
            StringRedisTemplate redis,
            LuaCreateScript script
    ) {
        this.redis = redis;
        this.script = script;
    }

    /**
     * Returns the CreateResponse, by the Player UUID and if
     * possible directly creates the Party via Lua Script or
     * returns errors while doing so
     */
    @Async
    public CompletableFuture<CreateResponse> createResponse(@NotNull UUID requestID, int maxMembers) {
        String partyKey = redis.opsForValue().get("user:" + requestID + ":party");

        if (partyKey != null) return CompletableFuture.completedFuture(CreateResponse.of(CreateFailed.ALREADY_PARTY_LEADER));

        String userPartyKey = "user" +  requestID + ":party";
        String partyID = "party:" + UUID.randomUUID();
        String members = partyID + ":members";

        Long result = redis.execute(
                script.getScript(),
                List.of(
                        userPartyKey,
                        partyID,
                        members
                ),
                requestID.toString(),
                String.valueOf(maxMembers),
                String.valueOf(System.currentTimeMillis())
        );

        if (result == 0) return CompletableFuture.completedFuture(CreateResponse.of(CreateFailed.ALREADY_IN_PARTY));

        String key = redis.opsForValue().get("user:" + requestID + ":party");
        if (key == null) return CompletableFuture.completedFuture(CreateResponse.of(CreateFailed.UNKNOWN));

        return CompletableFuture.completedFuture(CreateResponse.none());
    }
}