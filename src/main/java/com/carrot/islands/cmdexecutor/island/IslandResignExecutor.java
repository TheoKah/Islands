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

public class IslandResignExecutor implements CommandExecutor
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
			if (!ctx.<String>getOne("successor").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/n resign <successor>"));
				return CommandResult.success();
			}
			String successorName = ctx.<String>getOne("successor").get();
			UUID successor = DataHandler.getPlayerUUID(successorName);
			if (successor == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CE));
				return CommandResult.success();
			}
			if (!island.isCitizen(successor))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FF));
				return CommandResult.success();
			}
			island.removeMinister(successor);
			island.setPresident(successor);
			DataHandler.saveIsland(island.getUUID());
			for (UUID citizen : island.getCitizens())
			{
				Sponge.getServer().getPlayer(citizen).ifPresent(
					p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FV.replaceAll("\\{SUCCESSOR\\}", successorName).replaceAll("\\{PLAYER\\}", player.getName()))));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
