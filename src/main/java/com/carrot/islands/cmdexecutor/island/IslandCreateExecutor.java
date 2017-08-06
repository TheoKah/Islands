package com.carrot.islands.cmdexecutor.island;

import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;
import com.carrot.islands.object.Rect;

public class IslandCreateExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String>getOne("name").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/is create <name>"));
			return CommandResult.success();
		}
		if (src instanceof Player)
		{
			Player player = (Player) src;
			if (!ConfigHandler.getNode("worlds").getNode(player.getWorld().getName()).getNode("enabled").getBoolean())
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CS));
				return CommandResult.success();
			}
			if (DataHandler.getIslandOfPlayer(player.getUniqueId()) != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EK));
				return CommandResult.success();
			}
			String islandName = ctx.<String>getOne("name").get();
			if (DataHandler.getIsland(islandName) != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EL));
				return CommandResult.success();
			}
			if (!islandName.matches("[\\p{Alnum}\\p{IsIdeographic}\\p{IsLetter}\"_\"]*"))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EM));
				return CommandResult.success();
			}
			if (islandName.length() < ConfigHandler.getNode("others", "minIslandNameLength").getInt() || islandName.length() > ConfigHandler.getNode("others", "maxIslandNameLength").getInt())
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EN
						.replaceAll("\\{MIN\\}", ConfigHandler.getNode("others", "minIslandNameLength").getString())
						.replaceAll("\\{MAX\\}", ConfigHandler.getNode("others", "maxIslandNameLength").getString())));
				return CommandResult.success();
			}

			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.EB));

			Location<World> loc = DataHandler.getNextIslandLocation(player.getWorld());
			int radius = ConfigHandler.getNode("others", "islandRadius").getInt();
			Rect area = new Rect(loc.getExtent().getUniqueId(),
					loc.getBlockX() - radius,
					loc.getBlockX() + radius,
					loc.getBlockZ() - radius,
					loc.getBlockZ() + radius);
			Island island = new Island(UUID.randomUUID(), islandName, false, area);
			island.addSpawn("home", loc);
			island.addCitizen(player.getUniqueId());
			island.setPresident(player.getUniqueId());
			DataHandler.generateIslandTemplate(loc);
			DataHandler.addIsland(island);
			DataHandler.addToWorldChunks(island);
			player.setLocation(loc);
			DataHandler.resetPlayer(player);
			//DataHandler.giveStartKit(player.getInventory());
			MessageChannel.TO_ALL.send(Text.of(TextColors.AQUA, LanguageHandler.EP.replaceAll("\\{PLAYER\\}", player.getName()).replaceAll("\\{ISLAND\\}", islandName)));
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.EQ.replaceAll("\\{ISLAND\\}", island.getName())));

		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
