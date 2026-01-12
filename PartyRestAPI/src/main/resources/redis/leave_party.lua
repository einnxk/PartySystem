local partyId = redis.call("GET", KEYS[1])
if not partyId then
    return nil
end

local partyKey = "party:" .. partyId
local membersKey = partyKey .. ":members"

redis.call("SREM", membersKey, ARGV[1])
redis.call("DEL", KEYS[1])

local remainingMembers = redis.call("SMEMBERS", membersKey)

local result = {}
table.insert(result, partyId)

for _, uuid in ipairs(remainingMembers) do
    table.insert(result, uuid)
end

return result