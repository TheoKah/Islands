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

public class IslandHomeExecutor implements CommandExecutor
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

			Location<World> spawn = island.getSpawn("home");
			if (spawn == null)
			{
				src.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.GF));
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
