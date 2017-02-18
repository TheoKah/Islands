package com.carrot.islands.serializer;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.UUID;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.carrot.islands.object.Island;
import com.carrot.islands.object.Rect;
import com.carrot.islands.object.Zone;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class IslandSerializer implements JsonSerializer<Island>
{
	@Override
	public JsonElement serialize(Island island, Type type, JsonSerializationContext ctx)
	{
		JsonObject json = new JsonObject();
		
		json.add("uuid", new JsonPrimitive(island.getUUID().toString()));
		json.add("name", new JsonPrimitive(island.getName()));
		json.add("biome", new JsonPrimitive(island.getBiomeName()));
		json.add("admin", new JsonPrimitive(island.isAdmin()));
		Rect area = island.getArea();
		json.add("world", new JsonPrimitive(area.getWorld().toString()));
		json.add("minX", new JsonPrimitive(area.getMinX()));
		json.add("maxX", new JsonPrimitive(area.getMaxX()));
		json.add("minY", new JsonPrimitive(area.getMinY()));
		json.add("maxY", new JsonPrimitive(area.getMaxY()));
		
		JsonObject flags = new JsonObject();
		for (Entry<String, Boolean> e : island.getFlags().entrySet())
		{
			flags.add(e.getKey(), new JsonPrimitive(e.getValue()));
		}
		json.add("flags", flags);
		
		JsonObject perms = new JsonObject();
		for (Entry<String, Hashtable<String, Boolean>> e : island.getPerms().entrySet())
		{
			JsonObject obj = new JsonObject();
			for (Entry<String, Boolean> en : e.getValue().entrySet())
			{
				obj.add(en.getKey(), new JsonPrimitive(en.getValue()));
			}
			perms.add(e.getKey(), obj);
		}
		json.add("perms", perms);
		
		JsonArray zonesArray = new JsonArray();
		for (Zone zone : island.getZones().values())
		{
			JsonObject zoneObj = new JsonObject();
			
			zoneObj.add("uuid", new JsonPrimitive(zone.getUUID().toString()));
			zoneObj.add("name", new JsonPrimitive(zone.getName()));
			
			JsonObject rectJson = new JsonObject();
			rectJson.add("world", new JsonPrimitive(zone.getRect().getWorld().toString()));
			rectJson.add("minX", new JsonPrimitive(zone.getRect().getMinX()));
			rectJson.add("maxX", new JsonPrimitive(zone.getRect().getMaxX()));
			rectJson.add("minY", new JsonPrimitive(zone.getRect().getMinY()));
			rectJson.add("maxY", new JsonPrimitive(zone.getRect().getMaxY()));
			zoneObj.add("rect", rectJson);
			
			if (zone.getOwner() != null)
			{
				zoneObj.add("owner", new JsonPrimitive(zone.getOwner().toString()));
			}
			
			JsonArray coownersArray = new JsonArray();
			for (UUID coowner : zone.getCoowners())
			{
				coownersArray.add(new JsonPrimitive(coowner.toString()));
			}
			zoneObj.add("coowners", coownersArray);
			
			JsonObject zoneFlags = new JsonObject();
			for (Entry<String, Boolean> e : zone.getFlags().entrySet())
			{
				zoneFlags.add(e.getKey(), new JsonPrimitive(e.getValue()));
			}
			zoneObj.add("flags", zoneFlags);
			
			JsonObject zonePerms = new JsonObject();
			for (Entry<String, Hashtable<String, Boolean>> e : zone.getPerms().entrySet())
			{
				JsonObject obj = new JsonObject();
				for (Entry<String, Boolean> en : e.getValue().entrySet())
				{
					obj.add(en.getKey(), new JsonPrimitive(en.getValue()));
				}
				zonePerms.add(e.getKey(), obj);
			}
			zoneObj.add("perms", zonePerms);
			
			zonesArray.add(zoneObj);
		}
		json.add("zones", zonesArray);
		
		if (!island.isAdmin())
		{
			json.add("president", new JsonPrimitive(island.getPresident().toString()));
			
			JsonArray ministersArray = new JsonArray();
			for (UUID minister : island.getMinisters())
			{
				ministersArray.add(new JsonPrimitive(minister.toString()));
			}
			json.add("ministers", ministersArray);
			
			JsonArray citizensArray = new JsonArray();
			for (UUID citizen : island.getCitizens())
			{
				citizensArray.add(new JsonPrimitive(citizen.toString()));
			}
			json.add("citizens", citizensArray);
			
			JsonObject spawns = new JsonObject();
			for (Entry<String, Location<World>> e : island.getSpawns().entrySet())
			{
				JsonObject loc = new JsonObject();
				loc.add("world", new JsonPrimitive(e.getValue().getExtent().getUniqueId().toString()));
				loc.add("x", new JsonPrimitive(e.getValue().getX()));
				loc.add("y", new JsonPrimitive(e.getValue().getY()));
				loc.add("z", new JsonPrimitive(e.getValue().getZ()));
				
				spawns.add(e.getKey(), loc);
			}
			json.add("spawns", spawns);
			
		}
		return json;
	}
}
