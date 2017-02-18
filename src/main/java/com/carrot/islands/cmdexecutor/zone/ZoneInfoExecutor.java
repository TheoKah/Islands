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
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;
import com.carrot.islands.object.Zone;

public class ZoneInfoExecutor implements CommandExecutor
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
			Zone zone = null;
			if (!ctx.<String>getOne("zone").isPresent())
			{
				zone = island.getZone(player.getLocation());
				if (zone == null)
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GZ));
					return CommandResult.success();
				}
			}
			else
			{
				String zoneName = ctx.<String>getOne("zone").get();
				for (Zone z : island.getZones().values())
				{
					if (z.getName().equalsIgnoreCase(zoneName))
					{
						zone = z;
					}
				}
				if (zone == null)
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CF));
					return CommandResult.success();
				}
			}
			int clicker = Utils.CLICKER_DEFAULT;
			if (zone.isOwner(player.getUniqueId()) || zone.isCoowner(player.getUniqueId()) || island.isStaff(player.getUniqueId()))
			{
				clicker = Utils.CLICKER_DEFAULT;
			}
			src.sendMessage(Utils.formatZoneDescription(zone, island, clicker));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
