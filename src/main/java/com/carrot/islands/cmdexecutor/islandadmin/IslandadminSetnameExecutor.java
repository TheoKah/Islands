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

public class IslandadminSetnameExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String> getOne("oldname").isPresent() || !ctx.<String> getOne("newname").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/ia setname <oldname> <newname>"));
			return CommandResult.success();
		}
		String oldName = ctx.<String> getOne("oldname").get();
		String newName = ctx.<String> getOne("newname").get();
		Island island = DataHandler.getIsland(oldName);
		if (island == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CI));
			return CommandResult.success();
		}
		if (DataHandler.getIsland(newName) != null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EL));
			return CommandResult.success();
		}
		if (!newName.matches("[\\p{Alnum}\\p{IsIdeographic}\\p{IsLetter}]*"))
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EM));
			return CommandResult.success();
		}
		if (newName.length() < ConfigHandler.getNode("others", "minIslandNameLength").getInt()
				|| newName.length() > ConfigHandler.getNode("others", "maxIslandNameLength").getInt())
		{
			src.sendMessage(Text.of(TextColors.RED,
					LanguageHandler.EN
							.replaceAll("\\{MIN\\}",
									ConfigHandler.getNode("others", "minIslandNameLength").getString())
							.replaceAll("\\{MAX\\}",
									ConfigHandler.getNode("others", "maxIslandNameLength").getString())));
			return CommandResult.success();
		}
		island.setName(newName);
		DataHandler.saveIsland(island.getUUID());
		MessageChannel.TO_ALL.send(Text.of(TextColors.RED,
				LanguageHandler.FW.replaceAll("\\{OLDNAME\\}", oldName).replaceAll("\\{NEWNAME\\}", newName)));
		return CommandResult.success();
	}
}

