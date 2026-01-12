package de.einnik.partyapi.destroy;

import de.einnik.enums.DestroyFailed;
import de.einnik.partyapi.script.LuaDeleteScript;
import de.einnik.response.DestroyResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Spring Service where the information is gotten
 * by the inbuild Spring Redis System and then transformed
 * in the DestroyResponse, later JSON, format
 */
@Service
public class DestroyService {

    private final StringRedisTemplate redis;
    private final LuaDeleteScript script;

    public DestroyService(
            StringRedisTemplate redis,
            LuaDeleteScript script
    ) {
        this.redis = redis;
        this.script = script;
    }

    /**
     * Returns the DestroyResponse, by the Player UUID and if
     * possible directly deletes the Party via Lua Script or returns errors while
     * doing so
     */
    @Async
    public CompletableFuture<@NotNull DestroyResponse> getDestroyResponse(@NotNull UUID requestID) {
        String partyID = redis.opsForValue().get("user:" + requestID + ":party");

        if (partyID == null) return CompletableFuture.completedFuture(DestroyResponse.none(DestroyFailed.NOT_PARTY_LEADER));

        String partyKey = "party:" + partyID;

        Set<String> membersString = redis.opsForSet().members(partyKey + ":members");

        Set<UUID> members = membersString.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        redis.execute(
                script.getScript(),
                List.of(
                        partyKey,
                        partyKey + ":members"
                )
        );

        String key = redis.opsForValue().get("user:" + requestID + ":members");
        if (key != null) return CompletableFuture.completedFuture(DestroyResponse.none(DestroyFailed.UNKNOWN));

        return CompletableFuture.completedFuture(DestroyResponse.of(
                members
        ));
    }
}