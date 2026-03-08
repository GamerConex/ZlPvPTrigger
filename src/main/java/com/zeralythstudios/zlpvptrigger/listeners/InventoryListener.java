package com.zeralythstudios.zlpvptrigger.listeners;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class InventoryListener implements Listener {

    private final ZlPvPTrigger plugin;

    public InventoryListener(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        int triggerSlot = plugin.getConfigManager().getTriggerSlot();
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        boolean clickedOwnTriggerSlot = event.getClickedInventory() != null
                && event.getClickedInventory().equals(player.getInventory())
                && event.getSlot() == triggerSlot;

        boolean shiftMovingTrigger = event.isShiftClick()
                && plugin.getInventoryProtectionManager().isProtectedTrigger(current);

        boolean numberKeyToTriggerSlot = event.getClick() == ClickType.NUMBER_KEY
                && event.getHotbarButton() == triggerSlot;

        boolean doubleClickWithTrigger = event.getClick() == ClickType.DOUBLE_CLICK
                && (plugin.getInventoryProtectionManager().isProtectedTrigger(cursor)
                || plugin.getInventoryProtectionManager().isProtectedTrigger(current));

        boolean touchesTrigger = plugin.getInventoryProtectionManager().isProtectedTrigger(current)
                || plugin.getInventoryProtectionManager().isProtectedTrigger(cursor)
                || clickedOwnTriggerSlot
                || numberKeyToTriggerSlot
                || doubleClickWithTrigger
                || shiftMovingTrigger;

        if (!touchesTrigger) {
            return;
        }

        event.setCancelled(true);
        plugin.getInventoryProtectionManager().denyMove(player);
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getInventoryProtectionManager().scanAndFix(player);
            }
        });
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        int triggerSlot = plugin.getConfigManager().getTriggerSlot();
        int topSize = event.getView().getTopInventory().getSize();
        Set<Integer> rawSlots = event.getRawSlots();

        boolean draggingIntoTriggerSlot = false;
        for (Integer rawSlot : rawSlots) {
            if (rawSlot.intValue() >= topSize && event.getView().convertSlot(rawSlot.intValue()) == triggerSlot) {
                draggingIntoTriggerSlot = true;
                break;
            }
        }

        if (draggingIntoTriggerSlot || plugin.getInventoryProtectionManager().isProtectedTrigger(event.getOldCursor())) {
            event.setCancelled(true);
            plugin.getInventoryProtectionManager().denyMove(player);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (plugin.getInventoryProtectionManager().isProtectedTrigger(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            plugin.getInventoryProtectionManager().denyMove(event.getPlayer());
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        if (plugin.getInventoryProtectionManager().isProtectedTrigger(event.getMainHandItem())
                || plugin.getInventoryProtectionManager().isProtectedTrigger(event.getOffHandItem())) {
            event.setCancelled(true);
            plugin.getInventoryProtectionManager().denyMove(event.getPlayer());
        }
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent event) {
        Inventory destination = event.getDestination();
        if (plugin.getInventoryProtectionManager().isProtectedTrigger(event.getItem())
                || plugin.getInventoryProtectionManager().containsProtectedItem(destination)) {
            event.setCancelled(true);
        }
    }
}
