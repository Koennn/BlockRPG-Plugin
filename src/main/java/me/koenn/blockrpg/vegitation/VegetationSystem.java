package me.koenn.blockrpg.vegitation;

import me.koenn.blockrpg.util.PlayerHelper;
import org.bukkit.Bukkit;
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
public class VegetationSystem implements Listener, Runnable {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if (player.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.GRASS) || player.getLocation().add(0, -2, 0).getBlock().getType().equals(Material.GRASS)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
        } else if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            if (player.getActivePotionEffects().size() == 0) {
                return;
            }

            player.getActivePotionEffects().forEach(potionEffect -> {
                if (potionEffect.getType().equals(PotionEffectType.SPEED) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 5))) {
                    player.removePotionEffect(potionEffect.getType());
                }
            });
        }
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!PlayerHelper.isColor(player, "green")) {
                return;
            }

            if (VegetationChecker.nearVegetation(player.getLocation())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false), true);

                if (player.getActivePotionEffects().size() == 0) {
                    return;
                }

                player.getActivePotionEffects().forEach(potionEffect -> {
                    if (potionEffect.getType().equals(PotionEffectType.WEAKNESS) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 5))) {
                        player.removePotionEffect(potionEffect.getType());
                    }
                });
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false), true);

                if (player.getActivePotionEffects().size() == 0) {
                    return;
                }

                player.getActivePotionEffects().forEach(potionEffect -> {
                    if (potionEffect.getType().equals(PotionEffectType.REGENERATION) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 5))) {
                        player.removePotionEffect(potionEffect.getType());
                    }
                });
            }
        });
    }
}
