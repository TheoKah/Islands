package com.carrot.islands.cmdexecutor.island;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.LanguageHandler;

public class IslandHelpExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		List<Text> contents = new ArrayList<>();

		contents.add(Text.of(TextColors.GOLD, "/is info [island]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AA));
		contents.add(Text.of(TextColors.GOLD, "/is here", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AB));
		contents.add(Text.of(TextColors.GOLD, "/is list", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AC));
		contents.add(Text.of(TextColors.GOLD, "/is create <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AD));
		contents.add(Text.of(TextColors.GOLD, "/is biome [biome]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AE));
		contents.add(Text.of(TextColors.GOLD, "/is invite <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AI));
		contents.add(Text.of(TextColors.GOLD, "/is join <island>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AJ));
		contents.add(Text.of(TextColors.GOLD, "/is kick <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AK));
		contents.add(Text.of(TextColors.GOLD, "/is leave", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AL));
		contents.add(Text.of(TextColors.GOLD, "/is resign <successor>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AM));
		contents.add(Text.of(TextColors.GOLD, "/is minister <add/remove> <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AN));
		contents.add(Text.of(TextColors.GOLD, "/is citizen <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AU));
		contents.add(Text.of(TextColors.GOLD, "/is chat", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AW));
		contents.add(Text.of(TextColors.GOLD, "/is perm <type> <perm> [true|false]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AO));
		contents.add(Text.of(TextColors.GOLD, "/is flag <flag> <true|false>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AP));
		contents.add(Text.of(TextColors.GOLD, "/is spawn [name]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AQ));
		contents.add(Text.of(TextColors.GOLD, "/is home", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AF));
		contents.add(Text.of(TextColors.GOLD, "/is setname <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BN));
		contents.add(Text.of(TextColors.GOLD, "/is settag [tag]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.LP));
		contents.add(Text.of(TextColors.GOLD, "/is visit <island> [name]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AX));
		contents.add(Text.of(TextColors.GOLD, "/is setspawn <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AR));
		contents.add(Text.of(TextColors.GOLD, "/is delspawn <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AS));
		
		PaginationList.builder()
		.title(Text.of(TextColors.GOLD, "{ ", TextColors.YELLOW, "/island", TextColors.GOLD, " }"))
		.contents(contents)
		.padding(Text.of("-"))
		.sendTo(src);
		return CommandResult.success();
	}
}
