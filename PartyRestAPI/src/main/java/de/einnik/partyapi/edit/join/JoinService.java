package de.einnik.partyapi.edit.join;

import de.einnik.enums.EditFailed;
import de.einnik.partyapi.script.LuaJoinScript;
import de.einnik.response.JoinResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Spring Service where the information is gotten
 * by the inbuild Spring Redis System and then transformed
 * in the JoinResponse, later JSON, format
 */
@Service
public class JoinService {

    private final StringRedisTemplate redis;
    private final LuaJoinScript script;

    public JoinService(
            StringRedisTemplate redis,
            LuaJoinScript script
    ) {
        this.redis = redis;
        this.script = script;
    }

    /**
     * Returns the DestroyResponse, by the Player UUID and if
     * possible directly adds the Player via Lua Script or returns errors while
     * doing so
     */
    @Async
    public CompletableFuture<@NotNull JoinResponse> getJoinResponse(@NotNull String requestUUID, @NotNull String userUUID) {
        String partyKey = redis.opsForValue().get("user:" + userUUID + ":party");

        if (partyKey != null) return CompletableFuture.completedFuture(JoinResponse.none(EditFailed.ALREADY_PARTY_LEADER));

        String userKey = "user:" + userUUID + ":party";
        String partyID = "party:" + requestUUID;
        String memberKey = partyID + ":members";

        Long result = redis.execute(
                script.getScript(),
                List.of(
                        userKey,
                        partyID,
                        memberKey
                ),
                userUUID
        );

        Map<Object, Object> partyData =
                redis.opsForHash().entries(partyID);

        Set<String> partyMembersString =
                redis.opsForSet().members(partyID + ":members");

        UUID partyLeader = UUID.fromString((String) partyData.get("leader"));
        int MaxMembers = Integer.parseInt((String) partyData.get("maxMembers"));
        long createdAt = Long.parseLong((String) partyData.get("timeStamp"));

        Set<UUID> members = partyMembersString.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        return CompletableFuture.completedFuture(switch (result.intValue()) {
            case -2 -> JoinResponse.none(EditFailed.PARTY_NOT_EXIST);
            case -1 -> JoinResponse.none(EditFailed.ALREADY_IN_PARTY);
            case 0 -> JoinResponse.none(EditFailed.PARTY_FULL);
            case 1 -> JoinResponse.of(
                    UUID.fromString(partyID),
                    partyLeader,
                    members,
                    MaxMembers,
                    createdAt
            );
            default -> JoinResponse.none(EditFailed.UNKNOWN);
        });
    }
}