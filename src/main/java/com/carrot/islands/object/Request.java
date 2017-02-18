package com.carrot.islands.object;

import java.util.Date;
import java.util.UUID;

public class Request
{
	private final UUID islandUUID;
	private final UUID playerUUID;
	private final Date date;

	public Request(UUID islandUUID, UUID playerUUID)
	{
		this.islandUUID = islandUUID;
		this.playerUUID = playerUUID;
		this.date = new Date();
	}

	public UUID getIslandUUID()
	{
		return islandUUID;
	}

	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	public Date getDate()
	{
		return date;
	}

	public boolean match(UUID islandUUID, UUID citizenUUID)
	{
		return (this.playerUUID.equals(citizenUUID) && this.islandUUID.equals(islandUUID));
	}
}
