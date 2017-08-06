package com.carrot.islands.listener;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.channel.MessageChannel;

import com.carrot.islands.DataHandler;
import com.carrot.islands.object.Island;

public class PlayerConnectionListener
{
	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join event)
	{
		if (event.getTargetEntity() instanceof Player)
		{
			Player player = event.getTargetEntity();
			Island island = DataHandler.getIslandOfPlayer(player.getUniqueId());
			if (island != null)
				island.getMessageChannel().addMember(player);
			player.setMessageChannel(MessageChannel.TO_ALL);
			if (player.hasPermission("islands.admin.spychat"))
				DataHandler.getSpyChannel().addMember(player);
		}
	}

	@Listener
	public void onPlayerLeave(ClientConnectionEvent.Disconnect event)
	{
		if (event.getTargetEntity() instanceof Player)
		{
			Player player = (Player) event.getTargetEntity();
			DataHandler.removeFirstPoint(player.getUniqueId());
			DataHandler.removeSecondPoint(player.getUniqueId());
			Island island = DataHandler.getIslandOfPlayer(player.getUniqueId());
			if (island != null)
				island.getMessageChannel().removeMember(player);
			DataHandler.getSpyChannel().removeMember(player);
		}
	}
}
