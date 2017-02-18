package com.carrot.islands.cmdexecutor.islandadmin;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;

public class IslandadminForceleaveExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String>getOne("player").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/ia forceleave <player>"));
			return CommandResult.success();
		}
		String playerName = ctx.<String>getOne("player").get();
		
		UUID uuid = DataHandler.getPlayerUUID(playerName);
		if (uuid == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CC));
			return CommandResult.success();
		}
		Island island = DataHandler.getIslandOfPlayer(uuid);
		if (island == null)
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.HJ));
			return CommandResult.success();
		}
		if (island.isPresident(uuid))
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.HK));
			return CommandResult.success();
		}
		island.removeCitizen(uuid);
		DataHandler.saveIsland(island.getUUID());
		Sponge.getServer().getPlayer(uuid).ifPresent(p -> 
			p.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FM)));
		src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.HL));
		return CommandResult.success();
	}
}
