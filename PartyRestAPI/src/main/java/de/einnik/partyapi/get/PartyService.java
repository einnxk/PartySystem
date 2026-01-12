package de.einnik.partyapi.get;

import de.einnik.response.PartyResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Spring Service where the information is gotten
 * by the inbuild Spring Redis System and then transformed
 * in the PartyResponse, later JSON, format
 */
@Service
public class PartyService {

    private final StringRedisTemplate redis;

    public PartyService(StringRedisTemplate stringRedisTemplate) {
        this.redis = stringRedisTemplate;
    }

    /**
     * Returns the PartyResponse, by the Player UUID asynchronously
     */
    @Async
    public @NotNull CompletableFuture<@NotNull PartyResponse> getPartyResponse(@NotNull UUID requestID) {
        String partyID = redis.opsForValue().get("user:" + requestID + ":party");

        if (partyID == null) return CompletableFuture.completedFuture(PartyResponse.none());

        String partyKey = "party:" + requestID;

        Map<Object, Object> partyData =
                redis.opsForHash().entries(partyKey);

        Set<String> partyMembersString =
                redis.opsForSet().members(partyKey + ":members");

        UUID partyLeader = UUID.fromString((String) partyData.get("leader"));
        int MaxMembers = Integer.parseInt((String) partyData.get("maxMembers"));
        long createdAt = Long.parseLong((String) partyData.get("timeStamp"));

        Set<UUID> members = partyMembersString.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        return CompletableFuture.completedFuture(PartyResponse.of(
                UUID.fromString(partyID),
                partyLeader,
                members,
                MaxMembers,
                createdAt
        ));
    }
}