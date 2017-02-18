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
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;

public class IslandadminFlagExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String>getOne("island").isPresent() || !ctx.<String>getOne("flag").isPresent() || !ctx.<String>getOne("bool").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/ia flag <island> <flag> <true|false>"));
			return CommandResult.success();
		}
		String islandName = ctx.<String>getOne("island").get();
		Island island = DataHandler.getIsland(islandName);
		if (island == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CB));
			return CommandResult.success();
		}
		String flag = ctx.<String>getOne("flag").get();
		boolean bool = (ctx.<Boolean>getOne("bool").isPresent()) ? ctx.<Boolean>getOne("bool").get() : !island.getFlag(flag);
		island.setFlag(flag, bool);
		DataHandler.saveIsland(island.getUUID());
		src.sendMessage(Utils.formatIslandDescription(island, Utils.CLICKER_NONE));
		return CommandResult.success();
	}
}
