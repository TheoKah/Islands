package com.carrot.islands.cmdelement;

import java.util.Collections;
import java.util.stream.Collectors;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.carrot.islands.DataHandler;
import com.carrot.islands.object.Island;

public class ZoneNameElement extends PatternMatchingCommandElement
{
	public ZoneNameElement(Text key)
	{
		super(key);
	}
	
	@Override
	protected Iterable<String> getChoices(CommandSource src)
	{
		if (!(src instanceof Player))
		{
			return Collections.emptyList();
		}
		Player player = (Player) src;
		Island island = DataHandler.getIsland(player.getLocation());
		if (island == null)
		{
			island = DataHandler.getIslandOfPlayer(player.getUniqueId());
			if (island == null)
				return Collections.emptyList();
		}
		return island.getZones().values().stream().map(z -> z.getName()).collect(Collectors.toList());
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException
	{
		return choice;
	}

	public Text getUsage(CommandSource src)
	{
		return Text.EMPTY;
	}
}
