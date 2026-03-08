package com.zeralythstudios.zlpvptrigger.managers;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryProtectionManager {

    private final ZlPvPTrigger plugin;

    public InventoryProtectionManager(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    public boolean isProtectedTrigger(ItemStack itemStack) {
        return plugin.getTriggerItemManager().isTriggerItem(itemStack);
    }

    public void denyMove(Player player) {
        plugin.getMessageManager().send(player, "errors.cannot-move-trigger-item");
    }

    public void scanAndFix(Player player) {
        plugin.getTriggerItemManager().ensureTriggerItem(player);
    }

    public boolean containsProtectedItem(Inventory inventory) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (isProtectedTrigger(itemStack)) {
                return true;
            }
        }
        return false;
    }
}
