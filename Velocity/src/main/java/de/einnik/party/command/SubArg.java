package de.einnik.party.command;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.einnik.party.Party;
import de.einnik.response.PartyResponse;
import dev.einnxk.CustomColor;
import dev.einnxk.Symbol;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Class to handle the Sub-Commands of the
 * Main Party Command
 */
public class SubArg {

    /**
     * Sends all Members in the Party of a Player to the
     * current Party leaders server
     */
    public static void pull(@NotNull Player player, ProxyServer proxy) {
        CompletableFuture<PartyResponse> future =
                Party.getApplication().getPartyByPlayer(player.getUniqueId());

        future.thenAcceptAsync(partyResponse -> {
            if (!(partyResponse.isInParty())){
                player.sendMessage(
                        Party.PREFIX.append(
                                Component.text("Du leitest aktuell keine Party")
                        )
                );
                return;
            }

            Set<UUID> partyMembers =
                    partyResponse.getPartyMembers();

            Set<Player> players = new HashSet<>();

            for (UUID uuid : partyMembers) {
                Optional<Player> opP = proxy.getPlayer(uuid);
                if (opP.isEmpty()) return;
                Player p = opP.get();

                players.add(p);
            }

            players.forEach(partyPlayer -> {
                RegisteredServer server = player.getCurrentServer().get().getServer();

                partyPlayer.createConnectionRequest(server).connect();
            });

            player.sendMessage(
                    Party.PREFIX.append(
                            Component.text("Deine Party wurde auf deinen aktuellen Server gezogen", CustomColor.YELLOW.color())
                    )
            );
        });
    }

    /**
     * Sends an invitation to the Party to another Player
     */
    public static void invite(@NotNull Player player, String [] args, ProxyServer proxy){
        if (args.length < 2){
            player.sendMessage(
                    Party.PREFIX.append(
                            Component.text("Nutze /party invite <Player>", CustomColor.RED.color())
                    )
            );
            return;
        }

        Optional<Player> optionalTarget = proxy.getPlayer(args[1]);
        if (optionalTarget.isEmpty()){
            player.sendMessage(Party.PREFIX.append(
                    Component.text("Dieser Spieler ist gerade nicht online", CustomColor.RED.color())
            ));
            return;
        }
        Player target = optionalTarget.get();

        CompletableFuture<PartyResponse> future =
                Party.getApplication().getPartyByPlayer(player.getUniqueId());

        future.thenAcceptAsync(response -> {
            if (!(response.isInParty())){
                player.sendMessage(Party.PREFIX.append(
                        Component.text("Du hast keine Party")
                ));
                return;
            }

            UUID uuid = response.getPartyID();

            target.sendMessage(
                    Party.PREFIX.append(
                            Component.text(player.getUsername(), CustomColor.LIGHT_PURPLE.color())
                    ).append(
                            Component.text(" hat dich in eine Party eingeladen", CustomColor.LIGHT_GRAY.color())
                    ).append(
                            Component.text(" [" + Symbol.CHECK_MARK.string() + "] ")
                                    .clickEvent(ClickEvent.callback(audience -> {
                                        Player p = (Player) audience;

                                        Party.getProcessor().handlePartyJoin(p.getUniqueId(), uuid);
                                    }))
                    ).append(
                            Component.text(" [" + Symbol.CROSS_MARK.string() + "] ")
                                    .clickEvent(ClickEvent.callback(audience -> {
                                        Player p = (Player) audience;

                                        player.sendMessage(
                                                Party.PREFIX.append(
                                                        Component.text(p.getUsername(), CustomColor.LIGHT_PURPLE.color())
                                                ).append(
                                                        Component.text(" hat deine Anfrage ", CustomColor.LIGHT_GRAY.color())
                                                ).append(
                                                        Component.text("abgelehnt", CustomColor.RED.color())
                                                )
                                        );
                                    }))
                    )
            );

            player.sendMessage(
                    Party.PREFIX.append(
                            Component.text(target.getUsername(), CustomColor.LIGHT_PURPLE.color())
                    ).append(
                            Component.text(" wurde in deine Party ", CustomColor.LIGHT_GRAY.color())
                    ).append(
                            Component.text("eingeladen", CustomColor.LIME.color())
                    )
            );
        });
    }

    /**
     * Sends Command help as a Sub-Command
     */
    public static void help(@NotNull Player player){
        player.sendMessage(
                Party.PREFIX.append(
                        Component.text("Hilfe zum Partysystem", CustomColor.YELLOW.color(), TextDecoration.BOLD)
                )
        );
        player.sendMessage(sendPartyHelp("/party help ", "Öffne diese Hilfe"));
        player.sendMessage(sendPartyHelp("/party create ", "Erstelle eine leere Party"));
        player.sendMessage(sendPartyHelp("/party disband ", "Löse deine aktuelle Party auf"));
        player.sendMessage(sendPartyHelp("/party leave ", "Verlasse die Party in der du aktuell bist"));
        player.sendMessage(sendPartyHelp("/party invite <Player> ", "Lade jemanden zu deiner Party ein"));
        player.sendMessage(sendPartyHelp("/party pull ", "Ziehe deine Party auf deinen aktuellen Server"));
    }

    /**
     * Util Method for sending Party Help
     */
    private static Component sendPartyHelp(String cmd, String description){
        return Component.text(cmd, CustomColor.LIGHT_PURPLE.color()).append(
                Component.text("- ", CustomColor.DARK_GRAY.color())
        ).append(
                Component.text(description, CustomColor.LIGHT_PURPLE.color())
        );
    }
}