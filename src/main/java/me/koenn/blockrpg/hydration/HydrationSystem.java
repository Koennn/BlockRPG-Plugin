package me.koenn.blockrpg.hydration;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.util.PlayerHelper;
import me.koenn.core.misc.ActionBar;
import me.koenn.core.misc.ProgressBar;
import me.koenn.core.player.CPlayer;
import me.koenn.core.player.CPlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class HydrationSystem implements Runnable, Listener {

    private static final ProgressBar progressBar = new ProgressBar(20, ChatColor.DARK_BLUE, ChatColor.GRAY, '\u25CF');
    private static final HashMap<UUID, Integer> cooldown = new HashMap<>();

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!PlayerHelper.isColor(player, "light-blue")) {
                return;
            }

            final CPlayer cPlayer = CPlayerRegistry.getCPlayer(player.getUniqueId());
            if (!cPlayer.contains("thirst")) {
                cPlayer.set("thirst", 100);
            }

            final int thirst = Integer.parseInt(cPlayer.get("thirst"));
            new ActionBar(progressBar.get(Math.round(thirst)), BlockRPG.getInstance()).send(player);

            if (player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) {
                cPlayer.set("thirst", Math.min(thirst + 1, 100));
            }

            if (thirst > 90) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, true, false), true);
            } else {
                player.getActivePotionEffects().forEach(potionEffect -> {
                    if (potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 10))) {
                        player.removePotionEffect(potionEffect.getType());
                    }
                });
            }

            if (thirst < 20) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, true, false), true);
            } else {
                player.getActivePotionEffects().forEach(potionEffect -> {
                    if (potionEffect.getType().equals(PotionEffectType.WEAKNESS) && potionEffect.getDuration() > (Integer.MAX_VALUE - (Integer.MAX_VALUE / 5))) {
                        player.removePotionEffect(potionEffect.getType());
                    }
                });
            }
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (!PlayerHelper.isColor(player, "light-blue")) {
            return;
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        final ItemStack hand = event.getItem();
        if (hand == null || !hand.getType().equals(Material.POTION)) {
            return;
        }

        final CPlayer cPlayer = CPlayerRegistry.getCPlayer(event.getPlayer().getUniqueId());

        Bukkit.getScheduler().scheduleSyncDelayedTask(BlockRPG.getInstance(), new CheckDrink(player, player.getInventory().getHeldItemSlot(), () -> {
            if (cPlayer.contains("thirst")) {
                int thirst = Integer.parseInt(cPlayer.get("thirst"));
                cPlayer.set("thirst", Math.min(thirst + 40, 100));
            }
        }), 33L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (!PlayerHelper.isColor(player, "light-blue")) {
            return;
        }

        final CPlayer cPlayer = CPlayerRegistry.getCPlayer(player.getUniqueId());
        if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
            if (!cooldown.containsKey(player.getUniqueId()) || cooldown.get(player.getUniqueId()) == 0) {
                if (cPlayer.contains("thirst")) {
                    int thirst = Integer.parseInt(cPlayer.get("thirst"));
                    cPlayer.set("thirst", Math.max(thirst - (player.isSprinting() ? 2 : 1), 0));
                }
                cooldown.put(player.getUniqueId(), 50);
            }
            if (cooldown.containsKey(player.getUniqueId())) {
                cooldown.put(player.getUniqueId(), cooldown.get(player.getUniqueId()) - 1);
            }
        }

        if (!player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, true, false));
        }

        if (player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
            player.setAllowFlight(true);
            player.setFlying(true);
            player.removePotionEffect(PotionEffectType.SLOW);
        } else if (player.hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
            player.removePotionEffect(PotionEffectType.WATER_BREATHING);
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }
}
