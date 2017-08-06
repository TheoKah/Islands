package com.carrot.islands.cmdexecutor.islandadmin;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;

public class IslandadminSettagExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String> getOne("Island").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/na settag <Island> [tag]"));
			return CommandResult.success();
		}
		String newTag = null;
		if (ctx.<String> getOne("tag").isPresent())
			newTag = ctx.<String> getOne("tag").get();
		Island island = DataHandler.getIsland(ctx.<String> getOne("Island").get());
		if (island == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CI));
			return CommandResult.success();
		}
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
		if (newTag != null && (newTag.length() < ConfigHandler.getNode("others", "minIslandTagLength").getInt()
				|| newTag.length() > ConfigHandler.getNode("others", "maxIslandTagLength").getInt()))
		{
			src.sendMessage(Text.of(TextColors.RED,
					LanguageHandler.LN
							.replaceAll("\\{MIN\\}",
									ConfigHandler.getNode("others", "minIslandTagLength").getString())
							.replaceAll("\\{MAX\\}",
									ConfigHandler.getNode("others", "maxIslandTagLength").getString())));
			return CommandResult.success();
		}
		String oldTag = island.getTag();
		island.setTag(newTag);
		DataHandler.saveIsland(island.getUUID());
		MessageChannel.TO_ALL.send(Text.of(TextColors.RED,
				LanguageHandler.LO.replaceAll("\\{NAME\\}", island.getName()).replaceAll("\\{OLDTAG\\}", oldTag).replaceAll("\\{NEWTAG\\}", island.getTag())));
		return CommandResult.success();
	}
}

