if redis.call("EXISTS", KEYS[1]) == 1 then
    return 0
end

redis.call("HSET", KEYS[2],
        "leader", ARGV[1],
        "maxMembers", ARGV[2],
        "createdAt", ARGV[3]
)

redis.call("SADD", KEYS[3], ARGV[1])

redis.call("SET", KEYS[1], string.sub(KEYS[2], 7))

return 1