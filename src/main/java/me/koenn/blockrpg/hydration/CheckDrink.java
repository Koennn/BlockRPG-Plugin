package me.koenn.blockrpg.hydration;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * <player>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class CheckDrink implements Runnable {

    private final Player player;
    private final int slot;
    private final Runnable onDrink;

    public CheckDrink(Player player, int slot, Runnable onDrink) {
        this.player = player;
        this.slot = slot;
        this.onDrink = onDrink;
    }

    @Override
    public void run() {
        if (player.getInventory().getHeldItemSlot() == slot && player.getInventory().getItemInMainHand().getType().equals(Material.GLASS_BOTTLE)) {
            this.onDrink.run();
        }
    }
}
