local partyKey = KEYS[1]
local membersKey = KEYS[2]

local members = redis.call("SMEMBERS", membersKey)

for _, uuid in ipairs(members) do
  redis.call("DEL", "user:" .. uuid .. ":party")
end

redis.call("DEL", partyKey)
redis.call("DEL", membersKey)

return #members