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

public class IslandKickExecutor implements CommandExecutor
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
			if (!ctx.<String>getOne("player").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/is kick <player>"));
				return CommandResult.success();
			}
			String toKick = ctx.<String>getOne("player").get();
			UUID uuid = DataHandler.getPlayerUUID(toKick);
			if (uuid == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CC));
				return CommandResult.success();
			}
			if (!island.isCitizen(uuid))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FF));
				return CommandResult.success();
			}
			if (player.getUniqueId().equals(uuid))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FG));
				return CommandResult.success();
			}
			if (island.isPresident(uuid))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FH));
				return CommandResult.success();
			}
			if (island.isMinister(uuid) && island.isMinister(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FI));
				return CommandResult.success();
			}
			island.removeCitizen(uuid);
			DataHandler.saveIsland(island.getUUID());
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.FJ.replaceAll("\\{PLAYER\\}", toKick)));
			Sponge.getServer().getPlayer(uuid).ifPresent(
					p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FJ.replaceAll("\\{PLAYER\\}", player.getName()))));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
