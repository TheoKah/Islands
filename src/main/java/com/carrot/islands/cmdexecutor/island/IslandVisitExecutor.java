package com.carrot.islands.cmdexecutor.island;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.IslandsPlugin;
import com.carrot.islands.Utils;
import com.carrot.islands.event.PlayerTeleportEvent;
import com.carrot.islands.object.Island;

public class IslandVisitExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			if (!ctx.<String>getOne("island").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/is visit <island> [name]"));
				return CommandResult.success();
			}
			String islandName = ctx.<String>getOne("island").get();
			Island island = DataHandler.getIsland(islandName);
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CI));
				return CommandResult.success();
			}
			int clicker = Utils.CLICKER_NONE;
			
			Island playerIsland = DataHandler.getIslandOfPlayer(player.getUniqueId());
			if (playerIsland != null && playerIsland.getUUID().equals(island.getUUID())) {
				clicker = Utils.CLICKER_DEFAULT;
			}
			
			if (player.hasPermission("islands.admin.bypass.visit"))
			{
				clicker = Utils.CLICKER_ADMIN;
			}
			
			if (clicker == Utils.CLICKER_NONE && !island.getFlag("public")) {
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.HT));
				return CommandResult.success();
			}
			
			if (!ctx.<String>getOne("name").isPresent())
			{
				src.sendMessage(Text.builder()
						.append(Text.of(TextColors.AQUA, LanguageHandler.GA.split("\\{SPAWNLIST\\}")[0]))
						.append(Utils.formatIslandSpawns(island, TextColors.YELLOW, clicker))
						.append(Text.of(TextColors.AQUA, LanguageHandler.GA.split("\\{SPAWNLIST\\}")[1]))
						.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX)).build());
				return CommandResult.success();
			}

			String spawnName = ctx.<String>getOne("name").get();
			Location<World> spawn = island.getSpawn(spawnName);
			if (spawn == null)
			{
				src.sendMessage(Text.builder()
						.append(Text.of(TextColors.RED, LanguageHandler.GB.split("\\{SPAWNLIST\\}")[0]))
						.append(Utils.formatIslandSpawns(island, TextColors.YELLOW, clicker))
						.append(Text.of(TextColors.RED, LanguageHandler.GB.split("\\{SPAWNLIST\\}")[1]))
						.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX)).build());
				return CommandResult.success();
			}
			
			if (player.hasPermission("islands.bypass.teleport.warmup")) {
				PlayerTeleportEvent event = new PlayerTeleportEvent(player, spawn, IslandsPlugin.getCause());
				Sponge.getEventManager().post(event);
				if (!event.isCancelled())
				{
					player.setLocation(spawn);
					src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.GC));
				}
				return CommandResult.success();
			}
			
			src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.HU));
			
			Scheduler scheduler = Sponge.getScheduler();
			Task.Builder taskBuilder = scheduler.createTaskBuilder();
			taskBuilder.execute(new Consumer<Task>() {
				
				@Override
				public void accept(Task t) {
					t.cancel();
					PlayerTeleportEvent event = new PlayerTeleportEvent(player, spawn, IslandsPlugin.getCause());
					Sponge.getEventManager().post(event);
					if (!event.isCancelled())
					{
						player.setLocation(spawn);
						src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.GC));
					}
				}
			}).delay(2, TimeUnit.SECONDS).submit(IslandsPlugin.getInstance());
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
