package net.edhum.bukkit.core.listener.gui;

import com.google.inject.Inject;
import net.edhum.bukkit.api.gui.GUI;
import net.edhum.bukkit.api.gui.repository.GUIRepository;
import net.edhum.bukkit.api.item.Item;
import net.edhum.bukkit.api.player.Player;
import net.edhum.bukkit.api.player.repository.PlayerRepository;
import net.edhum.bukkit.api.player.repository.filter.PlayerFilterFactory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;
import java.util.UUID;

public class InventoryClickListener implements Listener {

    private final PlayerRepository playerRepository;
    private final PlayerFilterFactory playerFilterFactory;
    private final GUIRepository guiRepository;

    @Inject
    public InventoryClickListener(PlayerRepository playerRepository, PlayerFilterFactory playerFilterFactory, GUIRepository guiRepository) {
        this.playerRepository = playerRepository;
        this.playerFilterFactory = playerFilterFactory;
        this.guiRepository = guiRepository;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        HumanEntity bukkitPlayer = event.getWhoClicked();

        if (inventory == bukkitPlayer.getOpenInventory().getTopInventory()) {
            UUID uuid = bukkitPlayer.getUniqueId();
            Player player = this.playerRepository.find(this.playerFilterFactory.uuid(uuid)).orElseThrow();

            Optional<GUI> optionalGui = this.guiRepository.get(player);
            if (optionalGui.isPresent()) {
                GUI gui = optionalGui.get();
                int slot = event.getSlot();

                Optional<Item> optionalItem = gui.get(slot);
                if (optionalItem.isPresent()) {
                    Item item = optionalItem.get();
                    item.click(player);
                }

                event.setCancelled(true);
            }
        }
    }
}
