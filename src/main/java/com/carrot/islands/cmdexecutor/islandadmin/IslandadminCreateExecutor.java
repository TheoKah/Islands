package com.carrot.islands.cmdexecutor.islandadmin;

import java.util.UUID;

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
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;
import com.carrot.islands.object.Rect;

public class IslandadminCreateExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (!ctx.<String>getOne("name").isPresent())
		{
			src.sendMessage(Text.of(TextColors.YELLOW, "/ia create <name>"));
			return CommandResult.success();
		}
		if (src instanceof Player)
		{
			Player player = (Player) src;
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
			
			Location<World> loc = DataHandler.getNextIslandLocation(player.getWorld());
			int radius = ConfigHandler.getNode("others", "islandRadius").getInt();
			Rect area = new Rect(loc.getExtent().getUniqueId(),
					loc.getBlockX() - radius,
					loc.getBlockX() + radius,
					loc.getBlockZ() - radius,
					loc.getBlockZ() + radius);
			Island island = new Island(UUID.randomUUID(), islandName, true, area);
			DataHandler.generateIslandTemplate(loc);
			DataHandler.addIsland(island);
			DataHandler.addToWorldChunks(island);
			Utils.safeTP(player, loc);
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.HL));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
