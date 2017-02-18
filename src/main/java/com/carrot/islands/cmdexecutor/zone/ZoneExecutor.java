package com.carrot.islands.cmdexecutor.zone;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.LanguageHandler;

public class ZoneExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		src.sendMessage(Text.of(
				TextColors.GOLD, ((src instanceof Player) ? "" : "\n") + "--------{ ",
				TextColors.YELLOW, "/zone",
				TextColors.GOLD, " }--------",
				TextColors.GOLD, "\n/z info [zone]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BA,
				TextColors.GOLD, "\n/z list", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BB,
				TextColors.GOLD, "\n/z create <name> [owner]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BC,
				TextColors.GOLD, "\n/z delete [zone]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BK,
				TextColors.GOLD, "\n/z coowner <add/remove> <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BD,
				TextColors.GOLD, "\n/z setowner <player>", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BE,
				TextColors.GOLD, "\n/z delowner", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BF,
				TextColors.GOLD, "\n/z rename", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.DS,
				TextColors.GOLD, "\n/z perm <type> <perm> [true/false]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BG,
				TextColors.GOLD, "\n/z flag <flag> [true/false]", TextColors.GRAY, " - ", TextColors.YELLOW, LanguageHandler.BH));
		return CommandResult.success();
	}
}
