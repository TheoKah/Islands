package com.carrot.islands.listener;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.world.ExplosionEvent;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.Debugger;

public class ExplosionListener
{
	@Listener(order=Order.FIRST)
	public void onExplosion(ExplosionEvent.Pre event)
	{
		if (!ConfigHandler.getNode("worlds").getNode(event.getTargetWorld().getName()).getNode("enabled").getBoolean())
		{
			return;
		}
		Debugger.log("onExplosion", event);
		if (!DataHandler.getFlag("explosions", event.getExplosion().getLocation()))
		{
			event.setCancelled(true);
			/*event.setExplosion(Sponge.getRegistry().createBuilder(Explosion.Builder.class)
					.from(event.getExplosion())
					.canCauseFire(canCauseFire)
					.shouldBreakBlocks(canBreakBlocks)
					.build());*/
		}
	}
}
