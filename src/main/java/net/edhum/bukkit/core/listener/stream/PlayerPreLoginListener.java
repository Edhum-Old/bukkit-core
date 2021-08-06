package net.edhum.bukkit.core.listener.stream;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.inject.Inject;
import net.edhum.bukkit.api.handshake.HandshakeHandler;
import net.edhum.common.i18n.Language;
import net.edhum.common.i18n.ServerLanguage;
import net.edhum.common.message.MessageBuilder;
import net.edhum.common.message.MessageService;
import net.edhum.common.utils.Debugger;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerPreLoginListener implements Listener {

    private final HandshakeHandler handshakeHandler;
    private final Debugger debugger;
    private final Messages messages;

    @Inject
    public PlayerPreLoginListener(HandshakeHandler handshakeHandler,
                                  Debugger debugger,
                                  Messages messages) {
        this.handshakeHandler = handshakeHandler;
        this.debugger = debugger;
        this.messages = messages;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        this.debugger.start();

        PlayerProfile bukkitPlayer = event.getPlayerProfile();
        UUID uuid = bukkitPlayer.getId();

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

        private final MessageService messageService;
        private final Language serverLanguage;

        @Inject
        public Messages(MessageService messageService,
                        @ServerLanguage Language serverLanguage) {
            this.messageService = messageService;
            this.serverLanguage = serverLanguage;
        }

        public Component kick() {
            String message = this.messageService.get(
                    new MessageBuilder()
                            .withPath("handshake.error")
                            .build(),
                    this.serverLanguage);

            return Component.text(message);
        }
    }
}
