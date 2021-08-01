package net.edhum.bukkit.core;

import com.google.inject.Module;
import com.google.inject.Stage;
import net.edhum.bukkit.api.plugin.EdhumPlugin;

import java.util.Collection;
import java.util.Collections;

public class BukkitCorePlugin extends EdhumPlugin {

    @Override
    public Collection<Module> getModules() {
        return Collections.singleton(new CoreModule());
    }

    @Override
    public Stage getStage() {
        return Stage.PRODUCTION;
    }
}
