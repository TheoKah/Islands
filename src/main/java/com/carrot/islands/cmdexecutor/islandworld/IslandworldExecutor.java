package com.carrot.islands.cmdexecutor.islandworld;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.LanguageHandler;

public class IslandworldExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		src.sendMessage(Text.of(
				TextColors.GOLD, ((src instanceof Player) ? "" : "\n") + "--------{ ",
				TextColors.YELLOW, "/islandworld",
				TextColors.GOLD, " }--------",
				TextColors.GOLD, "\n/iw info [world]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BU,
				TextColors.GOLD, "\n/iw list", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BV,
				TextColors.GOLD, "\n/iw enable <world>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BW, 
				TextColors.GOLD, "\n/iw disable <world>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BX,
				TextColors.GOLD, "\n/iw perm <perm> [true|false]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BY,
				TextColors.GOLD, "\n/iw flag <flag> <true|false>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BZ
		));
		return CommandResult.success();
	}
}
