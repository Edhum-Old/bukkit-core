package net.edhum.bukkit.core.listener;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import net.edhum.bukkit.core.listener.command.PlayerCommandPreprocessListener;
import net.edhum.bukkit.core.listener.command.PlayerCommandSendListener;
import net.edhum.bukkit.core.listener.economy.PlayerInteractListener;
import net.edhum.bukkit.core.listener.gui.InventoryClickListener;
import net.edhum.bukkit.core.listener.stream.PlayerJoinListener;
import net.edhum.bukkit.core.listener.stream.PlayerPreLoginListener;
import net.edhum.bukkit.core.listener.stream.PlayerQuitListener;
import org.bukkit.event.Listener;

public class ListenerModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Listener> listenerBinder = Multibinder.newSetBinder(binder(), Listener.class);

        listenerBinder.addBinding().to(PlayerCommandPreprocessListener.class);
        listenerBinder.addBinding().to(PlayerCommandSendListener.class);

        listenerBinder.addBinding().to(PlayerInteractListener.class);

        listenerBinder.addBinding().to(InventoryClickListener.class);

        listenerBinder.addBinding().to(PlayerJoinListener.class);
        listenerBinder.addBinding().to(PlayerPreLoginListener.class);
        listenerBinder.addBinding().to(PlayerQuitListener.class);
    }
}
