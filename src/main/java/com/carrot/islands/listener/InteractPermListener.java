package com.carrot.islands.listener;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.Debugger;
import com.carrot.islands.LanguageHandler;

public class InteractPermListener
{
	@Listener(order=Order.FIRST)
	public void onInteract(InteractBlockEvent event, @First Player player)
	{
		if (!ConfigHandler.getNode("worlds").getNode(player.getWorld().getName()).getNode("enabled").getBoolean())
		{
			return;
		}
		if (event.getTargetBlock().getState().getId().startsWith("exnihiloadscensio:blockSieve") || 
				event.getTargetBlock().getState().getId().startsWith("exnihiloadscensio:blockBarrel"))
		{
			return;
		}
		Debugger.log("onInteract", event, player);
		if (player.hasPermission("islands.admin.bypass.perm.interact"))
		{
			return;
		}
		event.getTargetBlock().getLocation().ifPresent(loc -> {
			if (!DataHandler.getPerm("interact", player.getUniqueId(), loc))
			{
				event.setCancelled(true);
				player.sendMessage(Text.of(TextColors.RED, LanguageHandler.HI));
			}
		});
	}
}
