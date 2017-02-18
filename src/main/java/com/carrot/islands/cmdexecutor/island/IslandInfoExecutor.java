package com.carrot.islands.cmdexecutor.island;

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
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;

public class IslandInfoExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		int clicker = Utils.CLICKER_NONE;
		Island island;
		if (ctx.<String>getOne("island").isPresent())
		{
			island = DataHandler.getIsland(ctx.<String>getOne("island").get());
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CB));
				return CommandResult.success();
			}
			if (src instanceof Player)
			{
				Player player = (Player) src;
				if (island.isStaff(player.getUniqueId()))
				{
					clicker = Utils.CLICKER_DEFAULT;
				}
			}
		}
		else
		{
			if (src instanceof Player)
			{
				Player player = (Player) src;
				island = DataHandler.getIslandOfPlayer(player.getUniqueId());
				if (island == null)
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CG));
					return CommandResult.success();
				}
				if (island.isStaff(player.getUniqueId()))
				{
					clicker = Utils.CLICKER_DEFAULT;
				}
			}
			else
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CG));
				return CommandResult.success();
			}
		}
		if (clicker == Utils.CLICKER_NONE && src.hasPermission("islands.command.islandadmin"))
		{
			clicker = Utils.CLICKER_ADMIN;
		}
		src.sendMessage(Utils.formatIslandDescription(island, clicker));
		return CommandResult.success();
	}
}
