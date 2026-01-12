package de.einnik.partyapi.script;

import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Util class for a User UUID trying to leave a party
 * Spring component to provide the lua script
 * for faster redis operations as a DefaultRedisScript
 * provided by Spring
 */
@Component
public class LuaLeaveScript {

    @Getter
    private final DefaultRedisScript<List> script;

    public LuaLeaveScript() {
        this.script = new DefaultRedisScript<>();
        this.script.setLocation(
                new ClassPathResource("redis/leave_party.lua")
        );
        this.script.setResultType(List.class);
    }
}