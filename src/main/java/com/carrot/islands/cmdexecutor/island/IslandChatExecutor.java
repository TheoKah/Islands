package com.carrot.islands.cmdexecutor.island;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.channel.IslandMessageChannel;
import com.carrot.islands.object.Island;

public class IslandChatExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			Island island = DataHandler.getIslandOfPlayer(player.getUniqueId());
			if (island == null)
			{
				player.setMessageChannel(MessageChannel.TO_ALL);
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CI));
				return CommandResult.success();
			}
			IslandMessageChannel channel = island.getMessageChannel();
			
			if (player.getMessageChannel().equals(channel)) {
				player.setMessageChannel(MessageChannel.TO_ALL);
				src.sendMessage(Text.of(TextColors.YELLOW, LanguageHandler.DU));
			} else {
				player.setMessageChannel(channel);
				src.sendMessage(Text.of(TextColors.YELLOW, LanguageHandler.DT));
			}
			
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
