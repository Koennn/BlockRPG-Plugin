package me.koenn.blockrpg.util;

import me.koenn.blockrpg.BlockRPG;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class OpLogger implements Listener {

    public static FileWriter writer;

    public OpLogger() {
        File file = new File(BlockRPG.getInstance().getDataFolder(), "cmdlog " + new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date()) + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        try {
            writer.append("[")
                    .append(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                    .append("] [")
                    .append(event.getPlayer().getName())
                    .append("] ")
                    .append(event.getMessage())
                    .append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        try {
            writer.append("[")
                    .append(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                    .append("] [")
                    .append(event.getSender().getName())
                    .append("] ")
                    .append(event.getCommand())
                    .append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onRemoteServerCommand(RemoteServerCommandEvent event) {
        try {
            writer.append("[")
                    .append(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                    .append("] [")
                    .append(event.getSender().getName())
                    .append("] ")
                    .append(event.getCommand())
                    .append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
