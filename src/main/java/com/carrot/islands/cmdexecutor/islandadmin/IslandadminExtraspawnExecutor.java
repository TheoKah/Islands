package com.carrot.islands.cmdexecutor.islandadmin;

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

public class IslandadminExtraspawnExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String>getOne("island").isPresent() || !ctx.<String>getOne("give|take|set").isPresent() || !ctx.<String>getOne("amount").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/ia extraspawn <give|take|set> <island> <amount>"));
			return CommandResult.success();
		}
		String islandName = ctx.<String>getOne("nation").get();
		Integer amount = Integer.valueOf(ctx.<Integer>getOne("amount").get());
		String operation = ctx.<String>getOne("give|take|set").get();
		
		Island island = DataHandler.getIsland(islandName);
		if (island == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CB));
			return CommandResult.success();
		}
		if (operation.equalsIgnoreCase("give"))
		{
			island.addExtraSpawns(amount);
		}
		else if (operation.equalsIgnoreCase("take"))
		{
			island.addExtraSpawns(amount);
		}
		else if (operation.equalsIgnoreCase("set"))
		{
			island.addExtraSpawns(amount);
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CX));
			return CommandResult.success();
		}
		DataHandler.saveIsland(island.getUUID());
		src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.HL));
		return CommandResult.success();
	}
}
