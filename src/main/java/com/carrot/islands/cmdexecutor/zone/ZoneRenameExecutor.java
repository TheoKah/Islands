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

public class ZoneRenameExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			String zoneName = null;
			if (ctx.<String>getOne("name").isPresent())
			{
				zoneName = ctx.<String>getOne("name").get();
			}
			if (zoneName != null && !zoneName.matches("[\\p{Alnum}\\p{IsIdeographic}\\p{IsLetter}\"-\"]*{1,30}"))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FY
						.replaceAll("\\{MIN\\}", "1")
						.replaceAll("\\{MAX\\}", "30")));
				return CommandResult.success();
			}
			Player player = (Player) src;
			Island island = DataHandler.getIsland(player.getLocation());
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.DQ));
				return CommandResult.success();
			}
			Zone currentZone = island.getZone(player.getLocation());
			if (currentZone == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GX));
				return CommandResult.success();
			}
			if (!island.isStaff(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GV));
				return CommandResult.success();
			}
			if (zoneName != null)
			{
				for (Zone zone : island.getZones().values())
				{
					if (zone.isNamed() && zone.getRealName().equalsIgnoreCase(zoneName))
					{
						src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GR));
						return CommandResult.success();
					}
				}
			}
			currentZone.setName(zoneName);
			DataHandler.saveIsland(island.getUUID());
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.HS.replaceAll("\\{ZONE\\}", currentZone.getName())));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
