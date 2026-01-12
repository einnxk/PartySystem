package de.einnik.party.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.einnik.party.Party;
import de.einnik.party.data.PartySnapshot;
import de.einnik.response.PartyResponse;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Velocity Listener for Player Connections
 * around the Server
 */
public class ServerSwitchListener {

    /**
     * We need the Proxy Server to get the Players
     * later on
     */
    private final ProxyServer proxy;
    public ServerSwitchListener(ProxyServer proxy) {
        this.proxy = proxy;
    }

    /**
     * Event before the Player is connected to another Server,
     * here whe cache the Player Party if existing
     */
    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        RegisteredServer targetServer = event.getResult().getServer().orElse(null);
        RegisteredServer previousServer = player.getCurrentServer()
                .map(ServerConnection::getServer)
                .orElse(null);

        if (targetServer == null) return;

        CompletableFuture<PartyResponse> future =
                Party.getApplication().getPartyByPlayer(player.getUniqueId());

        future.thenAcceptAsync(partyResponse -> {
            if (!(partyResponse.isInParty())) return;

            PartySnapshot snapshot = PartySnapshot.of(
                    partyResponse.getPartyID(),
                    partyResponse.getLeaderID(),
                    partyResponse.getPartyMembers(),
                    partyResponse.getMaxMembers(),
                    partyResponse.getTimeStamp()
            );

            Party.getCache().put(player.getUniqueId(), snapshot);
        });
    }

    /**
     * Event after the Player is connected here we get the Party
     * from the cache if existing and sends the Party Members also
     * on tho the server
     */
    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        RegisteredServer currentServer = event.getServer();

        PartySnapshot snapshot = Party.getCache().get(player.getUniqueId());
        if (snapshot == null) return;

        Set<UUID> uuidMembers = snapshot.getMembers();

        Set<Player> players = new HashSet<>();
        uuidMembers.forEach(uuid -> {
            Optional<Player> playerOptional = proxy.getPlayer(uuid);
            playerOptional.ifPresent(players::add);
        });

        players.forEach(targetPlayer -> {
            ConnectionRequestBuilder connectionRequestBuilder =
                    targetPlayer.createConnectionRequest(currentServer);

            connectionRequestBuilder.connect();
        });
    }
}