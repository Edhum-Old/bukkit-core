package net.edhum.bukkit.core.listener.command;

import com.google.inject.Inject;
import net.edhum.bukkit.api.player.Player;
import net.edhum.bukkit.api.player.repository.PlayerRepository;
import net.edhum.bukkit.api.player.repository.filter.PlayerFilterFactory;
import net.edhum.common.command.CommandTree;
import net.edhum.common.command.repository.CommandRepository;
import net.edhum.common.command.repository.filter.CommandRepositoryFilterFactory;
import net.edhum.common.message.MessageBuilderFactory;
import net.edhum.common.message.context.receiver.ReceiverContextFactory;
import net.edhum.common.message.context.writer.WriterContextFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Optional;
import java.util.UUID;

public class PlayerCommandPreprocessListener implements Listener {

    private final PlayerRepository playerRepository;
    private final PlayerFilterFactory playerFilterFactory;
    private final CommandRepository commandRepository;
    private final CommandRepositoryFilterFactory commandRepositoryFilterFactory;
    private final Messages messages;

    @Inject
    public PlayerCommandPreprocessListener(PlayerRepository playerRepository,
                                           PlayerFilterFactory playerFilterFactory,
                                           CommandRepository commandRepository,
                                           CommandRepositoryFilterFactory commandRepositoryFilterFactory,
                                           Messages messages) {
        this.playerRepository = playerRepository;
        this.playerFilterFactory = playerFilterFactory;
        this.commandRepository = commandRepository;
        this.commandRepositoryFilterFactory = commandRepositoryFilterFactory;
        this.messages = messages;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Player sender = this.playerRepository.find(this.playerFilterFactory.uuid(uuid)).orElseThrow();
        String[] args = event.getMessage().split(" ");

        String command = args[0].substring(1); // Slash removal

        Optional<CommandTree> optionalTree = this.commandRepository.find(this.commandRepositoryFilterFactory.name(command));

        if (optionalTree.isEmpty()) {
            this.messages.unknownCommand(sender);

            event.setCancelled(true);
        }
    }

    private static class Messages {

        private final MessageBuilderFactory messageBuilderFactory;
        private final ReceiverContextFactory receiverContextFactory;
        private final WriterContextFactory writerContextFactory;

        @Inject
        public Messages(MessageBuilderFactory messageBuilderFactory, ReceiverContextFactory receiverContextFactory, WriterContextFactory writerContextFactory) {
            this.messageBuilderFactory = messageBuilderFactory;
            this.receiverContextFactory = receiverContextFactory;
            this.writerContextFactory = writerContextFactory;
        }

        public void unknownCommand(Player sender) {
            this.messageBuilderFactory.createMessageBuilder("command.unknown_command")
                    .build().write(this.receiverContextFactory.single(sender), this.writerContextFactory.chat());
        }
    }
}
