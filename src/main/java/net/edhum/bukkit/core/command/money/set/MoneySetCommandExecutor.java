package net.edhum.bukkit.core.command.money.set;

import net.edhum.common.command.execution.CommandExecutor;
import net.edhum.common.command.execution.buffer.ArgumentBuffer;
import net.edhum.common.command.execution.context.CommandContext;
import net.edhum.common.command.execution.result.CommandResult;
import net.edhum.common.command.sender.CommandSender;

import java.util.Optional;

public class MoneySetCommandExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandContext context, ArgumentBuffer buffer) {
        int coins = buffer.readArgument();
        Optional<String> player = buffer.readOptionalArgument();

        CommandSender sender = context.getSender();
        sender.sendMessage(String.format("%d : %s", coins, player.orElse("sender")));

        return CommandResult.SUCCESS;
    }
}
