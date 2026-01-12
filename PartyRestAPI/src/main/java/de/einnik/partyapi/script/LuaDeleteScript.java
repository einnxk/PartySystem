package de.einnik.partyapi.script;

import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

/**
 * Util class for Deleting Partys
 * Spring component to provide the lua script
 * for faster redis operations as a DefaultRedisScript
 * provided by Spring
 */
@Component
public class LuaDeleteScript {

    @Getter
    private final DefaultRedisScript<Long> script;

    public LuaDeleteScript() {
        this.script = new DefaultRedisScript<>();
        this.script.setLocation(
                new ClassPathResource("redis/destroy_party.lua")
        );
        this.script.setResultType(Long.class);
    }
}