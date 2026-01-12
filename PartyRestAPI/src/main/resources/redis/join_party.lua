if redis.call("EXISTS", KEYS[1]) == 1 then
    return -1
end

if redis.call("EXISTS", KEYS[2]) == 0 then
    return -2
end

local maxMembers = tonumber(redis.call("HGET", KEYS[2], "maxMembers"))
local currentMembers = redis.call("SCARD", KEYS[3])

if currentMembers >= maxMembers then
    return 0
end

redis.call("SADD", KEYS[3], ARGV[1])
redis.call("SET", KEYS[1], string.sub(KEYS[2], 7))

return 1