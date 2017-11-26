package com.carrot.islands.listener;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageEvent.MessageFormatter;
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
		
		MessageFormatter formater = e.getFormatter();
		
		if (chan.equals(MessageChannel.TO_ALL) && ConfigHandler.getNode("others", "enableIslandTag").getBoolean(true))
		{
			e.setMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(ConfigHandler.getNode("others", "publicChatFormat").getString().replaceAll("\\{ISLAND\\}", island.getTag()).replaceAll("\\{TITLE\\}", DataHandler.getCitizenTitle(p.getUniqueId()))), formater.getHeader().toText()), formater.getBody().toText());
		}
		else if (chan instanceof IslandMessageChannel)
		{
			e.setMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(ConfigHandler.getNode("others", "islandChatFormat").getString().replaceAll("\\{ISLAND\\}", island.getTag()).replaceAll("\\{TITLE\\}", DataHandler.getCitizenTitle(p.getUniqueId()))), formater.getHeader().toText()), Text.of(TextColors.YELLOW, formater.getBody().toText()));
			DataHandler.getSpyChannel().send(p, Text.of(TextColors.WHITE, " [", TextColors.RED, "SC", TextColors.WHITE,  "]", TextColors.RESET, e.getMessage()));
		}
	}
}
