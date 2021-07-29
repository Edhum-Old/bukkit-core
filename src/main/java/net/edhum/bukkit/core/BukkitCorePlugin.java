package net.edhum.bukkit.core;

import com.google.inject.Stage;
import net.edhum.bukkit.api.plugin.EdhumPlugin;

public class BukkitCorePlugin extends EdhumPlugin {

    @Override
    public Stage getStage() {
        return Stage.PRODUCTION;
    }
}
