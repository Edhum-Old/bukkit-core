package net.edhum.bukkit.core.listener.command;

import com.google.inject.Inject;
import net.edhum.bukkit.api.player.Player;
import net.edhum.bukkit.api.player.repository.PlayerRepository;
import net.edhum.bukkit.api.player.repository.filter.PlayerFilterFactory;
import net.edhum.common.command.Command;
import net.edhum.common.command.CommandTree;
import net.edhum.common.command.repository.CommandRepository;
import net.edhum.common.command.repository.filter.CommandRepositoryFilterFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.Collection;
import java.util.UUID;

public class PlayerCommandSendListener implements Listener {

    private final PlayerRepository playerRepository;
    private final PlayerFilterFactory playerFilterFactory;
    private final CommandRepository commandRepository;
    private final CommandRepositoryFilterFactory commandSenderFilter;

    @Inject
    public PlayerCommandSendListener(PlayerRepository playerRepository,
                                     PlayerFilterFactory playerFilterFactory,
                                     CommandRepository commandRepository,
                                     CommandRepositoryFilterFactory commandSenderFilter) {
        this.playerRepository = playerRepository;
        this.playerFilterFactory = playerFilterFactory;
        this.commandRepository = commandRepository;
        this.commandSenderFilter = commandSenderFilter;
    }

    @EventHandler
    public void onPlayerCommandSend(PlayerCommandSendEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Player player = this.playerRepository.find(this.playerFilterFactory.uuid(uuid)).orElseThrow();

        Collection<CommandTree> allowedCommands = this.commandRepository.findAll(this.commandSenderFilter.sender(player));

        event.getCommands().removeIf(command -> allowedCommands.stream()
                .filter(commandTree -> {
                    Command root = commandTree.getRoot().getCommand();

                    return root.getName().equalsIgnoreCase(command) || root.getAliases().contains(command);
                })
                .findAny().isEmpty());
    }
}
