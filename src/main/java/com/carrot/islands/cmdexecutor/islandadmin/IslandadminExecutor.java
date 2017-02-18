package com.carrot.islands.cmdexecutor.islandadmin;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.LanguageHandler;

public class IslandadminExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		src.sendMessage(Text.of(
				TextColors.GOLD, ((src instanceof Player) ? "" : "\n") + "--------{ ",
				TextColors.YELLOW, "/islandadmin",
				TextColors.GOLD, " }--------",
				TextColors.GOLD, "\n/ia reload", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.AZ,
				TextColors.GOLD, "\n/ia create <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BL,
				TextColors.GOLD, "\n/ia delete <island>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BM,
				TextColors.GOLD, "\n/ia setname <island> <name>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BN,
				TextColors.GOLD, "\n/ia setpres <island> <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BO,
				TextColors.GOLD, "\n/ia forcejoin <island> <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BP,
				TextColors.GOLD, "\n/ia forceleave <island> <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BQ,
				TextColors.GOLD, "\n/ia template", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BR,
				TextColors.GOLD, "\n/ia firstkit", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BI,
				TextColors.GOLD, "\n/ia perm <island> <type> <perm> <true|false>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BS,
				TextColors.GOLD, "\n/ia flag <island> <flag> <true|false>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BT
		));
		return CommandResult.success();
	}
}
