package me.koenn.blockrpg.waterlava;

import me.koenn.blockrpg.util.PlayerHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
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
public class WaterLavaSystem implements Listener, Runnable {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (!PlayerHelper.isColor(player, "red")) {
            return;
        }
        final double temperature = event.getPlayer().getLocation().getBlock().getTemperature();

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));

        if (player.getFireTicks() > 0) {
            player.setFireTicks(0);
        }

        if (temperature <= 0.0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, true, false));
        } else {
            player.getActivePotionEffects().forEach(potionEffect -> {
                if (potionEffect.getType().equals(PotionEffectType.SLOW) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 5))) {
                    player.removePotionEffect(potionEffect.getType());
                }
            });
        }

        if (player.getLocation().getBlock().getType().equals(Material.STATIONARY_LAVA)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        } else if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!PlayerHelper.isColor(player, "red")) {
                return;
            }

            if (player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) {
                player.damage(2.0);
            }
        });
    }
}
