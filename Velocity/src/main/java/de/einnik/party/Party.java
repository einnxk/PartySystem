package de.einnik.party;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.einnik.party.command.PartyCommand;
import de.einnik.party.data.PartyCache;
import de.einnik.party.http.PartyApplication;
import de.einnik.party.http.PostCallProcessor;
import de.einnik.party.listener.ProxyQuitListener;
import de.einnik.party.listener.ServerSwitchListener;
import dev.einnxk.ColorLibraryAPI;
import dev.einnxk.CustomColor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

@Plugin(
    id = "party",
    name = "party",
    version = "1.0"
    ,description = "Party System Velocity Adapter"
    ,authors = {"EinNik"}
)
public class Party {

    public static final Component PREFIX = ColorLibraryAPI.addBracketsToPrefix(Component.text("Party", CustomColor.LIGHT_PURPLE.color()));

    @Inject private ProxyServer proxy;
    @Inject private Logger logger;
    @Getter
    private static PartyApplication application;
    @Getter
    private static PartyCache cache;
    @Getter
    private static PostCallProcessor processor;

    /**
     * Start of the Proxy Server
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        application = new PartyApplication();
        cache = new PartyCache();
        processor = new PostCallProcessor(application, proxy);

        this.initializeListeners();

        CommandMeta meta = proxy.getCommandManager().metaBuilder("party").build();
        proxy.getCommandManager().register(meta, new PartyCommand(proxy));
    }

    /**
     * Stop of the Proxy Server
     */
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        cache.clear();
    }

    private void initializeListeners(){
        proxy.getEventManager().register(this, new ServerSwitchListener(proxy));
        proxy.getEventManager().register(this, new ProxyQuitListener(proxy));
    }
}