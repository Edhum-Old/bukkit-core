package net.edhum.bukkit.core.command.money.show;

import net.edhum.common.command.execution.CommandExecutor;
import net.edhum.common.command.execution.buffer.ArgumentBuffer;
import net.edhum.common.command.execution.context.CommandContext;
import net.edhum.common.command.execution.result.CommandResult;
import net.edhum.common.command.sender.CommandSender;

import java.util.Optional;

public class MoneyShowCommandExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandContext context, ArgumentBuffer buffer) {
        Optional<String> player = buffer.readOptionalArgument();

        CommandSender sender = context.getSender();
        sender.sendMessage(String.format("%s : %d", player.orElse("sender"), 10));

        return CommandResult.SUCCESS;
    }
}
