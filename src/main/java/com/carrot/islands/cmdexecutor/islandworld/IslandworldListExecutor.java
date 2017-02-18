package com.carrot.islands.cmdexecutor.islandworld;

import java.util.Iterator;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import com.carrot.islands.LanguageHandler;
import com.carrot.islands.Utils;

public class IslandworldListExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Builder builder = Text.builder();
		Iterator<World> iter = Sponge.getServer().getWorlds().iterator();
		builder.append(Text.of(TextColors.GOLD, "--------{ ", TextColors.YELLOW, LanguageHandler.JC, TextColors.GOLD, " }--------\n"));
		while (iter.hasNext())
		{
			World world = iter.next();
			builder.append(Utils.worldClickable(TextColors.YELLOW, world.getName()));
			if (iter.hasNext())
			{
				builder.append(Text.of(TextColors.YELLOW, ", "));
			}
		}
		if (src.hasPermission("islands.command.islandworld.info"))
		{
			builder.append(Text.of(TextColors.DARK_GRAY, " <- click"));
		}
		src.sendMessage(builder.build());
		return CommandResult.success();
	}
}
