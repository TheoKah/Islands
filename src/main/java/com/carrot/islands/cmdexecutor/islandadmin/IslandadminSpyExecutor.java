package com.carrot.islands.cmdexecutor.islandadmin;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.channel.IslandMessageChannel;

public class IslandadminSpyExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			IslandMessageChannel channel = DataHandler.getSpyChannel();
			if (channel.getMembers().contains(src))
			{
				channel.removeMember(src);
				src.sendMessage(Text.of(TextColors.YELLOW, LanguageHandler.DW));
			}
			else
			{
				channel.addMember(src);
				src.sendMessage(Text.of(TextColors.YELLOW, LanguageHandler.DV));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
