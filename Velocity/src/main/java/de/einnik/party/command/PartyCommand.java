package de.einnik.party.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.einnik.party.Party;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Velocity Command for the party
 */
public class PartyCommand implements SimpleCommand {

    /**
     * We need to get the ProxyServer to later
     * get Players by their UUID
     */
    private final ProxyServer server;
    public PartyCommand(ProxyServer server){
        this.server = server;
    }

    /**
     * Executes the Command
     */
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player player)) return;

        if (args.length == 0) {
            player.sendMessage(
                    Party.PREFIX.append(
                            Component.text("Falsche Syntax")
                    )
            );
            return;
        }

        String subArg = args[0].toLowerCase();
        switch (subArg) {
            case "help" -> SubArg.help(player);
            case "create" -> Party.getProcessor().handlePartyCreate(player.getUniqueId());
            case "disband" -> Party.getProcessor().handlePartyDisband(player.getUniqueId());
            case "leave" -> Party.getProcessor().handlePartyLeave(player.getUniqueId());
            case "invite" -> SubArg.invite(player, args, server);
            case "pull" -> SubArg.pull(player, server);
        }
    }

    /**
     * Tab Completer which suggests the commands
     */
    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> completions = new ArrayList<>();
        List<String> toComplete = new ArrayList<>();

        if (args.length == 0){
            completions.add("help");
            toComplete.add("create");
            toComplete.add("disband");
            toComplete.add("leave");
            toComplete.add("invite");
            toComplete.add("pull");
            return completions;
        }

        if (args.length == 1){
            completions.add("help");
            toComplete.add("create");
            toComplete.add("disband");
            toComplete.add("leave");
            toComplete.add("invite");
            toComplete.add("pull");
        }

        if (Objects.equals(args[0], "invite")){
            for (Player player : server.getAllPlayers()){
                completions.add(player.getUsername());
            }
        }

        for (String tc : completions) {
            if (tc.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                toComplete.add(tc);
            }
        }

        return toComplete;
    }
}