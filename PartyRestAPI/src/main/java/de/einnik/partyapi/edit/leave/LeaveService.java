package de.einnik.partyapi.edit.leave;

import de.einnik.partyapi.script.LuaDeleteScript;
import de.einnik.partyapi.script.LuaLeaveScript;
import de.einnik.response.LeaveResponse;
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
 * in the LeaveResponse, later JSON, format
 */
@Service
public class LeaveService {

    private final StringRedisTemplate redis;
    private final LuaDeleteScript deleteScript;
    private final LuaLeaveScript leaveScript;

    public LeaveService(
            StringRedisTemplate redis,
            LuaDeleteScript deleteScript,
            LuaLeaveScript leaveScript
    ) {
        this.redis = redis;
        this.deleteScript = deleteScript;
        this.leaveScript = leaveScript;
    }

    /**
     * Returns the DestroyResponse, by the Player UUID and if
     * possible directly deletes the Party via Lua Script, or removes
     * player from a possible party or returns errors while doing so asynchronously
     */
    @Async
    public CompletableFuture<@NotNull LeaveResponse> getResponse(@NotNull String playerUUID){
        String partyID = redis.opsForValue().get("user:" +  playerUUID + ":party");

        if (partyID != null) {
            String partyKey = "party:" + partyID;

            Set<String> partyKeyMembers =
                    redis.opsForSet().members(partyID + ":members");

            Set<UUID> members = partyKeyMembers.stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());

            redis.execute(
                    deleteScript.getScript(),
                    List.of(
                            partyKey,
                            partyKey + ":members"
                    )
            );

            return CompletableFuture.completedFuture(LeaveResponse.of(
                    members
            ));
        }

        String userPartyKey = "user:" + playerUUID + ":party";

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) redis.execute(
                leaveScript.getScript(),
                List.of(
                        userPartyKey
                ),
                playerUUID
        );

        if (result == null || result.isEmpty()) {
            return CompletableFuture.completedFuture(LeaveResponse.none());
        }

        Set<UUID> remainingMembers = result.stream()
                .skip(1)
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        return CompletableFuture.completedFuture(LeaveResponse.of(
                remainingMembers
        ));
    }
}