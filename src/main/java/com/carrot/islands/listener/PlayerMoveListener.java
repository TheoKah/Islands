package com.carrot.islands.listener;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;
import com.carrot.islands.object.Zone;

public class PlayerMoveListener
{
	@Listener
	public void onPlayerMove(MoveEntityEvent event, @First Player player)
	{
		if (event.getFromTransform().getLocation().getBlockX() == event.getToTransform().getLocation().getBlockX() && 
				event.getFromTransform().getLocation().getBlockZ() == event.getToTransform().getLocation().getBlockZ())
		{
			return;
		}
		if (!ConfigHandler.getNode("worlds").getNode(event.getToTransform().getExtent().getName()).getNode("enabled").getBoolean())
		{
			return;
		}

		Location<World> loc = event.getToTransform().getLocation();
		Island island = DataHandler.getIsland(loc);
		Island lastIslandWalkedOn = DataHandler.getLastIslandWalkedOn(player.getUniqueId());
		Zone zone = null;
		if (island != null)
		{
			zone = island.getZone(loc);
		}
		Zone lastZoneWalkedOn = DataHandler.getLastZoneWalkedOn(player.getUniqueId());
		if ((island == null && lastIslandWalkedOn == null) || (island != null && lastIslandWalkedOn != null && island.getUUID().equals(lastIslandWalkedOn.getUUID())))
		{
			if ((zone == null && lastZoneWalkedOn == null) || (zone != null && lastZoneWalkedOn != null && zone.getUUID().equals(lastZoneWalkedOn.getUUID())))
			{
				return;
			}
		}
		DataHandler.setLastIslandWalkedOn(player.getUniqueId(), island);
		DataHandler.setLastZoneWalkedOn(player.getUniqueId(), zone);

		String toast;

		if (island == null) {
			toast = ConfigHandler.getNode("toast", "wild").getString();
		} else {
			toast = (zone == null ? ConfigHandler.getNode("toast", "island").getString() : ConfigHandler.getNode("toast", "zone").getString());
		}
		
		String formatPresident = "";
		
		if (island != null && !island.isAdmin()) {
			formatPresident = ConfigHandler.getNode("toast", "formatPresident").getString()
					.replaceAll("\\{TITLE\\}", DataHandler.getCitizenTitle(island.getPresident()))
					.replaceAll("\\{NAME\\}", DataHandler.getPlayerName(island.getPresident()));
		}
		
		String formatZoneName = "";
		String formatZoneOwner = "";
		
		if (zone != null) {
			if (zone.isNamed())
				formatZoneName = ConfigHandler.getNode("toast", "formatZoneName").getString().replaceAll("\\{ARG\\}", zone.getName()) + " ";
			if (zone.isOwned())
				formatZoneOwner = ConfigHandler.getNode("toast", "formatZoneOwner").getString().replaceAll("\\{ARG\\}", DataHandler.getPlayerName(zone.getOwner())) + " ";
		}
		
		String formatPvp;
		
		if (DataHandler.getFlag("pvp", loc)) {
			formatPvp = ConfigHandler.getNode("toast", "formatPvp").getString().replaceAll("\\{ARG\\}", LanguageHandler.TOAST_PVP);
		} else {
			formatPvp = ConfigHandler.getNode("toast", "formatNoPvp").getString().replaceAll("\\{ARG\\}", LanguageHandler.TOAST_NOPVP);
		}
		
		if (island != null) {
			toast = toast.replaceAll("\\{ISLAND\\}", island.getName());
		} else {
			toast = toast.replaceAll("\\{WILD\\}", LanguageHandler.IA);
		}


		Text finalToast = TextSerializers.FORMATTING_CODE.deserialize(toast
				.replaceAll("\\{FORMATPRESIDENT\\}", formatPresident)
				.replaceAll("\\{FORMATZONENAME\\}", formatZoneName)
				.replaceAll("\\{FORMATZONEOWNER\\}", formatZoneOwner)
				.replaceAll("\\{FORMATPVP\\}", formatPvp));

		player.sendMessage(ChatTypes.ACTION_BAR, finalToast);
		MessageChannel.TO_CONSOLE.send(Text.of(player.getName(), " entered area ", finalToast));
	}
}
