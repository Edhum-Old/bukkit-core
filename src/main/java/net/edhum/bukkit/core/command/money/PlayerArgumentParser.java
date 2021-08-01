package net.edhum.bukkit.core.command.money;

import net.edhum.common.command.argument.ArgumentParser;
import net.edhum.common.command.sender.CommandSender;

import java.util.Arrays;
import java.util.Collection;

public class PlayerArgumentParser implements ArgumentParser<String> {

    @Override
    public String get(CommandSender sender, String argument) {
        return argument;
    }

    @Override
    public Collection<String> tabComplete(CommandSender sender) {
        return Arrays.asList("Juan", "William", "Martin");
    }
}
