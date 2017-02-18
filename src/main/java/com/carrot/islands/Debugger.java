package com.carrot.islands;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.ChangeBlockEvent.Break;
import org.spongepowered.api.event.block.ChangeBlockEvent.Place;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.event.world.ExplosionEvent.Pre;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;


public class Debugger {
	static PrintWriter file = null;
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd.hh:mm:ss");

	static public void start(CommandSource src)
	{
		if (file != null) {
			src.sendMessage(Text.of(TextColors.RED, "DEBUG IS ALREADY RUNNING!"));
			return ;
		}
		String filename = dateFormat.format(new Date());

		try {
			file = new PrintWriter("/var/www/html/cache/sf3debug/" + filename + ".csv", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			src.sendMessage(Text.of(TextColors.RED, "ERROR! see console for more details"));
			e.printStackTrace();
			file = null;
			return ;
		}
		src.sendMessage(Text.of(TextColors.GREEN, "Starting to log Islands perm listeners"));
	}

	static public void stop(CommandSource src)
	{
		if (file == null) {
			src.sendMessage(Text.of(TextColors.RED, "DEBUG IS NOT RUNNING!"));
			return ;
		}
		file.close();
		file = null;
		src.sendMessage(Text.of(TextColors.GREEN, "Stopping log of Islands perm listeners"));
	}

	static private void logCommon(List<String> logs, String type, Event event, Player player)
	{
		logs.add(String.valueOf(Sponge.getServer().getRunningTimeTicks()));
		logs.add(type);
		if (player != null)
		{
			logs.add(player.getUniqueId().toString());
			logs.add(player.getName());
			Location<World> playerPos = player.getLocation();
			logs.add(playerPos.getExtent().getName());
			logs.add(String.valueOf(playerPos.getBlockX()));
			logs.add(String.valueOf(playerPos.getBlockY()));
			logs.add(String.valueOf(playerPos.getBlockZ()));
		}
		logs.add(event.getCause().toString());
	}

	static public void log(String type, InteractBlockEvent event, Player player)
	{
		if (file == null)
			return;
		List<String> logs = new ArrayList<>();
		logCommon(logs, type, event, player);
		logs.add(event.getTargetBlock().toString());
		Optional<Location<World>> oloc = event.getTargetBlock().getLocation();
		if (oloc.isPresent()) {
			Location<World> loc = oloc.get();
			logs.add(loc.getExtent().getName());
			logs.add(String.valueOf(loc.getBlockX()));
			logs.add(String.valueOf(loc.getBlockY()));
			logs.add(String.valueOf(loc.getBlockZ()));
		}
		file.println(String.join(";", logs));
	}

	public static void log(String type, ChangeBlockEvent event, Player player) {
		if (file == null)
			return;
		List<String> logs = new ArrayList<>();
		logCommon(logs, type, event, player);
		event
		.getTransactions()
		.stream()
		.forEach(trans -> trans.getOriginal().getLocation().ifPresent(loc -> {
			logs.add(trans.getOriginal().toString());
			logs.add(trans.getFinal().toString());
			logs.add(loc.getExtent().getName());
			logs.add(String.valueOf(loc.getBlockX()));
			logs.add(String.valueOf(loc.getBlockY()));
			logs.add(String.valueOf(loc.getBlockZ()));
		}));
		file.println(String.join(";", logs));
	}

	public static void log(String type, ExplosionEvent event) {
		if (file == null)
			return;
		List<String> logs = new ArrayList<>();
		logCommon(logs, type, event, null);
		
		Location<World> loc = event.getExplosion().getLocation();
		logs.add(loc.getExtent().getName());
		logs.add(String.valueOf(loc.getBlockX()));
		logs.add(String.valueOf(loc.getBlockY()));
		logs.add(String.valueOf(loc.getBlockZ()));
		file.println(String.join(";", logs));
	}

	static public class CmdExecutor implements CommandExecutor
	{
		@Override
		public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
		{
			if (!ctx.<String>getOne("start|stop").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/iw debug <start|stop>"));
				return CommandResult.success();
			}
			String startOrStop = ctx.<String>getOne("start|stop").get();
			if (startOrStop.equals("start"))
				start(src);
			else
				stop(src);
			return CommandResult.success();
		}
	}


}
