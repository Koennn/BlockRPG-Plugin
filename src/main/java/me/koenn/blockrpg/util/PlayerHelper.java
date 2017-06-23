package me.koenn.blockrpg.util;

import me.koenn.core.player.CPlayer;
import me.koenn.core.player.CPlayerRegistry;
import org.bukkit.entity.Player;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class PlayerHelper {

    public static boolean isColor(Player player, String color) {
        final CPlayer cPlayer = CPlayerRegistry.getCPlayer(player.getUniqueId());
        return cPlayer.hasField("color") && cPlayer.get("color").equals(color);
    }
}
