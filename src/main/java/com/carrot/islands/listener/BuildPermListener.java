package com.carrot.islands.listener;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.Debugger;
import com.carrot.islands.LanguageHandler;

public class BuildPermListener
{

	@Listener(order=Order.FIRST)
	public void onPlayerPlacesBlock(ChangeBlockEvent.Place event, @First Player player)
	{
		if (!ConfigHandler.getNode("worlds").getNode(event.getTargetWorld().getName()).getNode("enabled").getBoolean())
		{
			return;
		}
		Debugger.log("onPlayerPlacesBlock", event, player);
		if (player.hasPermission("islands.admin.bypass.perm.build"))
		{
			return;
		}
		event
		.getTransactions()
		.stream()
		.forEach(trans -> trans.getOriginal().getLocation().ifPresent(loc -> {
			if (!trans.getFinal().getState().getType().getId().equals(ConfigHandler.getNode("others", "gravestoneBlock").getString("gravestone:gravestone"))) {
				if(!DataHandler.getPerm("build", player.getUniqueId(), loc))
				{
					trans.setValid(false);
					try {
						player.sendMessage(Text.of(TextColors.RED, LanguageHandler.HH));
					} catch (Exception e) {}
				}
			}
		}));
	}

	@Listener(order=Order.FIRST)
	public void onPlayerBreaksBlock(ChangeBlockEvent.Break event, @First Player player)
	{
		if (!ConfigHandler.getNode("worlds").getNode(event.getTargetWorld().getName()).getNode("enabled").getBoolean())
		{
			return;
		}
		Debugger.log("onPlayerBreaksBlock", event, player);
		if (player.hasPermission("islands.admin.bypass.perm.build"))
		{
			return;
		}
		event
		.getTransactions()
		.stream()
		.forEach(trans -> trans.getOriginal().getLocation().ifPresent(loc -> {
			if(!DataHandler.getPerm("build", player.getUniqueId(), loc))
			{
				trans.setValid(false);
				try {
					player.sendMessage(Text.of(TextColors.RED, LanguageHandler.HH));
				} catch (Exception e) {}
			} else {
				trans.setValid(true);
			}
		}));
	}

	@Listener(order=Order.FIRST)
	public void onEntitySpawn(SpawnEntityEvent event, @First Player player, @First EntitySpawnCause entitySpawnCause)
	{
		if (!ConfigHandler.getNode("worlds").getNode(event.getTargetWorld().getName()).getNode("enabled").getBoolean())
		{
			return;
		}
		if (player.hasPermission("islands.admin.bypass.perm.build"))
		{
			return;
		}
		if (entitySpawnCause.getType() == SpawnTypes.PLACEMENT)
		{
			try {
			if (!DataHandler.getPerm("build", player.getUniqueId(), event.getEntities().get(0).getLocation()))
				event.setCancelled(true);
			} catch (IndexOutOfBoundsException e) {}
		}
	}

}
