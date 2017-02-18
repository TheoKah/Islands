package com.carrot.islands.cmdexecutor.island;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.LanguageHandler;

public class IslandExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		src.sendMessage(Text.of(
				TextColors.GOLD, ((src instanceof Player) ? "" : "\n") + "--------{ ",
				TextColors.YELLOW, "/island",
				TextColors.GOLD, " }--------",
				TextColors.GOLD, "\n/is info [island]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AA,
				TextColors.GOLD, "\n/is here", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AB,
				TextColors.GOLD, "\n/is list", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AC,
				TextColors.GOLD, "\n/is create <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AD,
				TextColors.GOLD, "\n/is biome [biome]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AE,
				TextColors.GOLD, "\n/is invite <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AI,
				TextColors.GOLD, "\n/is join <island>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AJ,
				TextColors.GOLD, "\n/is kick <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AK,
				TextColors.GOLD, "\n/is leave", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AL,
				TextColors.GOLD, "\n/is resign <successor>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AM,
				TextColors.GOLD, "\n/is minister <add/remove> <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AN,
				TextColors.GOLD, "\n/is citizen <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AU,
				TextColors.GOLD, "\n/is chat", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AW,
				TextColors.GOLD, "\n/is perm <type> <perm> [true|false]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AO,
				TextColors.GOLD, "\n/is flag <flag> <true|false>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AP,
				TextColors.GOLD, "\n/is spawn [name]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AQ,
				TextColors.GOLD, "\n/is home", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AF,
				TextColors.GOLD, "\n/is visit <island> [name]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AX,
				TextColors.GOLD, "\n/is setspawn <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AR,
				TextColors.GOLD, "\n/is delspawn <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AS));
		return CommandResult.success();
	}
}
