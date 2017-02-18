package com.carrot.islands.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.ConstructEntityEvent;

import com.carrot.islands.ConfigHandler;


public class MobSpawningListener
{	
	//	@Listener
	//	public void onEntitySpawn(SpawnEntityEvent event)
	//	{
	//		if (!ConfigHandler.getNode("worlds").getNode(event.getTargetWorld().getName()).getNode("enabled").getBoolean())
	//		{
	//			return;
	//		}
	//		System.out.println(">>block<<");
	//		for (Entity e : event.getEntities()) {
	//			if (e instanceof Monster)
	//				System.out.println("found monster before");
	//		}
	////		event.filterEntities(e -> !(e instanceof Monster) || DataHandler.getFlag("mobs", e.getLocation()));
	//		event.filterEntities(e -> !(e instanceof Monster));
	//		for (Entity e : event.getEntities()) {
	//			if (e instanceof Monster)
	//				System.out.println("found monster after");
	//		}
	//		System.out.println("<<block>>");
	//		
	//	}

//	@Listener(order=Order.FIRST)
//	public void onEntitySpawn(ConstructEntityEvent.Pre event)
//	{
//		if (!ConfigHandler.getNode("worlds").getNode(event.getTransform().getExtent().getName()).getNode("enabled").getBoolean())
//		{
//			return;
//		}
//
//		if (event.getTargetType().getId().equals("minecraft:bat") || event.getTargetType().equals("minecraft:squid"))
//		{
//			event.setCancelled(true);
//		}
//	}
}
