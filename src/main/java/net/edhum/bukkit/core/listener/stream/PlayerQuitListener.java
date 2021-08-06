package net.edhum.bukkit.core.listener.stream;

import com.google.inject.Inject;
import net.edhum.bukkit.api.message.context.receiver.BukkitReceiverContextFactory;
import net.edhum.bukkit.api.player.Player;
import net.edhum.bukkit.api.player.PlayerService;
import net.edhum.bukkit.api.player.repository.PlayerRepository;
import net.edhum.bukkit.api.player.repository.filter.PlayerFilterFactory;
import net.edhum.common.message.MessageArgument;
import net.edhum.common.message.MessageBuilder;
import net.edhum.common.message.MessageService;
import net.edhum.common.message.context.writer.WriterContextFactory;
import net.edhum.common.utils.Debugger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final PlayerRepository playerRepository;
    private final PlayerFilterFactory playerFilterFactory;
    private final PlayerService playerService;
    private final Debugger debugger;
    private final Messages messages;

    @Inject
    public PlayerQuitListener(PlayerRepository playerRepository, PlayerFilterFactory playerFilterFactory, PlayerService playerService, Debugger debugger, Messages messages) {
        this.playerRepository = playerRepository;
        this.playerFilterFactory = playerFilterFactory;
        this.playerService = playerService;
        this.debugger = debugger;
        this.messages = messages;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.debugger.start();

        UUID uuid = event.getPlayer().getUniqueId();

        try {
            Player player = this.playerRepository.find(this.playerFilterFactory.uuid(uuid)).orElseThrow();
            this.messages.leave(player);

            this.playerService.unloadPlayer(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.debugger.end();
        }

        event.quitMessage(null);
    }

    private static class Messages {

        private final MessageService messageService;
        private final BukkitReceiverContextFactory receiverContextFactory;
        private final WriterContextFactory writerContextFactory;

        @Inject
        public Messages(MessageService messageService, BukkitReceiverContextFactory receiverContextFactory, WriterContextFactory writerContextFactory) {
            this.messageService = messageService;
            this.receiverContextFactory = receiverContextFactory;
            this.writerContextFactory = writerContextFactory;
        }

        public void leave(Player player) {
            this.messageService.write(
                    new MessageBuilder()
                            .withPath("leave")
                            .withArgument(new MessageArgument("player", player))
                            .build(),
                    this.receiverContextFactory.broadcast(player), this.writerContextFactory.chat());
        }
    }
}
