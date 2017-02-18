package com.carrot.islands.cmdexecutor.islandadmin;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;

public class IslandadminForcejoinExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String>getOne("island").isPresent() || !ctx.<String>getOne("player").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/ia forcejoin <island> <player>"));
			return CommandResult.success();
		}
		String islandName = ctx.<String>getOne("island").get();
		String playerName = ctx.<String>getOne("player").get();
		
		Island island = DataHandler.getIsland(islandName);
		if (island == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CB));
			return CommandResult.success();
		}
		UUID playerUUID = DataHandler.getPlayerUUID(playerName);
		if (playerUUID == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CC));
			return CommandResult.success();
		}
		
		Island playerIsland = DataHandler.getIslandOfPlayer(playerUUID);
		if (playerIsland != null)
		{
			playerIsland.removeCitizen(playerUUID);
			for (UUID uuid : playerIsland.getCitizens())
			{
				Sponge.getServer().getPlayer(uuid).ifPresent(p -> 
					p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FN.replaceAll("\\{PLAYER\\}", playerName))));
			}
		}
		
		for (UUID uuid : island.getCitizens())
		{
			Sponge.getServer().getPlayer(uuid).ifPresent(p -> 
				p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.EY.replaceAll("\\{PLAYER\\}", playerName))));
		}
		island.addCitizen(playerUUID);
		DataHandler.saveIsland(island.getUUID());
		Sponge.getServer().getPlayer(playerUUID).ifPresent(p -> 
			p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.EZ.replaceAll("\\{ISLAND\\}", islandName))));
		src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.HL));
		return CommandResult.success();
	}
}
