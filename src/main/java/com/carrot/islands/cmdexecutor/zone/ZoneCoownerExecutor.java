package com.carrot.islands.cmdexecutor.zone;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;
import com.carrot.islands.object.Zone;

public class ZoneCoownerExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			Island island = DataHandler.getIsland(player.getLocation());
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.DQ));
				return CommandResult.success();
			}
			Zone zone = island.getZone(player.getLocation());
			if (zone == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GI));
				return CommandResult.success();
			}
			final String zoneName = zone.getName();
			if (!zone.isOwner(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GJ));
				return CommandResult.success();
			}
			if (!ctx.<String>getOne("add|remove").isPresent() || !ctx.<String>getOne("citizen").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/z coowner add <citizen>\n/z coowner remove <citizen>"));
				return CommandResult.success();
			}
			String addOrRemove = ctx.<String>getOne("add|remove").get();
			String playerName = ctx.<String>getOne("citizen").get();
			UUID uuid = DataHandler.getPlayerUUID(playerName);
			if (uuid == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CC));
				return CommandResult.success();
			}
			if (player.getUniqueId().equals(uuid))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GK));
				return CommandResult.success();
			}
			if (addOrRemove.equalsIgnoreCase("add"))
			{
				if (zone.isCoowner(uuid))
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GL.replaceAll("\\{PLAYER\\}", playerName)));
					return CommandResult.success();
				}
				zone.addCoowner(uuid);
				DataHandler.saveIsland(island.getUUID());
				src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.GM.replaceAll("\\{PLAYER\\}", playerName)));
				Sponge.getServer().getPlayer(uuid).ifPresent(
						p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.GN.replaceAll("\\{PLAYER\\}", player.getName()).replaceAll("\\{ZONE\\}", zoneName))));
			}
			else if (addOrRemove.equalsIgnoreCase("remove"))
			{
				if (!island.isMinister(uuid))
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GO.replaceAll("\\{PLAYER\\}", playerName)));
					return CommandResult.success();
				}
				zone.removeCoowner(uuid);
				DataHandler.saveIsland(island.getUUID());
				src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.GP.replaceAll("\\{PLAYER\\}", playerName)));
				Sponge.getServer().getPlayer(uuid).ifPresent(
						p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.GQ.replaceAll("\\{PLAYER\\}", player.getName()).replaceAll("\\{ZONE\\}", zoneName))));
			}
			else
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CD));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
