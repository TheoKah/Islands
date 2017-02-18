package com.carrot.islands.listener;

import java.util.Optional;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;

public class FireListener
{
	@Listener(order=Order.EARLY)
	public void onFire(ChangeBlockEvent event)
	{
		if (!ConfigHandler.getNode("worlds").getNode(event.getTargetWorld().getName()).getNode("enabled").getBoolean())
		{
			return;
		}
		event
		.getTransactions()
		.stream()
		.filter(trans -> trans.getFinal().getState().getType() == BlockTypes.FIRE)
		.filter(trans -> {
			Optional<Location<World>> optLoc = trans.getFinal().getLocation();
			if (!optLoc.isPresent())
				return false;
			return !DataHandler.getFlag("fire", optLoc.get());
		})
		.forEach(trans -> trans.setValid(false));
	}
}
