package com.carrot.islands.listener;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.channel.IslandMessageChannel;
import com.carrot.islands.object.Island;

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
			e.setMessage(TextSerializers.FORMATTING_CODE.deserialize(ConfigHandler.getNode("others", "publicChatFormat").getString().replaceAll("\\{ISLAND\\}", island.getTag()).replaceAll("\\{TITLE\\}", DataHandler.getCitizenTitle(p.getUniqueId()))), e.getMessage());
		}
		else if (chan instanceof IslandMessageChannel)
		{
			e.setMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(ConfigHandler.getNode("others", "islandChatFormat").getString().replaceAll("\\{ISLAND\\}", island.getTag()).replaceAll("\\{TITLE\\}", DataHandler.getCitizenTitle(p.getUniqueId()))), TextColors.YELLOW, e.getMessage()));
			DataHandler.getSpyChannel().send(p, Text.of(TextColors.WHITE, " [", TextColors.RED, "SpyChat", TextColors.WHITE,  "]", TextColors.RESET, e.getMessage()));
		}
	}
}
