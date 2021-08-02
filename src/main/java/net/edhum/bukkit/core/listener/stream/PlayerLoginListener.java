package net.edhum.bukkit.core.listener.stream;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.inject.Inject;
import net.edhum.bukkit.api.handshake.HandshakeHandler;
import net.edhum.common.i18n.Language;
import net.edhum.common.i18n.ServerLanguage;
import net.edhum.common.message.MessageBuilderFactory;
import net.edhum.common.utils.Debugger;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerLoginListener implements Listener {

    private final HandshakeHandler handshakeHandler;
    private final Debugger debugger;
    private final Messages messages;

    @Inject
    public PlayerLoginListener(HandshakeHandler handshakeHandler,
                               Debugger debugger,
                               Messages messages) {
        this.handshakeHandler = handshakeHandler;
        this.debugger = debugger;
        this.messages = messages;
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        PlayerProfile bukkitPlayer = event.getPlayerProfile();

        UUID uuid = bukkitPlayer.getId();

        this.debugger.start();

        try {
            this.handshakeHandler.handshake(uuid);
        } catch (Exception e) {
            e.printStackTrace();

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, this.messages.kick());
        } finally {
            this.debugger.end();
        }
    }

    private static class Messages {

        private final MessageBuilderFactory messageBuilderFactory;
        private final Language serverLanguage;

        @Inject
        public Messages(MessageBuilderFactory messageBuilderFactory,
                        @ServerLanguage Language serverLanguage) {
            this.messageBuilderFactory = messageBuilderFactory;
            this.serverLanguage = serverLanguage;
        }

        public Component kick() {
            String message = this.messageBuilderFactory.createMessageBuilder("handshake.error")
                    .build().get(this.serverLanguage);

            return Component.text(message);
        }
    }
}
