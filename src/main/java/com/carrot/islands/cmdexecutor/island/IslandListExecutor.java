package com.carrot.islands.cmdexecutor.island;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;

public class IslandListExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		List<Text> contents = new ArrayList<>();
		Iterator<Island> iter = DataHandler.getIslands().values().iterator();
		if (!iter.hasNext())
		{
			contents.add(Text.of(TextColors.YELLOW, LanguageHandler.CO));
		}
		else
		{
			while (iter.hasNext())
			{
				Island nation = iter.next();
				if (!nation.isAdmin() || src.hasPermission("islands.admin.nation.listall"))
				{
					contents.add(Text.of(Utils.islandClickable(TextColors.YELLOW, nation.getRealName()), TextColors.GOLD, " [" + nation.getNumCitizens() + "]"));
				}
			}
		}
		PaginationList.builder()
		.title(Text.of(TextColors.GOLD, "{ ", TextColors.YELLOW, LanguageHandler.JB, TextColors.GOLD, " }"))
		.contents(contents)
		.padding(Text.of("-"))
		.sendTo(src);
		return CommandResult.success();
	}
}
