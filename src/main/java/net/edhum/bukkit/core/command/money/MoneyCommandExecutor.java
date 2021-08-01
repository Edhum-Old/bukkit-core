package net.edhum.bukkit.core.command.money;

import net.edhum.common.command.execution.CommandExecutor;
import net.edhum.common.command.execution.buffer.ArgumentBuffer;
import net.edhum.common.command.execution.context.CommandContext;
import net.edhum.common.command.execution.result.CommandResult;
import net.edhum.common.command.sender.CommandSender;

public class MoneyCommandExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandContext context, ArgumentBuffer buffer) {
        CommandSender sender = context.getSender();
        sender.sendMessage("Sender has 10 coins");

        return CommandResult.SUCCESS;
    }
}
