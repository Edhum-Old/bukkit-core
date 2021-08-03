package net.edhum.bukkit.core.command;

import com.google.inject.AbstractModule;
import net.edhum.bukkit.core.command.money.MoneyCommandModule;

public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new MoneyCommandModule());
    }
}
