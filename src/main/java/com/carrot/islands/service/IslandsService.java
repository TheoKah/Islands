package com.carrot.islands.service;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.carrot.islands.DataHandler;
import com.carrot.islands.object.Island;

public class IslandsService
{
	public Optional<String> getIslandNameOfPlayer(UUID uuid)
	{
		Island island = DataHandler.getIslandOfPlayer(uuid);
		if (island == null)
		{
			return Optional.empty();
		}
		return Optional.of(island.getRealName());
	}
	
	public Optional<String> getIslandNameAtLocation(Location<World> loc)
	{
		Island island = DataHandler.getIsland(loc);
		if (island == null)
		{
			return Optional.empty();
		}
		return Optional.of(island.getRealName());
	}
	
	public boolean hasIsland(UUID uuid)
	{
		return DataHandler.getIslandOfPlayer(uuid) != null;
	}
	
	public boolean isPresident(UUID uuid)
	{
		Island island = DataHandler.getIslandOfPlayer(uuid);
		if (island == null)
		{
			return false;
		}
		return island.isPresident(uuid);
	}
	
	public boolean isMinister(UUID uuid)
	{
		Island island = DataHandler.getIslandOfPlayer(uuid);
		if (island == null)
		{
			return false;
		}
		return island.isMinister(uuid);
	}
}
