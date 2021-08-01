package net.edhum.bukkit.core;

import com.google.inject.AbstractModule;
import net.edhum.bukkit.core.listener.ListenerModule;

public class CoreModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ListenerModule());
    }
}
