package com.carrot.islands.cmdexecutor.islandadmin;

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
import com.carrot.islands.object.Point;
import com.flowpowered.math.vector.Vector3i;

public class IslandadminTemplateExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			
			Point a = DataHandler.getFirstPoint(player.getUniqueId());
			Point b = DataHandler.getSecondPoint(player.getUniqueId());
			if (a == null || b == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EA));
				return CommandResult.success();
			}
			Point min = new Point(a.getWorld(), Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()));
			Point max = new Point(a.getWorld(), Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()));
			
			DataHandler.saveTemplate(player.getLocation(), new Vector3i(min.getX(), 0, min.getY()), new Vector3i(max.getX(), 255, max.getY()));
			
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.HV));

		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
