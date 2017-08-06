package com.carrot.islands.cmdexecutor.zone;

import java.util.Map.Entry;
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
import com.carrot.islands.object.Point;
import com.carrot.islands.object.Rect;
import com.carrot.islands.object.Zone;

public class ZoneCreateExecutor implements CommandExecutor
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
			if (!island.isStaff(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CK));
				return CommandResult.success();
			}
			String zoneName = null;
			if (!ctx.<String>getOne("name").isPresent())
			{
				zoneName = ctx.<String>getOne("name").get();
			}
			if (zoneName != null && !zoneName.matches("[\\p{Alnum}\\p{IsIdeographic}\\p{IsLetter}\"_\"]*{1,30}"))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FY
						.replaceAll("\\{MIN\\}", "1")
						.replaceAll("\\{MAX\\}", "30")));
				return CommandResult.success();
			}
			UUID owner = null;
			if (ctx.<String>getOne("owner").isPresent())
			{
				owner = DataHandler.getPlayerUUID(ctx.<String>getOne("owner").get());
				if (owner == null)
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CC));
					return CommandResult.success();
				}
			}
			Point a = DataHandler.getFirstPoint(player.getUniqueId());
			Point b = DataHandler.getSecondPoint(player.getUniqueId());
			if (a == null || b == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EA));
				return CommandResult.success();
			}
			Rect rect = new Rect(a, b);
			if (!island.isInside(rect))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.HG));
				return CommandResult.success();
			}
			for (Zone zone : island.getZones().values())
			{
				if (zoneName != null && zone.isNamed() && zone.getName().equalsIgnoreCase(zoneName))
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GR));
					return CommandResult.success();
				}
				if (rect.intersects(zone.getRect()))
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.GS));
					return CommandResult.success();
				}
			}
			Zone zone = new Zone(UUID.randomUUID(), zoneName, rect, owner);
			for (Entry<String, Boolean> e : island.getFlags().entrySet())
			{
				zone.setFlag(e.getKey(), e.getValue());
			}
			island.addZone(zone);
			DataHandler.saveIsland(island.getUUID());
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.GT.replaceAll("\\{ZONE\\}", zone.getName())));
			Sponge.getServer().getPlayer(owner).ifPresent(
					p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.GU.replaceAll("\\{ZONE\\}", zone.getName()))));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
