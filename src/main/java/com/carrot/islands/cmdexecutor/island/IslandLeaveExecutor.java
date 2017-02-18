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
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;

public class IslandLeaveExecutor implements CommandExecutor
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
			if (island.isPresident(player.getUniqueId()))
			{
				if (island.getNumCitizens() > 1)
				{
					src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FL));
					return CommandResult.success();
				}
				island.removeCitizen(player.getUniqueId());
				src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.FM));
				DataHandler.removeIsland(island.getUUID());
				MessageChannel.TO_ALL.send(Text.of(TextColors.AQUA, LanguageHandler.CN.replaceAll("\\{ISLAND\\}", island.getName())));
				return CommandResult.success();
			}
			island.removeCitizen(player.getUniqueId());
			DataHandler.saveIsland(island.getUUID());
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.FM));
			for (UUID citizen : island.getCitizens())
			{
				Sponge.getServer().getPlayer(citizen).ifPresent(
						p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FN.replaceAll("\\{PLAYER\\}", player.getName()))));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
