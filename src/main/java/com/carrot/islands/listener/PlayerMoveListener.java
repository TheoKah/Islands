package com.carrot.islands.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.Utils;
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
		if (Sponge.getServer().getRunningTimeTicks() % 5 != 0)
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
		
		Text.Builder builder = Text.builder("~ ").color(TextColors.GRAY);
		
		builder.append((island == null) ? Text.of(TextColors.DARK_GREEN, LanguageHandler.IA) : Utils.islandClickable(TextColors.DARK_AQUA, island.getName()));
		builder.append(Text.of(TextColors.GRAY, " - "));
		if (zone != null)
		{
			builder.append(Utils.zoneClickable(TextColors.GREEN, zone.getName()));
			builder.append(Text.of(TextColors.GRAY, " - "));
		}
		
		builder.append((DataHandler.getFlag("pvp", loc)) ? Text.of(TextColors.DARK_RED, "(PvP)") : Text.of(TextColors.DARK_GREEN, "(No PvP)"));
		builder.append(Text.of(TextColors.GRAY, " ~"));
		
		player.sendMessage(builder.build());
	}
}
