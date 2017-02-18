package com.carrot.islands.cmdexecutor.island;

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
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;

public class IslandDelspawnExecutor implements CommandExecutor
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
			if (!ctx.<String>getOne("name").isPresent())
			{
				src.sendMessage(Text.builder()
						.append(Text.of(TextColors.AQUA, LanguageHandler.ER.split("\\{SPAWNLIST\\}")[0]))
						.append(Utils.formatIslandSpawns(island, TextColors.YELLOW, "delhome"))
						.append(Text.of(TextColors.AQUA, LanguageHandler.ER.split("\\{SPAWNLIST\\}")[1])).build());
				return CommandResult.success();
			}
			String spawnName = ctx.<String>getOne("name").get();
			if (!island.isStaff(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CK));
				return CommandResult.success();
			}
			if (island.getSpawn(spawnName) == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.ES));
				return CommandResult.success();
			}
			island.removeSpawn(spawnName);
			DataHandler.saveIsland(island.getUUID());
			src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.ET));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
