package com.carrot.islands.cmdexecutor.islandadmin;

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

public class IslandadminExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		List<Text> contents = new ArrayList<>();

		contents.add(Text.of(TextColors.GOLD, "/ia reload", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AZ));
		contents.add(Text.of(TextColors.GOLD, "/ia create <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BL));
		contents.add(Text.of(TextColors.GOLD, "/ia delete <island>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BM));
		contents.add(Text.of(TextColors.GOLD, "/ia setname <island> <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BN));
		contents.add(Text.of(TextColors.GOLD, "/ia settag <nation> <tag>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.LP));
		contents.add(Text.of(TextColors.GOLD, "/ia setpres <island> <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BO));
		contents.add(Text.of(TextColors.GOLD, "/ia forcejoin <island> <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BP));
		contents.add(Text.of(TextColors.GOLD, "/ia forceleave <island> <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BQ));
		contents.add(Text.of(TextColors.GOLD, "/ia template", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BR));
		contents.add(Text.of(TextColors.GOLD, "/ia firstkit", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BI));
		contents.add(Text.of(TextColors.GOLD, "/ia extraspawn <give|take|set> <nation> <amount>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.LG));
		contents.add(Text.of(TextColors.GOLD, "/ia extraspawnplayer <give|take|set> <nation> <amount>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.LH));
		contents.add(Text.of(TextColors.GOLD, "/ia perm <island> <type> <perm> [true|false]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BS));
		contents.add(Text.of(TextColors.GOLD, "/ia flag <island> <flag> [true|false]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BT));
		contents.add(Text.of(TextColors.GOLD, "/ia spy", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.DX));
		
		PaginationList.builder()
		.title(Text.of(TextColors.GOLD, "{ ", TextColors.YELLOW, "/islandadmin", TextColors.GOLD, " }"))
		.contents(contents)
		.padding(Text.of("-"))
		.sendTo(src);
		return CommandResult.success();
	}
}
