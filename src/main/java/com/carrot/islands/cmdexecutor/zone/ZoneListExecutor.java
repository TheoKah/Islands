package com.carrot.islands.cmdexecutor.zone;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;

public class ZoneListExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Island island;
		if (ctx.<String>getOne("island").isPresent())
		{
			if (!src.hasPermission("islands.admin.zone.listall"))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CP));
				return CommandResult.success();
			}
			island = DataHandler.getIsland(ctx.<String>getOne("island").get());
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CB));
				return CommandResult.success();
			}
		}
		else
		{
			if (src instanceof Player)
			{
				Player player = (Player) src;
				island = DataHandler.getIsland(player.getLocation());
				if (island == null)
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CG));
					return CommandResult.success();
				}
			}
			else
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CG));
				return CommandResult.success();
			}
		}
		
		String str = LanguageHandler.HA.replaceAll("\\{ISLAND\\}", island.getName());
		String[] splited = str.split("\\{ZONELIST\\}");
		src.sendMessage(Utils.structureX(
				island.getZones().values().iterator(),
				Text.builder(splited[0]).color(TextColors.AQUA), 
				(b) -> b.append(Text.of(TextColors.GRAY, LanguageHandler.IP)),
				(b, zone) -> b.append(Text.builder(zone.getName()).color(TextColors.YELLOW).onClick(TextActions.runCommand("/z info " + zone.getName())).build()),
				(b) -> b.append(Text.of(TextColors.AQUA, ", "))).append(Text.of(TextColors.AQUA, (splited.length > 1) ? splited[1] : "")).build());
		
		return CommandResult.success();
	}
}
