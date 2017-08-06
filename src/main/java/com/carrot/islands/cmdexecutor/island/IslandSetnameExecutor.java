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

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;

public class IslandSetnameExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			if (!ctx.<String>getOne("name").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/is setname <name>"));
				return CommandResult.success();
			}
			Player player = (Player) src;
			Island island = DataHandler.getIslandOfPlayer(player.getUniqueId());
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CI));
				return CommandResult.success();
			}
			if (!island.isStaff(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CK));
				return CommandResult.success();
			}
			String newName = ctx.<String>getOne("name").get();
			if (DataHandler.getIsland(newName) != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EL));
				return CommandResult.success();
			}
			if (newName != null && DataHandler.getIslandByTag(newName) != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.LL));
				return CommandResult.success();
			}
			if (!newName.matches("[\\p{Alnum}\\p{IsIdeographic}\\p{IsLetter}\"_\"]*"))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EM));
				return CommandResult.success();
			}
			if (newName.length() < ConfigHandler.getNode("others", "minIslandNameLength").getInt() || newName.length() > ConfigHandler.getNode("others", "maxIslandNameLength").getInt())
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EN
						.replaceAll("\\{MIN\\}", ConfigHandler.getNode("others", "minIslandNameLength").getString())
						.replaceAll("\\{MAX\\}", ConfigHandler.getNode("others", "maxIslandNameLength").getString())));
				return CommandResult.success();
			}
			String oldName = island.getName();
			island.setName(newName);
			DataHandler.saveIsland(island.getUUID());
			MessageChannel.TO_ALL.send(Text.of(TextColors.AQUA, LanguageHandler.FW
					.replaceAll("\\{OLDNAME\\}", oldName)
					.replaceAll("\\{NEWNAME\\}", island.getName())));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
