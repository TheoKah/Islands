package com.carrot.islands.cmdexecutor.island;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;

public class IslandSetspawnExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			if (!ctx.<String>getOne("name").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/is setspawn <name>"));
				return CommandResult.success();
			}
			String spawnName = ctx.<String>getOne("name").get();
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
			Location<World> newSpawn = player.getLocation();
			if (!island.isInside(newSpawn))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FX));
				return CommandResult.success();
			}
			if (island.getNumSpawns() + 1 > ConfigHandler.getNode("others", "maxIslandSpawns").getInt())
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.HR
						.replaceAll("\\{MAX\\}", ConfigHandler.getNode("others", "maxIslandSpawns").getString())));
				return CommandResult.success();
			}
			if (!spawnName.matches("[\\p{Alnum}\\p{IsIdeographic}\\p{IsLetter}]{1,30}"))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FY
						.replaceAll("\\{MIN\\}", ConfigHandler.getNode("others", "minZoneNameLength").getString())
						.replaceAll("\\{MAX\\}", ConfigHandler.getNode("others", "maxZoneNameLength").getString())));
				return CommandResult.success();
			}
			island.addSpawn(spawnName, newSpawn);
			DataHandler.saveIsland(island.getUUID());
			src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.FZ));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
