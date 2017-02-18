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

public class IslandadminSetpresExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String>getOne("island").isPresent() || !ctx.<String>getOne("president").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/ia setpres <island> <president>"));
			return CommandResult.success();
		}
		String islandName = ctx.<String>getOne("island").get();
		Island island = DataHandler.getIsland(islandName);
		if (island == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CB));
			return CommandResult.success();
		}
		String presidentName = ctx.<String>getOne("president").get();
		UUID presidentUUID = DataHandler.getPlayerUUID(presidentName);
		if (presidentUUID == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CC));
			return CommandResult.success();
		}
		if (island.isPresident(presidentUUID))
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CQ));
			return CommandResult.success();
		}
		if (!island.isCitizen(presidentUUID))
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CR));
			return CommandResult.success();
		}
		UUID oldPresidentUUID = island.getPresident();
		final String oldPresidentName = DataHandler.getPlayerName(oldPresidentUUID);
		island.setPresident(presidentUUID);
		DataHandler.saveIsland(island.getUUID());
		
		for (UUID citizen : island.getCitizens())
		{
			Sponge.getServer().getPlayer(citizen).ifPresent(
					p -> p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FV
							.replaceAll("\\{SUCCESSOR\\}", presidentName)
							.replaceAll("\\{PLAYER\\}", (oldPresidentName == null) ? LanguageHandler.IQ : oldPresidentName))));
		}
		
		return CommandResult.success();
	}
}
