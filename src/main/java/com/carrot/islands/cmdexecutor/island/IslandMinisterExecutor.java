package com.carrot.islands.cmdexecutor.island;

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

public class IslandMinisterExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			Island island = DataHandler.getIslandOfPlayer(player.getUniqueId());
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CI));
				return CommandResult.success();
			}
			if (!island.isPresident(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CJ));
				return CommandResult.success();
			}
			if (!ctx.<String>getOne("add|remove").isPresent() || !ctx.<String>getOne("citizen").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/n minister add <citizen>\n/n minister remove <citizen>"));
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
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FO));
				return CommandResult.success();
			}
			if (addOrRemove.equalsIgnoreCase("add"))
			{
				if (island.isMinister(uuid))
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FP.replaceAll("\\{PLAYER\\}", playerName)));
					return CommandResult.success();
				}
				island.addMinister(uuid);
				DataHandler.saveIsland(island.getUUID());
				src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FQ.replaceAll("\\{PLAYER\\}", playerName)));
				Sponge.getServer().getPlayer(uuid).ifPresent(
						p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FR.replaceAll("\\{PLAYER\\}", player.getName()))));
			}
			else if (addOrRemove.equalsIgnoreCase("remove"))
			{
				if (!island.isMinister(uuid))
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FS.replaceAll("\\{PLAYER\\}", playerName)));
					return CommandResult.success();
				}
				island.removeMinister(uuid);
				DataHandler.saveIsland(island.getUUID());
				src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FT.replaceAll("\\{PLAYER\\}", playerName)));
				Sponge.getServer().getPlayer(uuid).ifPresent(
						p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FU.replaceAll("\\{PLAYER\\}", player.getName()))));
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
