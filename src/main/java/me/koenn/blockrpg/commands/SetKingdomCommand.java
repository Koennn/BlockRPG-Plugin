package me.koenn.blockrpg.commands;

import me.koenn.core.command.Command;
import me.koenn.core.player.CPlayer;
import me.koenn.core.player.CPlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class SetKingdomCommand extends Command {

    public SetKingdomCommand() {
        super("setkingdom", "/setkingdom <color> [player]");
    }

    @Override
    public boolean execute(final CPlayer cPlayer, final String[] args) {
        final Player player = cPlayer.getPlayer();
        if (!player.isOp()) {
            return false;
        }

        if (args.length < 1) {
            return false;
        }

        CPlayer target = cPlayer;
        if (args.length > 1) {
            target = CPlayerRegistry.getCPlayer(Bukkit.getPlayer(args[1]).getUniqueId());
        }
        if (target == null) {
            return false;
        }

        target.set("color", args[0]);
        cPlayer.sendMessage("&aSet the kingdom of \'" + ChatColor.stripColor(target.getPlayer().getDisplayName()) + "\' to \'" + args[0] + "\'!");
        return true;
    }
}
