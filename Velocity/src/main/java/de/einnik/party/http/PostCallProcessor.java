package de.einnik.party.http;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.einnik.party.Party;
import de.einnik.response.CreateResponse;
import de.einnik.response.DestroyResponse;
import de.einnik.response.JoinResponse;
import de.einnik.response.LeaveResponse;
import dev.einnxk.CustomColor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Class to further handle the Responses from the
 * Http Calls
 */
public class PostCallProcessor {

    /**
     * We need an Instance of the PartyApplication who calls
     * Http and a Proxy-Server in this class
     */
    private final PartyApplication application;
    private final ProxyServer proxy;
    public PostCallProcessor(PartyApplication application, ProxyServer proxy) {
        this.application = application;
        this.proxy = proxy;
    }

    /**
     * Handle what happens when a Player is trying to disband
     * his Party
     */
    public void handlePartyDisband(@NotNull UUID uuid){
        CompletableFuture<DestroyResponse> future
                = application.destroyPartyByPlayer(uuid);

        future.thenAcceptAsync(response -> {
            if (response.isSuccess()){
                Party.getCache().remove(uuid);

                Set<UUID> partyMembers = response.getMembers();

                Optional<Player> leader = proxy.getPlayer(uuid);
                if (leader.isEmpty()) return;
                Player player = leader.get();

                player.sendMessage(
                        Party.PREFIX.append(
                                Component.text(player.getUsername(), CustomColor.LIGHT_PURPLE.color())
                        ).append(
                                Component.text(" hat die Party ", CustomColor.LIGHT_GRAY.color())
                        ).append(
                                Component.text("verlassen", CustomColor.RED.color())
                        )
                );

                for (UUID unique : partyMembers){
                    Optional<Player> optionalTarget = proxy.getPlayer(unique);
                    if (optionalTarget.isEmpty()) return;
                    Player target = optionalTarget.get();

                    target.sendMessage(
                            Party.PREFIX.append(
                                    Component.text(player.getUsername(), CustomColor.LIGHT_PURPLE.color())
                            ).append(
                                    Component.text(" hat die Party ", CustomColor.LIGHT_GRAY.color())
                            ).append(
                                    Component.text("verlassen", CustomColor.RED.color())
                            )
                    );
                }
                return;
            }

            Optional<Player> optionalPlayer = proxy.getPlayer(uuid);
            if (optionalPlayer.isEmpty()) return;
            Player player = optionalPlayer.get();

            String reason = response.getMessage();
            switch (reason){
                case "not_leader":
                    player.sendMessage(
                            Party.PREFIX.append(
                                    Component.text("Du musst der leiter einer Party sein um diese aufzulösen", CustomColor.RED.color())
                            )
                    );
                    break;
                case "not_in_party":
                    player.sendMessage(
                            Party.PREFIX.append(
                                    Component.text("Du bist in keiner Party", CustomColor.RED.color())
                            )
                    );
                    break;
            }
        });
    }

    /**
     * Handle what happens when a Player is trying to
     * create a Party
     */
    public void handlePartyCreate(@NotNull UUID uuid){
        CompletableFuture<CreateResponse> future
                = application.createPartyForPlayer(uuid, 200);

        future.thenAcceptAsync(response -> {
            if (response.isSuccess()){
                Optional<Player> optionalTarget = proxy.getPlayer(uuid);
                if (optionalTarget.isEmpty()) return;
                Player player = optionalTarget.get();

                player.sendMessage(
                        Party.PREFIX.append(
                                Component.text("Es wurde eine Party für dich erstellt", CustomColor.LIME.color())
                        )
                );
                return;
            }

            Optional<Player> optionalTarget = proxy.getPlayer(uuid);
            if (optionalTarget.isEmpty()) return;
            Player player = optionalTarget.get();

            String reason = response.getMessage();
            switch (reason){
                case "already_in_party":
                    player.sendMessage(
                            Party.PREFIX.append(
                                    Component.text("Du bist bereits Mitglied einer Party", CustomColor.RED.color())
                            )
                    );
                    break;
                case "already_party_leader":
                    player.sendMessage(
                            Party.PREFIX.append(
                                    Component.text("Du hast bereits eine Party", CustomColor.RED.color())
                            )
                    );
            }
        });
    }

    /**
     * Handles what happens when a Player is trying to leave
     * a Party
     */
    public void handlePartyLeave(@NotNull UUID uuid){
        CompletableFuture<LeaveResponse> future =
                application.leavePartyByPlayer(uuid);

        future.thenAcceptAsync(response -> {
            if (response.isSuccess()){
                Optional<Player> optionalPlayer = proxy.getPlayer(uuid);
                if (optionalPlayer.isEmpty()) return;
                Player player = optionalPlayer.get();

                player.sendMessage(
                        Party.PREFIX.append(
                                Component.text(player.getUsername(), CustomColor.LIGHT_PURPLE.color())
                        ).append(
                                Component.text(" hat die Party ", CustomColor.LIGHT_GRAY.color())
                        ).append(
                                Component.text("verlassen", CustomColor.RED.color())
                        )
                );

                Set<UUID> partyMembers = response.getMembers();

                for (UUID unique : partyMembers){
                    Optional<Player> optionalTarget = proxy.getPlayer(unique);
                    if (optionalTarget.isEmpty()) return;
                    Player target = optionalTarget.get();

                    target.sendMessage(
                            Party.PREFIX.append(
                                    Component.text(player.getUsername(), CustomColor.LIGHT_PURPLE.color())
                            ).append(
                                    Component.text(" hat die Party ", CustomColor.LIGHT_GRAY.color())
                            ).append(
                                    Component.text("verlassen", CustomColor.RED.color())
                            )
                    );
                }
                return;
            }

            Optional<Player> optionalPlayer = proxy.getPlayer(uuid);
            if (optionalPlayer.isEmpty()) return;
            Player player = optionalPlayer.get();

            player.sendMessage(
                    Party.PREFIX.append(
                            Component.text("Du bist in keiner Party", CustomColor.RED.color())
                    )
            );
        });
    }

    /**
     * Handles what happens when a Player is trying to join
     * a Party
     */
    public void handlePartyJoin(@NotNull UUID uuid, @NotNull UUID partyID){
        CompletableFuture<JoinResponse> future =
                application.joinPartyForPlayer(uuid, partyID);

        future.thenAcceptAsync(response -> {
            if (response.isSuccess()){
                Optional<Player> optionalPlayer = proxy.getPlayer(uuid);
                if (optionalPlayer.isEmpty()) return;
                Player player = optionalPlayer.get();

                Set<UUID> uuidMembers = response.getPartyMembers();

                Set<Player> players = new HashSet<>();
                for (UUID unique: uuidMembers){
                    Optional<Player> optionalTarget = proxy.getPlayer(unique);
                    if (optionalTarget.isEmpty()) return;

                    Player target = optionalTarget.get();
                    players.add(target);
                }

                player.sendMessage(
                        Party.PREFIX.append(
                                Component.text(player.getUsername(), CustomColor.LIGHT_PURPLE.color())
                        ).append(
                                Component.text(" hat die Party ", CustomColor.LIGHT_GRAY.color())
                        ).append(
                                Component.text("betreten", CustomColor.LIME.color())
                        )
                );

                players.forEach(target -> {
                    target.sendMessage(
                            Party.PREFIX.append(
                                    Component.text(player.getUsername(), CustomColor.LIGHT_PURPLE.color())
                            ).append(
                                    Component.text(" hat die Party ", CustomColor.LIGHT_GRAY.color())
                            ).append(
                                    Component.text("betreten", CustomColor.LIME.color())
                            )
                    );
                });
                return;
            }

            Optional<Player> optionalPlayer = proxy.getPlayer(uuid);
            if (optionalPlayer.isEmpty()) return;
            Player player = optionalPlayer.get();

            String reason = response.getMessage();
            switch (reason){
                case "already_party_leader":
                    player.sendMessage(
                            Party.PREFIX.append(
                                    Component.text("Du bist bereits Leiter einer anderen Party", CustomColor.RED.color())
                            )
                    );
                    break;
                case "party_not_exist":
                    player.sendMessage(
                            Party.PREFIX.append(
                                    Component.text("Diese Party gibt es nicht mehr", CustomColor.RED.color())
                            )
                    );
                    break;
                case "already_in_party":
                    player.sendMessage(
                            Party.PREFIX.append(
                                    Component.text("Du bist bereits in einer Party", CustomColor.RED.color())
                            )
                    );
                    break;
                case "party_full":
                    player.sendMessage(
                            Party.PREFIX.append(
                                    Component.text("Diese Party ist voll", CustomColor.RED.color())
                            )
                    );
                    break;
            }
        });
    }
}