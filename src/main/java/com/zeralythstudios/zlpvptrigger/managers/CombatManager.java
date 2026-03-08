package com.zeralythstudios.zlpvptrigger.managers;

import com.zeralythstudios.zlpvptrigger.main.ZlPvPTrigger;
import org.bukkit.entity.Player;

public class CombatManager {

    private final ZlPvPTrigger plugin;

    public CombatManager(ZlPvPTrigger plugin) {
        this.plugin = plugin;
    }

    public boolean canFight(Player attacker, Player defender) {
        return plugin.getPvPManager().isPvPEnabled(attacker.getUniqueId())
                && plugin.getPvPManager().isPvPEnabled(defender.getUniqueId());
    }
}
