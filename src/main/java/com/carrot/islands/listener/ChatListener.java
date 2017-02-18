package com.carrot.islands.listener;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.channel.IslandMessageChannel;
import com.carrot.islands.object.Island;

@Plugin(id = "spongeislandtag", name = "Sponge Island Chat Tag", version = "1.1",
description = "Towny like chat formating", authors = {"Carrot"})
public class ChatListener
{

	@Listener(order = Order.LATE)
	public void onPlayerChat(MessageChannelEvent.Chat e, @First Player p)
	{
		Island island = DataHandler.getIslandOfPlayer(p.getUniqueId());
		if (island == null)
		{
			return;
		}
		MessageChannel chan = MessageChannel.TO_ALL;
		Optional<MessageChannel> channel = e.getChannel();
		if (channel.isPresent())
		{
			chan = channel.get();
		}
		
		if (chan.equals(MessageChannel.TO_ALL) && ConfigHandler.getNode("others", "enableIslandTag").getBoolean(true))
		{
			e.setMessage(Text.of(TextColors.WHITE, " [", TextColors.DARK_AQUA, island.getName(), TextColors.WHITE,  "] "), e.getMessage());
		}
		else if (chan instanceof IslandMessageChannel)
		{
			e.setMessage(Text.of(TextColors.WHITE, " {", TextColors.YELLOW, island.getName(), TextColors.WHITE,  "} "), Text.of(TextColors.YELLOW, e.getMessage()));
		}
	}
}
