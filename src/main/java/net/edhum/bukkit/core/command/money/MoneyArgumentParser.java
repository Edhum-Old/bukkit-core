package net.edhum.bukkit.core.command.money;

import net.edhum.common.command.argument.ArgumentParser;
import net.edhum.common.command.argument.exception.ArgumentException;
import net.edhum.common.command.sender.CommandSender;

public class MoneyArgumentParser implements ArgumentParser<Integer> {

    @Override
    public Integer get(CommandSender sender, String argument) throws ArgumentException {
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new ArgumentException(String.format("%s is not a valid string", argument));
        }
    }
}
