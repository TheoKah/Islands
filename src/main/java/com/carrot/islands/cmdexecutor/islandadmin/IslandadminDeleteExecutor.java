package com.carrot.islands.cmdexecutor.islandadmin;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;

public class IslandadminDeleteExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String>getOne("island").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/ia delete <island>"));
			return CommandResult.success();
		}
		String islandName = ctx.<String>getOne("island").get();
		Island island = DataHandler.getIsland(islandName);
		if (island == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CB));
			return CommandResult.success();
		}
		DataHandler.removeIsland(island.getUUID());
		MessageChannel.TO_ALL.send(Text.of(TextColors.AQUA, LanguageHandler.CN.replaceAll("\\{ISLAND\\}", island.getName())));
		return CommandResult.success();
	}
}
