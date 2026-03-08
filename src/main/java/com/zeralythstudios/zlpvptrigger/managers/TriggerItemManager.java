package com.zeralythstudios.zlpvptrigger.managers;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import com.zeralythstudios.zlpvptrigger.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TriggerItemManager {

    private final ZlPvPTrigger plugin;
    private final NamespacedKey triggerKey;

    public TriggerItemManager(ZlPvPTrigger plugin) {
        this.plugin = plugin;
        this.triggerKey = new NamespacedKey(plugin, "zlpvptrigger_trigger");
    }

    public NamespacedKey getTriggerKey() {
        return triggerKey;
    }

    public ItemStack createTriggerItem() {
        Material material = plugin.getConfigManager().getTriggerMaterial();
        return ItemUtils.createTaggedItem(material, triggerKey);
    }

    public boolean isTriggerItem(ItemStack itemStack) {
        return ItemUtils.hasTag(itemStack, triggerKey);
    }

    public void ensureTriggerItem(Player player) {
        int slot = plugin.getConfigManager().getTriggerSlot();
        ItemStack inSlot = player.getInventory().getItem(slot);
        if (!isTriggerItem(inSlot)) {
            player.getInventory().setItem(slot, createTriggerItem());
        }
    }
}
