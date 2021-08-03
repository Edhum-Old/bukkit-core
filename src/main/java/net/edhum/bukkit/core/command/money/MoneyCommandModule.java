package net.edhum.bukkit.core.command.money;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.ProvidesIntoSet;
import net.edhum.bukkit.core.command.money.set.MoneySetCommandExecutor;
import net.edhum.bukkit.core.command.money.show.MoneyShowCommandExecutor;
import net.edhum.common.command.CommandBuilder;
import net.edhum.common.command.CommandNode;
import net.edhum.common.command.CommandTree;

import javax.inject.Named;

public class MoneyCommandModule extends AbstractModule {

    @Provides
    @Named("money.set")
    public CommandNode provideMoneySetCommand(CommandBuilder builder, MoneyArgumentParser moneyArgumentParser, PlayerArgumentParser playerArgumentParser, MoneySetCommandExecutor executor) {
        return new CommandNode(builder.withName("set")
                .withArgument("money", moneyArgumentParser, true)
                .withArgument("player", playerArgumentParser, false)
                .withExecutor(executor)
                .build());
    }

    @Provides
    @Named("money.show")
    public CommandNode provideMoneyShowCommand(CommandBuilder builder, PlayerArgumentParser playerArgumentParser, MoneyShowCommandExecutor executor) {
        return new CommandNode(builder.withName("show")
                .withArgument("player", playerArgumentParser, false)
                .withExecutor(executor)
                .build());
    }

    @ProvidesIntoSet
    public CommandTree provideMoneyCommand(CommandBuilder builder, PlayerArgumentParser playerArgument, MoneyCommandExecutor executor, @Named("money.set") CommandNode setCommand, @Named("money.show") CommandNode showCommand) {
        CommandTree moneyCommandTree = new CommandTree(new CommandNode(builder.withName("money")
                .withArgument("player", playerArgument, false)
                .withExecutor(executor)
                .build()));

        moneyCommandTree.getRoot().addNode(setCommand);
        moneyCommandTree.getRoot().addNode(showCommand);

        return moneyCommandTree;
    }
}
