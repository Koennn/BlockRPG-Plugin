package me.koenn.blockrpg.daynight;

import me.koenn.blockrpg.util.PlayerHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class DayNightSystem implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (!PlayerHelper.isColor(player, "purple")) {
            return;
        }

        final long time = player.getWorld().getTime();
        if (time > 13000 || time < 0) {
            this.nightBuff(player, time);
        }
        this.dayDebuff(player, time);
    }

    private void nightBuff(Player player, long time) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, time > 14000 ? 1 : 0, false, false), true);
        if (time > 15000 && time < 21000) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false), true);
        }
        player.getActivePotionEffects().forEach(potionEffect -> {
            if (potionEffect.getType().equals(PotionEffectType.WEAKNESS) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 5))) {
                player.removePotionEffect(potionEffect.getType());
            }
        });
    }

    private void dayDebuff(Player player, long time) {
        if (time > 0 && time < 13000) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false), true);
        }

        player.getActivePotionEffects().forEach(potionEffect -> {
            if (potionEffect.getDuration() < (Integer.MAX_VALUE - (Integer.MAX_VALUE / 10))) {
                return;
            }

            if (potionEffect.getType().equals(PotionEffectType.SPEED)) {
                if (potionEffect.getAmplifier() == 1 && (time > 21000 || time < 15000)) {
                    player.removePotionEffect(potionEffect.getType());
                }
                if (potionEffect.getAmplifier() == 0 && (time > 0 && time < 13000)) {
                    player.removePotionEffect(potionEffect.getType());
                }
            }

            if (potionEffect.getType().equals(PotionEffectType.INVISIBILITY)) {
                if (time > 21000 || time < 15000) {
                    player.removePotionEffect(potionEffect.getType());
                }
            }
        });
    }
}
