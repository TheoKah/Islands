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

public class ZoneSetownerExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			if (!ctx.<String>getOne("owner").isPresent())
			{
				src.sendMessage(Text.of(TextColors.RED, "/z setowner <owner>"));
				return CommandResult.success();
			}
			String newOwnerName = ctx.<String>getOne("owner").get();
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
			UUID newOwner = DataHandler.getPlayerUUID(newOwnerName);
			if (newOwner == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CC));
				return CommandResult.success();
			}
			if (newOwner.equals(zone.getOwner()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.HB));
				return CommandResult.success();
			}
			if (!island.isCitizen(newOwner))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.HC));
				return CommandResult.success();
			}
			zone.setOwner(newOwner);
			DataHandler.saveIsland(island.getUUID());
			final String zoneName = zone.getName();
			if (newOwner.equals(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.GU.replaceAll("\\{ZONE\\}", zoneName)));
			}
			else
			{
				src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.HD.replaceAll("\\{PLAYER\\}", newOwnerName).replaceAll("\\{ZONE\\}", zoneName)));
				Sponge.getServer().getPlayer(newOwner).ifPresent(
						p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.HE.replaceAll("\\{PLAYER\\}", player.getName()).replaceAll("\\{ZONE\\}", zoneName))));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
