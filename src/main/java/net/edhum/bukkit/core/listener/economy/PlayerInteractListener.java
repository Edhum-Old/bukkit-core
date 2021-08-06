package net.edhum.bukkit.core.listener.economy;

import com.google.inject.Inject;
import net.edhum.bukkit.api.gui.GUI;
import net.edhum.bukkit.api.gui.GUIBuilder;
import net.edhum.bukkit.api.gui.GUIService;
import net.edhum.bukkit.api.item.ItemBuilder;
import net.edhum.bukkit.api.player.Player;
import net.edhum.bukkit.api.player.repository.PlayerRepository;
import net.edhum.bukkit.api.player.repository.filter.PlayerFilterFactory;
import net.edhum.common.message.MessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerInteractListener implements Listener {

    private static final Material RESOURCE_MATERIAL = Material.GOLD_INGOT;
    private static final long RESOURCE_VALUE = 2;

    private final PlayerRepository playerRepository;
    private final PlayerFilterFactory playerFilterFactory;
    private final GUIService guiService;

    @Inject
    public PlayerInteractListener(PlayerRepository playerRepository, PlayerFilterFactory playerFilterFactory, GUIService guiService) {
        this.playerRepository = playerRepository;
        this.playerFilterFactory = playerFilterFactory;
        this.guiService = guiService;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block != null && block.getType() == Material.STONE && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            System.out.println("AH !");

            UUID uuid = event.getPlayer().getUniqueId();
            Player player = this.playerRepository.find(this.playerFilterFactory.uuid(uuid)).orElseThrow();

            GUI gui = this.buildEconomyGUI();

            this.guiService.open(gui, player);
        }
    }

    private GUI buildEconomyGUI() {
        GUI gui = new GUIBuilder()
                .withTitle(new MessageBuilder()
                        .withPath("inventory.economy.title")
                        .build())
                .withRows(5)
                .build();

        gui.add(new ItemBuilder()
                .withName(new MessageBuilder()
                        .withPath("item.sell")
                        .build())
                .withMaterial(Material.DIAMOND)
                .withAction(player -> {
                    org.bukkit.entity.Player bukkitPlayer = Optional.ofNullable(Bukkit.getPlayer(player.getUniqueId())).orElseThrow();
                    Inventory inventory = bukkitPlayer.getInventory();

                    int totalIngots = 0;

                    Map<Integer, ? extends ItemStack> items = inventory.all(RESOURCE_MATERIAL);
                    for (ItemStack value : items.values()) {
                        totalIngots += value.getAmount();
                    }

                    long amount = totalIngots * RESOURCE_VALUE;

                    player.creditMoney(amount);
                    player.sendMessage(String.format("Vous avez été crédité de %d€", amount));

                    inventory.remove(RESOURCE_MATERIAL);
                })
                .build());

        return gui;
    }
}
