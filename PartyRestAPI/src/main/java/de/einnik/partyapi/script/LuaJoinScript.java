package de.einnik.partyapi.script;

import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

/**
 * Util class for a User UUID trying to join a party
 * Spring component to provide the lua script
 * for faster redis operations as a DefaultRedisScript
 * provided by Spring
 */
@Component
public class LuaJoinScript {

    @Getter
    private final DefaultRedisScript<Long> script;

    public LuaJoinScript(StringRedisTemplate redis) {
        this.script = new DefaultRedisScript<>();
        this.script.setLocation(
                new ClassPathResource("redis/join_party.lua")
        );
        this.script.setResultType(Long.class);
    }
}