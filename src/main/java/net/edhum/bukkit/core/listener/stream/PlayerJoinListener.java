package net.edhum.bukkit.core.listener.stream;

import com.google.inject.Inject;
import net.edhum.bukkit.api.message.context.receiver.BukkitReceiverContextFactory;
import net.edhum.bukkit.api.player.Player;
import net.edhum.bukkit.api.player.repository.PlayerRepository;
import net.edhum.bukkit.api.player.repository.filter.PlayerFilterFactory;
import net.edhum.common.message.MessageArgument;
import net.edhum.common.message.MessageBuilderFactory;
import net.edhum.common.message.context.writer.WriterContextFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final PlayerRepository playerRepository;
    private final PlayerFilterFactory playerFilterFactory;
    private final Messages messages;

    @Inject
    public PlayerJoinListener(PlayerRepository playerRepository,
                              PlayerFilterFactory playerFilterFactory,
                              Messages messages) {
        this.playerRepository = playerRepository;
        this.playerFilterFactory = playerFilterFactory;
        this.messages = messages;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Player player = this.playerRepository.find(this.playerFilterFactory.uuid(uuid)).orElseThrow();

        this.messages.handshake(player);

        event.joinMessage(null);
    }

    private static class Messages {

        private final MessageBuilderFactory messageBuilder;
        private final BukkitReceiverContextFactory receiverContextFactory;
        private final WriterContextFactory writerContextFactory;

        @Inject
        public Messages(MessageBuilderFactory messageBuilder, BukkitReceiverContextFactory receiverContextFactory, WriterContextFactory writerContextFactory) {
            this.messageBuilder = messageBuilder;
            this.receiverContextFactory = receiverContextFactory;
            this.writerContextFactory = writerContextFactory;
        }

        public void handshake(Player player) {
            this.messageBuilder.createMessageBuilder("handshake")
                    .withArgument(new MessageArgument("player", player))
                    .build().write(this.receiverContextFactory.broadcast(player), this.writerContextFactory.chat());
        }
    }
}
