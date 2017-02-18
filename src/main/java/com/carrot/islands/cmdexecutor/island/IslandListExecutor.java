package com.carrot.islands.cmdexecutor.island;

import java.util.Iterator;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;

public class IslandListExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Builder builder = Text.builder();
		Iterator<Island> iter = DataHandler.getIslands().values().iterator();
		if (!iter.hasNext())
		{
			builder.append(Text.of(TextColors.YELLOW, LanguageHandler.CO));
		}
		else
		{
			builder.append(Text.of(TextColors.GOLD, "--------{ ", TextColors.YELLOW, LanguageHandler.JB, TextColors.GOLD, " }--------\n"));
			while (iter.hasNext())
			{
				Island island = iter.next();
				if (!island.isAdmin() || src.hasPermission("islands.admin.island.listall"))
				{
					builder
					.append(Utils.islandClickable(TextColors.YELLOW, island.getName()))
					.append(Text.of(TextColors.GOLD, " [" + island.getNumCitizens() + "]"));
					if (iter.hasNext())
					{
						builder.append(Text.of(TextColors.YELLOW, ", "));
					}
				}
			}
		}
		src.sendMessage(builder.build());
		return CommandResult.success();
	}
}
