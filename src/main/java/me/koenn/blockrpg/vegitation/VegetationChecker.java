package me.koenn.blockrpg.vegitation;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public final class VegetationChecker {

    private static final Material[] VEGETATION = new Material[]{
            Material.LEAVES, Material.LEAVES_2, Material.LONG_GRASS, Material.YELLOW_FLOWER, Material.RED_ROSE,
            Material.SAPLING, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.VINE, Material.WATER_LILY,
            Material.DOUBLE_PLANT, Material.CROPS, Material.SUGAR_CANE, Material.CARROT, Material.POTATO
    };

    public static boolean nearVegetation(Location location) {
        for (int x = -5; x < 5; x++) {
            for (int y = -3; y < 33; y++) {
                for (int z = -5; z < 5; z++) {
                    final Location current = location.clone().add(x, y, z);
                    final Material type = current.getBlock().getType();
                    for (final Material vegetation : VEGETATION) {
                        if (vegetation.equals(type)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
