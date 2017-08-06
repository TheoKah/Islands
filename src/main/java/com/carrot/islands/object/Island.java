package com.carrot.islands.object;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.extent.worker.procedure.BiomeVolumeFiller;

import com.carrot.islands.ConfigHandler;
import com.carrot.islands.DataHandler;
import com.carrot.islands.channel.IslandMessageChannel;
import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3i;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Island
{
	public static final String TYPE_OUTSIDER = "outsider";
	public static final String TYPE_CITIZEN = "citizen";
	public static final String TYPE_COOWNER = "coowner";

	public static final String PERM_BUILD = "build";
	public static final String PERM_INTERACT = "interact";

	private UUID uuid;
	private String name;
	private String tag;
	private boolean isAdmin;
	private Hashtable<String, Location<World>> spawns;
	private Rect area;
	private UUID president;
	private ArrayList<UUID> ministers;
	private ArrayList<UUID> citizens;
	private Hashtable<String, Hashtable<String, Boolean>> perms;
	private Hashtable<String, Boolean> flags;
	private Hashtable<UUID, Zone> zones;
	private int extraspawns;
	private BiomeType biome;
	private Hashtable<String, Boolean> unownedCauses;

	private IslandMessageChannel channel = new IslandMessageChannel();

	@SuppressWarnings("serial")
	public Island(UUID uuid, String name, boolean isAdmin, Rect area)
	{
		this.uuid = uuid;
		this.name = name;
		this.tag = null;
		this.isAdmin = isAdmin;
		this.area = area;
		this.extraspawns = 0;
		this.biome = BiomeTypes.SKY;
		this.spawns = new Hashtable<String, Location<World>>();
		this.president = null;
		this.ministers = new ArrayList<UUID>();
		this.citizens = new ArrayList<UUID>();
		this.flags = new Hashtable<String, Boolean>();
		this.unownedCauses = new Hashtable<String, Boolean>();
		for (Entry<Object, ? extends CommentedConfigurationNode> e : ConfigHandler.getNode("islands", "flags").getChildrenMap().entrySet())
		{
			flags.put(e.getKey().toString(), e.getValue().getBoolean());
		}
		this.perms = new Hashtable<String, Hashtable<String, Boolean>>()
		{{
			put(TYPE_OUTSIDER, new Hashtable<String, Boolean>()
			{{
				put(PERM_BUILD, ConfigHandler.getNode("islands", "perms").getNode(TYPE_OUTSIDER).getNode(PERM_BUILD).getBoolean());
				put(PERM_INTERACT, ConfigHandler.getNode("islands", "perms").getNode(TYPE_OUTSIDER).getNode(PERM_INTERACT).getBoolean());
			}});
			put(TYPE_CITIZEN, new Hashtable<String, Boolean>()
			{{
				put(PERM_BUILD, ConfigHandler.getNode("islands", "perms").getNode(TYPE_CITIZEN).getNode(PERM_BUILD).getBoolean());
				put(PERM_INTERACT, ConfigHandler.getNode("islands", "perms").getNode(TYPE_CITIZEN).getNode(PERM_INTERACT).getBoolean());
			}});
		}};
		this.zones = new Hashtable<UUID, Zone>();
	}

	public UUID getUUID()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		return name.replace("_", " ");
	}

	public String getRealName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean hasTag()
	{
		return tag != null;
	}

	public String getTag()
	{
		if (tag == null)
			return getName();
		return tag;
	}
	
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	public boolean isAdmin()
	{
		return isAdmin;	
	}

	public boolean isInside(Location<World> loc) {
		return area.isInside(loc);
	}

	public boolean isInside(Rect rect) {
		return rect.pointsInside(area).size() == 4;
	}

	public Rect getArea() {
		return area;
	}

	public Location<World> getSpawn(String name)
	{
		return spawns.get(name);
	}

	public void addSpawn(String name, Location<World> spawn)
	{
		this.spawns.put(name, spawn);
	}

	public void removeSpawn(String name)
	{
		this.spawns.remove(name);
	}

	public Hashtable<String, Location<World>> getSpawns()
	{
		return spawns;
	}

	public int getNumSpawns()
	{
		return spawns.size();
	}

	public int getMaxSpawns()
	{
		return ConfigHandler.getNode("others", "maxNationSpawns").getInt() + extraspawns;
	}

	public int getExtraSpawns()
	{
		return extraspawns;
	}

	public void setExtraSpawns(int extraspawns)
	{
		this.extraspawns = extraspawns;
		if (this.extraspawns < 0)
			this.extraspawns = 0;
	}

	public void addExtraSpawns(int extraspawns)
	{
		this.extraspawns += extraspawns;
		if (this.extraspawns < 0)
			this.extraspawns = 0;
	}

	public void removeExtraSpawns(int extraspawns)
	{
		this.extraspawns -= extraspawns;
		if (this.extraspawns < 0)
			this.extraspawns = 0;
	}

	public UUID getPresident()
	{
		return president;
	}

	public void setPresident(UUID president)
	{
		this.president = president;
	}

	public boolean isPresident(UUID uuid)
	{
		return uuid.equals(president);
	}

	public ArrayList<UUID> getMinisters()
	{
		return ministers;
	}

	public void addMinister(UUID uuid)
	{
		ministers.add(uuid);
	}

	public void removeMinister(UUID uuid)
	{
		ministers.remove(uuid);
	}

	public boolean isMinister(UUID uuid)
	{
		return ministers.contains(uuid);
	}

	public ArrayList<UUID> getStaff()
	{
		ArrayList<UUID> staff = new ArrayList<UUID>();
		staff.add(president);
		staff.addAll(ministers);
		return staff;
	}

	public boolean isStaff(UUID uuid)
	{
		if (isPresident(uuid) || isMinister(uuid))
			return true;
		if (!isAdmin())
			return false;
		Optional<Player> player = Sponge.getServer().getPlayer(uuid);
		if (player.isPresent() && player.get().hasPermission("islands.admin.zone.staff"))
			return true;
		return false;
	}

	public ArrayList<UUID> getCitizens()
	{
		return citizens;
	}

	public void addCitizen(UUID uuid)
	{
		citizens.add(uuid);
		Optional<Player> player = Sponge.getServer().getPlayer(uuid);
		if (player.isPresent())
			channel.addMember(player.get());
	}

	public boolean isCitizen(UUID uuid)
	{
		return citizens.contains(uuid);
	}

	public int getNumCitizens()
	{
		return citizens.size();
	}

	public void removeCitizen(UUID uuid)
	{
		zones.values().stream()
		.filter(zone -> uuid.equals(zone.getOwner()))
		.forEach(zone -> zone.setOwner(null));
		ministers.remove(uuid);
		citizens.remove(uuid);
		Optional<Player> player = Sponge.getServer().getPlayer(uuid);
		if (player.isPresent())
		{
			channel.removeMember(player.get());
			player.get().setMessageChannel(MessageChannel.TO_ALL);
		}
	}

	public Hashtable<String, Boolean> getFlags()
	{
		return flags;
	}

	public void setFlag(String flag, boolean b)
	{
		flags.put(flag, b);
	}

	public boolean getFlag(String flag)
	{
		return flags.get(flag);
	}

	public boolean getFlag(String flag, Location<World> loc)
	{
		Zone zone = getZone(loc);
		if (zone == null || !zone.hasFlag(flag))
		{
			return getFlag(flag);
		}
		return zone.getFlag(flag);
	}

	public boolean getPerm(String type, String perm)
	{
		return perms.get(type).get(perm);
	}

	public Hashtable<String, Hashtable<String, Boolean>> getPerms()
	{
		return perms;
	}

	public void setPerm(String type, String perm, boolean bool)
	{
		perms.get(type).put(perm, bool);
		unownedCauses.clear();
	}

	public Hashtable<UUID, Zone> getZones()
	{
		return zones;
	}

	public Zone getZone(Location<World> loc)
	{
		Vector2i p = new Vector2i(loc.getBlockX(), loc.getBlockZ());
		for (Zone zone : zones.values())
		{
			if (zone.getRect().isInside(p))
			{
				return zone;
			}
		}
		return null;
	}

	public void addZone(Zone zone)
	{
		zones.put(zone.getUUID(), zone);
	}

	public void removeZone(UUID uuid)
	{
		zones.remove(uuid);
	}

	public String getBiomeName() {
		return biome.getName();
	}

	public void setBiome(String biome) {
		if (biome == null)
			return;
		for (BiomeType b : DataHandler.getBiomes()) {
			if (b.getName().equalsIgnoreCase(biome)) {
				this.biome = b;
				return ;
			}
		}
	}

	public void setBiome(BiomeType biome) {
		if (this.biome != null && this.biome.equals(biome.getName()))
			return ;
		this.biome = biome;
		Vector3i A = new Vector3i(area.getMinX(), 0, area.getMinY());
		Vector3i B = new Vector3i(area.getMaxX(), 255, area.getMaxY());

		Optional<World> world = Sponge.getServer().getWorld(area.getWorld());
		if (!world.isPresent())
			return ;
		Extent view = world.get().getExtentView(A, B);
		view.getBiomeWorker().fill(new BiomeVolumeFiller() {

			@Override
			public BiomeType produce(int x, int y, int z) {
				return biome;
			}
		});
	}

	public IslandMessageChannel getMessageChannel()
	{
		return channel;
	}

}
