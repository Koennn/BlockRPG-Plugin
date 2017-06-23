package me.koenn.blockrpg.enviromental;

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
public class EnvironmentalSystem implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final double temperature = event.getPlayer().getLocation().getBlock().getTemperature();
        final Player player = event.getPlayer();
        if (!PlayerHelper.isColor(player, "yellow") && !PlayerHelper.isColor(player, "orange")) {
            return;
        }

        if (temperature < 0.6) {
            this.addYellowBuffs(player, temperature);
            this.removeOrangeBuffs(player, temperature);
        } else if (temperature > 0.8) {
            this.addOrangeBuffs(player, temperature);
            this.removeYellowBuffs(player, temperature);
        } else {
            this.removeYellowBuffs(player, temperature);
            this.removeOrangeBuffs(player, temperature);
        }
    }

    private void addYellowBuffs(Player player, double temperature) {
        if (!PlayerHelper.isColor(player, "yellow")) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), true);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        if (temperature <= 0.0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), true);
        } else {
            player.getActivePotionEffects().forEach(potionEffect -> {
                if (potionEffect.getType().equals(PotionEffectType.SPEED) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 10))) {
                    player.removePotionEffect(potionEffect.getType());
                }
            });
        }
    }

    private void addOrangeBuffs(Player player, double temperature) {
        if (!PlayerHelper.isColor(player, "orange")) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), true);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        if (temperature >= 1.8) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), true);
        } else {
            player.getActivePotionEffects().forEach(potionEffect -> {
                if (potionEffect.getType().equals(PotionEffectType.SPEED) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 5))) {
                    player.removePotionEffect(potionEffect.getType());
                }
            });
        }
    }

    private void removeYellowBuffs(Player player, double temperature) {
        if (!PlayerHelper.isColor(player, "yellow")) {
            return;
        }

        player.getActivePotionEffects().forEach(potionEffect -> {
            if ((potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE) || potionEffect.getType().equals(PotionEffectType.SPEED)) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 10))) {
                player.removePotionEffect(potionEffect.getType());
            }
        });

        if (temperature > 0.8) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        }
    }

    private void removeOrangeBuffs(Player player, double temperature) {
        if (!PlayerHelper.isColor(player, "orange")) {
            return;
        }

        player.getActivePotionEffects().forEach(potionEffect -> {
            if ((potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE) || potionEffect.getType().equals(PotionEffectType.SPEED)) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 10))) {
                player.removePotionEffect(potionEffect.getType());
            }
        });

        if (temperature < 0.6) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        }
    }
}
