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

public class IslandadminPermExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String>getOne("island").isPresent() || !ctx.<String>getOne("type").isPresent() || !ctx.<String>getOne("perm").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/na perm <island> <type> <perm> [true|false]"));
			return CommandResult.success();
		}
		String islandName = ctx.<String>getOne("island").get();
		Island island = DataHandler.getIsland(islandName);
		if (island == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CB));
			return CommandResult.success();
		}
		String type = ctx.<String>getOne("type").get();
		String perm = ctx.<String>getOne("perm").get();
		boolean bool = (ctx.<Boolean>getOne("bool").isPresent()) ? ctx.<Boolean>getOne("bool").get() : !island.getPerm(type, perm);
		island.setPerm(type, perm, bool);
		DataHandler.saveIsland(island.getUUID());
		src.sendMessage(Utils.formatIslandDescription(island, Utils.CLICKER_ADMIN));
		return CommandResult.success();
	}
}
