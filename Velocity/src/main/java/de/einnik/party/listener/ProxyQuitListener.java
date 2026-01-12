package de.einnik.party.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.einnik.party.Party;
import de.einnik.response.LeaveResponse;
import dev.einnxk.CustomColor;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Velocity Listener when a Player is leaving
 * the Proxy-Server
 */
public class ProxyQuitListener {

    /**
     * We need the Proxy Server to get the Players
     * later on
     */
    private final ProxyServer proxy;
    public ProxyQuitListener(ProxyServer proxy) {
        this.proxy = proxy;
    }

    /**
     * When the Player is in a party, he is leaving
     * the Party when he is quitting the proxy server
     */
    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();

        CompletableFuture<LeaveResponse> future =
                Party.getApplication().leavePartyByPlayer(player.getUniqueId());

        future.thenAcceptAsync(partyResponse -> {
            if (!(partyResponse.isSuccess())) return;

            Set<UUID> uuidSet = partyResponse.getMembers();

            Set<Player> playerSet = new HashSet<>();
            uuidSet.forEach(uuid -> {
                Optional<Player> playerOptional = proxy.getPlayer(uuid);
                playerOptional.ifPresent(playerSet::add);
            });

            playerSet.forEach(targetPlayer -> {
                targetPlayer.sendMessage(
                        Party.PREFIX.append(
                                Component.text(player.getUsername(), CustomColor.LIGHT_PURPLE.color())
                        ).append(
                                Component.text(" hat die Party ", CustomColor.LIGHT_GRAY.color())
                        ).append(
                                Component.text("verlassen", CustomColor.RED.color())
                        )
                );
            });

            Party.getCache().remove(player.getUniqueId());
        });
    }
}