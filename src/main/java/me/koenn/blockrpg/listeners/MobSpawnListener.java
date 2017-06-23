package me.koenn.blockrpg.listeners;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Random;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class MobSpawnListener implements Listener {

    private static final Random random = new Random();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        final EntityType type = event.getEntityType();
        if (type.equals(EntityType.PIG_ZOMBIE) && random.nextInt(10) == 1) {
            final Location location = event.getLocation();
            event.setCancelled(true);
            location.getWorld().spawnEntity(location, EntityType.BLAZE);
            return;
        }

        if (type.equals(EntityType.PIG_ZOMBIE) && random.nextInt(20) == 1) {
            final Location location = event.getLocation();
            event.setCancelled(true);
            location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
            return;
        }

        if (type.equals(EntityType.ZOMBIE) && random.nextBoolean() && event.getLocation().getBlock().getTemperature() <= 0.0) {
            final Location location = event.getLocation();
            event.setCancelled(true);
            location.getWorld().spawnEntity(location, EntityType.SNOWMAN);
        }
    }
}
