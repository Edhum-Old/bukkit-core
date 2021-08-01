package net.edhum.bukkit.core.listener;

import com.google.inject.Inject;
import net.edhum.common.command.Command;
import net.edhum.common.command.CommandTree;
import net.edhum.common.command.repository.CommandRepository;
import net.edhum.common.command.repository.filter.CommandRepositoryFilterFactory;
import net.edhum.common.player.Player;
import net.edhum.common.player.repository.PlayerRepository;
import net.edhum.common.player.repository.filter.PlayerRepositoryFilterFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.Set;
import java.util.UUID;

public class PlayerCommandSendListener implements Listener {

    private final PlayerRepository playerRepository;
    private final PlayerRepositoryFilterFactory playerRepositoryFilterFactory;
    private final CommandRepository commandRepository;
    private final CommandRepositoryFilterFactory commandSenderFilter;

    @Inject
    public PlayerCommandSendListener(PlayerRepository playerRepository,
                                     PlayerRepositoryFilterFactory playerRepositoryFilterFactory,
                                     CommandRepository commandRepository,
                                     CommandRepositoryFilterFactory commandSenderFilter) {
        this.playerRepository = playerRepository;
        this.playerRepositoryFilterFactory = playerRepositoryFilterFactory;
        this.commandRepository = commandRepository;
        this.commandSenderFilter = commandSenderFilter;
    }

    @EventHandler
    public void onPlayerCommandSend(PlayerCommandSendEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Player player = this.playerRepository.find(this.playerRepositoryFilterFactory.uuid(uuid)).orElseThrow();

        Set<CommandTree> allowedCommands = this.commandRepository.findAll(this.commandSenderFilter.commandSender(player));

        event.getCommands().removeIf(command -> allowedCommands.stream()
                .filter(commandTree -> {
                    Command root = commandTree.getRoot().getCommand();

                    return root.getName().equalsIgnoreCase(command) || root.getAliases().contains(command);
                })
                .findAny().isEmpty());
    }
}
