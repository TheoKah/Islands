package com.carrot.islands.cmdexecutor.zone;

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

public class ZoneDelownerExecutor implements CommandExecutor
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
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GX));
				return CommandResult.success();
			}
			if (!zone.isOwner(player.getUniqueId()) && !island.isStaff(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GV));
				return CommandResult.success();
			}
			zone.resetCoowners();
			zone.setOwner(null);
			DataHandler.saveIsland(island.getUUID());
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.GW.replaceAll("\\{ZONE\\}", zone.getName())));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
