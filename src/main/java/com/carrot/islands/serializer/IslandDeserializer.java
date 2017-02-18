package com.carrot.islands.serializer;

import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import com.carrot.islands.object.Island;
import com.carrot.islands.object.Rect;
import com.carrot.islands.object.Zone;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class IslandDeserializer implements JsonDeserializer<Island>
{
	@Override
	public Island deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException
	{
		JsonObject obj = json.getAsJsonObject();
		UUID uuid = UUID.fromString(obj.get("uuid").getAsString());
		String name = obj.get("name").getAsString();
		boolean isAdmin = obj.get("admin").getAsBoolean();
		Island island = new Island(uuid, name, isAdmin, new Rect(
				UUID.fromString(obj.get("world").getAsString()),
				obj.get("minX").getAsInt(),
				obj.get("maxX").getAsInt(),
				obj.get("minY").getAsInt(),
				obj.get("maxY").getAsInt()));
		island.setBiome(obj.get("biome").getAsString());
		for (Entry<String, JsonElement> e : obj.get("flags").getAsJsonObject().entrySet())
		{
			island.setFlag(e.getKey(), e.getValue().getAsBoolean());
		}
		for (Entry<String, JsonElement> e : obj.get("perms").getAsJsonObject().entrySet())
		{
			for (Entry<String, JsonElement> en : e.getValue().getAsJsonObject().entrySet())
			{
				island.setPerm(e.getKey(), en.getKey(), en.getValue().getAsBoolean());
			}
		}
		
		if (obj.has("zones")) {
			for (JsonElement e : obj.get("zones").getAsJsonArray())
			{
				JsonObject zoneObj = e.getAsJsonObject();
				UUID zoneUUID = UUID.fromString(zoneObj.get("uuid").getAsString());
				String zoneName = zoneObj.get("name").getAsString();
				
				JsonObject rectObj = zoneObj.get("rect").getAsJsonObject();
				Rect rect = new Rect(
						UUID.fromString(rectObj.get("world").getAsString()),
						rectObj.get("minX").getAsInt(),
						rectObj.get("maxX").getAsInt(),
						rectObj.get("minY").getAsInt(),
						rectObj.get("maxY").getAsInt());
				Zone zone = new Zone(zoneUUID, zoneName, rect);
				if (zoneObj.has("owner"))
				{
					zone.setOwner(UUID.fromString(zoneObj.get("owner").getAsString()));
				}
				for (JsonElement el : zoneObj.get("coowners").getAsJsonArray())
				{
					zone.addCoowner(UUID.fromString(el.getAsString()));
				}
				for (Entry<String, JsonElement> en : zoneObj.get("flags").getAsJsonObject().entrySet())
				{
					zone.setFlag(en.getKey(), en.getValue().getAsBoolean());
				}
				for (Entry<String, JsonElement> en : zoneObj.get("perms").getAsJsonObject().entrySet())
				{
					for (Entry<String, JsonElement> ent : en.getValue().getAsJsonObject().entrySet())
					{
						zone.setPerm(en.getKey(), ent.getKey(), ent.getValue().getAsBoolean());
					}
				}
				island.addZone(zone);
			}
		}
		
		if (!isAdmin)
		{
			island.setPresident(UUID.fromString(obj.get("president").getAsString()));
			for (JsonElement element : obj.get("ministers").getAsJsonArray())
			{
				island.addMinister(UUID.fromString(element.getAsString()));
			}
			for (JsonElement element : obj.get("citizens").getAsJsonArray())
			{
				island.addCitizen(UUID.fromString(element.getAsString()));
			}

			for (Entry<String, JsonElement> e : obj.get("spawns").getAsJsonObject().entrySet())
			{
				JsonObject spawnObj = e.getValue().getAsJsonObject();
				Optional<World> optWorld = Sponge.getServer().getWorld(UUID.fromString(spawnObj.get("world").getAsString()));
				if (optWorld.isPresent())
				{
					island.addSpawn(e.getKey(), optWorld.get().getLocation(spawnObj.get("x").getAsDouble(), spawnObj.get("y").getAsDouble(), spawnObj.get("z").getAsDouble()));
				}
			}
		}
		return island;
	}
}
