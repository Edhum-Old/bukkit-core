package net.edhum.bukkit.core.listener;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import net.edhum.bukkit.core.listener.command.PlayerCommandPreprocessListener;
import net.edhum.bukkit.core.listener.command.PlayerCommandSendListener;
import org.bukkit.event.Listener;

public class ListenerModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Listener> listenerBinder = Multibinder.newSetBinder(binder(), Listener.class);
        listenerBinder.addBinding().to(PlayerCommandPreprocessListener.class);
        listenerBinder.addBinding().to(PlayerCommandSendListener.class);
    }
}
