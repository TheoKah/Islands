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

public class IslandSettagExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
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
			String newTag = null;
			if (ctx.<String>getOne("tag").isPresent())
				newTag = ctx.<String>getOne("tag").get();
			if (newTag != null && DataHandler.getIsland(newTag) != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EL));
				return CommandResult.success();
			}
			if (newTag != null && DataHandler.getIslandByTag(newTag) != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.LL));
				return CommandResult.success();
			}
			if (newTag != null && !newTag.matches("[\\p{Alnum}\\p{IsIdeographic}\\p{IsLetter}\"_\"]*"))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.LM));
				return CommandResult.success();
			}
			if (newTag != null && (newTag.length() < ConfigHandler.getNode("others", "minIslandTagLength").getInt() || newTag.length() > ConfigHandler.getNode("others", "maxIslandTagLength").getInt()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.LN
						.replaceAll("\\{MIN\\}", ConfigHandler.getNode("others", "minIslandTagLength").getString())
						.replaceAll("\\{MAX\\}", ConfigHandler.getNode("others", "maxIslandTagLength").getString())));
				return CommandResult.success();
			}
			String oldName = island.getTag();
			island.setTag(newTag);
			DataHandler.saveIsland(island.getUUID());
			MessageChannel.TO_ALL.send(Text.of(TextColors.AQUA, LanguageHandler.LO
					.replaceAll("\\{NAME\\}", island.getName())
					.replaceAll("\\{OLDTAG\\}", oldName)
					.replaceAll("\\{NEWTAG\\}", island.getTag())));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
