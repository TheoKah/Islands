package com.carrot.islands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.world.BlockChangeFlag;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.extent.ArchetypeVolume;
import org.spongepowered.api.world.schematic.BlockPaletteTypes;
import org.spongepowered.api.world.schematic.Schematic;

import com.carrot.islands.channel.IslandMessageChannel;
import com.carrot.islands.object.Island;
import com.carrot.islands.object.Point;
import com.carrot.islands.object.Rect;
import com.carrot.islands.object.Request;
import com.carrot.islands.object.Zone;
import com.carrot.islands.serializer.IslandDeserializer;
import com.carrot.islands.serializer.IslandSerializer;
import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.math.IntMath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ninja.leaping.configurate.ConfigurationNode;

public class DataHandler
{
	private static File schematicFile;
	private static Schematic schematic;
	private static File islandsDir;
	private static Gson gson;

	private static Hashtable<UUID, Island> islands;
	private static Hashtable<UUID, Hashtable<Vector2i, ArrayList<Island>>> worldChunks;
	private static HashMap<UUID, Island> lastIslandWalkedOn;
	private static HashMap<UUID, Zone> lastZoneWalkedOn;
	private static Hashtable<UUID, Point> firstPoints;
	private static Hashtable<UUID, Point> secondPoints;
	private static ArrayList<Request> inviteRequests;
	private static ArrayList<Request> joinRequests;
	private static ArrayList<BiomeType> biomes;
	private static IslandMessageChannel spyChannel;

	private static final String[] FAKE_PLAYERS = {
			"00000000-0000-0000-0000-000000000000",
			"0d0c4ca0-4ff1-11e4-916c-0800200c9a66",
			"41c82c87-7afb-4024-ba57-13d2c99cae77"};
	
	public static void init(File rootDir)
	{
		biomes = new ArrayList<BiomeType>();
		schematicFile = new File(rootDir, "island.schematic");
		islands = new Hashtable<UUID, Island>();
		islandsDir = new File(rootDir, "islands");
		lastIslandWalkedOn = new HashMap<UUID, Island>();
		lastZoneWalkedOn = new HashMap<UUID, Zone>();
		firstPoints = new Hashtable<UUID, Point>();
		secondPoints = new Hashtable<UUID, Point>();
		inviteRequests = new ArrayList<Request>();
		joinRequests = new ArrayList<Request>();
		spyChannel = new IslandMessageChannel();
		gson = (new GsonBuilder())
				.registerTypeAdapter(Island.class, new IslandSerializer())
				.registerTypeAdapter(Island.class, new IslandDeserializer())
				.setPrettyPrinting()
				.create();
	}

	public static void load()
	{
		populateBiomeList();
		islandsDir.mkdirs();
		for (File f : islandsDir.listFiles())
		{
			if (f.isFile() && f.getName().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.json"))
			{
				try
				{
					String json = new String(Files.readAllBytes(f.toPath()));
					Island island = gson.fromJson(json, Island.class);
					islands.put(island.getUUID(), island);
				}
				catch (IOException e)
				{
					IslandsPlugin.getLogger().error("Error while loading file " + f.getName());
					e.printStackTrace();
				}
			}
		}
		calculateWorldChunks();
		
		DataContainer schematicData;
        try {
            schematicData = DataFormats.NBT.readFrom(new GZIPInputStream(new FileInputStream(schematicFile)));
        } catch (Exception e) {
            e.printStackTrace();
            IslandsPlugin.getLogger().error("Error loading schematic: " + e.getMessage());
            return ;
        }

        schematic = DataTranslators.SCHEMATIC.translate(schematicData);
		
	}

	public static void save()
	{
		for (UUID uuid : islands.keySet())
		{
			saveIsland(uuid);
		}

	}
	
	public static IslandMessageChannel getSpyChannel()
	{
		return spyChannel;
	}

	// islands

	public static Location<World> getNextIslandLocation(World world) {
		ConfigurationNode nextIsland = ConfigHandler.getNode("worlds").getNode(world.getName()).getNode("nextIsland");
		int next = nextIsland.getInt();
		nextIsland.setValue(next + 1);
		ConfigHandler.save();
		Vector2i loc = getIslandLocation(next);
		int mult = ConfigHandler.getNode("others", "islandMultiplicatorLocation").getInt();
		return new Location<World>(world, loc.getX() * mult, ConfigHandler.getNode("others", "islandHeigth").getInt(), loc.getY() * mult);
	}

	public static Vector2i getIslandLocation(int n) {
		int k = (int) Math.ceil((Math.sqrt(n) - 1) / 2);
		int t = 2 * k + 1;
		int m = (int) Math.pow(t, 2);
		t = t - 1;
		if (n >= m - t)
			return new Vector2i(k-(m-n), -k);

		m = m - t;

		if (n >= m - t)
			return new Vector2i(-k, -k+(m-n));

		m = m - t;

		if (n >= m - t)
			return new Vector2i(-k+(m-n), k);

		return new Vector2i(k, k-(m-n-t));
	}

	public static void addIsland(Island island)
	{
		islands.put(island.getUUID(), island);
		saveIsland(island.getUUID());
	}

	public static Island getIsland(UUID uuid)
	{
		return islands.get(uuid);
	}

	public static Island getIsland(String name)
	{
		for (Island island : islands.values())
		{
			if (island.getRealName().equalsIgnoreCase(name))
			{
				return island;
			}
		}
		return null;
	}
	
	public static Island getIslandByTag(String tag)
	{
		for (Island island : islands.values())
		{
			if (island.getTag().equalsIgnoreCase(tag))
			{
				return island;
			}
		}
		return null;
	}

	public static Island getIsland(Location<World> loc)
	{
		if (!worldChunks.containsKey(loc.getExtent().getUniqueId()))
		{
			return null;
		}
		Vector2i area = new Vector2i(IntMath.divide(loc.getBlockX(), 16, RoundingMode.FLOOR), IntMath.divide(loc.getBlockZ(), 16, RoundingMode.FLOOR));
		if (!worldChunks.get(loc.getExtent().getUniqueId()).containsKey(area))
		{
			return null;
		}
		for (Island island : worldChunks.get(loc.getExtent().getUniqueId()).get(area))
		{
			if (island.isInside(loc))
			{
				return island;
			}
		}
		return null;
	}

	public static Island getIslandOfPlayer(UUID uuid)
	{
		for (Island island : islands.values())
		{
			for (UUID citizen : island.getCitizens())
			{
				if (citizen.equals(uuid))
				{
					return island;
				}
			}
		}
		return null;
	}

	public static void removeIsland(UUID uuid)
	{
		Island oldNation = getIsland(uuid);
		if (oldNation != null) {
			MessageChannel.TO_CONSOLE.send(Text.of("Removing Island " + uuid + ": "));
			MessageChannel.TO_CONSOLE.send(Utils.formatIslandDescription(oldNation, Utils.CLICKER_ADMIN));
		}
		islands.remove(uuid);

		ArrayList<UUID> toRemove = new ArrayList<>();
		for (Island island : lastIslandWalkedOn.values())
		{
			if (island != null && island.getUUID().equals(uuid))
			{
				toRemove.add(island.getUUID());
			}
		}
		for (UUID uuidToRemove : toRemove)
		{
			lastIslandWalkedOn.remove(uuidToRemove);
		}

		calculateWorldChunks();

		inviteRequests.removeIf(req -> req.getIslandUUID().equals(uuid));
		joinRequests.removeIf(req -> req.getIslandUUID().equals(uuid));

		File file = new File(islandsDir, uuid.toString() + ".json");
		file.delete();
	}

	public static Hashtable<UUID, Island> getIslands()
	{
		return islands;
	}

	public static boolean getFlag(String flag, Location<World> loc)
	{
		Island island = getIsland(loc);
		if (island == null)
		{
			return ConfigHandler.getNode("worlds").getNode(loc.getExtent().getName()).getNode("flags").getNode(flag).getBoolean();
		}
		Zone zone = island.getZone(loc);
		if (zone == null)
		{
			return island.getFlag(flag);
		}
		return zone.getFlag(flag);
	}

	public static boolean getPerm(String perm, UUID playerUUID, Location<World> loc)
	{
		Island island = getIsland(loc);
		if (island == null)
		{
			return ConfigHandler.getNode("worlds").getNode(loc.getExtent().getName()).getNode("perms").getNode(perm).getBoolean();
		}
		Zone zone = island.getZone(loc);
		if (zone == null)
		{
			if (island.isCitizen(playerUUID))
			{
				if (island.isStaff(playerUUID))
				{
					return true;
				}
				return island.getPerm(Island.TYPE_CITIZEN, perm);
			}
			return island.getPerm(Island.TYPE_OUTSIDER, perm);
		}

		if (island.isStaff(playerUUID) || zone.isOwner(playerUUID))
			return true;
		if (zone.isCoowner(playerUUID))
			return zone.getPerm(Island.TYPE_COOWNER, perm);
		if (island.isCitizen(playerUUID))
			return zone.getPerm(Island.TYPE_CITIZEN, perm);

		return zone.getPerm(Island.TYPE_OUTSIDER, perm);
	}
	
	public static boolean getPerm(String perm, Location<World> loc)
	{
		Island island = getIsland(loc);
		if (island == null)
		{
			return ConfigHandler.getNode("worlds").getNode(loc.getExtent().getName()).getNode("perms").getNode(perm).getBoolean();
		}
		Zone zone = island.getZone(loc);
		if (zone == null)
		{
			return island.getPerm(Island.TYPE_OUTSIDER, perm);
		}
		
		return zone.getPerm(Island.TYPE_OUTSIDER, perm);
	}

	// players

	public static String getPlayerName(UUID uuid)
	{
		Optional<Player> optPlayer = Sponge.getServer().getPlayer(uuid);
		if (optPlayer.isPresent())
		{
			return optPlayer.get().getName();
		}
		try
		{
			return Sponge.getServer().getGameProfileManager().get(uuid).get().getName().get();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static Collection<String> getPlayerNames()
	{
		return Sponge.getServer().getGameProfileManager().getCache().getProfiles().stream().filter(gp -> gp.getName().isPresent()).map(gp -> gp.getName().get()).collect(Collectors.toList());
	}
	
	public static ArrayList<BiomeType> getBiomes()
	{
		return biomes;
	}

	private static void populateBiomeList() {
		biomes.add(BiomeTypes.OCEAN);
		biomes.add(BiomeTypes.FOREST);
		biomes.add(BiomeTypes.DESERT);
		biomes.add(BiomeTypes.JUNGLE);
		biomes.add(BiomeTypes.SWAMPLAND);
		biomes.add(BiomeTypes.TAIGA);
		biomes.add(BiomeTypes.MUSHROOM_ISLAND);
		biomes.add(BiomeTypes.HELL);
		biomes.add(BiomeTypes.SKY);
		biomes.add(BiomeTypes.PLAINS);
		biomes.add(BiomeTypes.EXTREME_HILLS);
		biomes.add(BiomeTypes.FLOWER_FOREST);
		biomes.add(BiomeTypes.VOID);
	}
	
	public static UUID getPlayerUUID(String name)
	{
		Optional<Player> optPlayer = Sponge.getServer().getPlayer(name);
		if (optPlayer.isPresent())
		{
			return optPlayer.get().getUniqueId();
		}
		try
		{
			return Sponge.getServer().getGameProfileManager().get(name).get().getUniqueId();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static String getCitizenTitle(UUID uuid)
	{
		if (!ConfigHandler.getNode("others", "enableIslandRanks").getBoolean())
		{
			return "";
		}
		Island island = getIslandOfPlayer(uuid);
		if (island == null)
		{
			return LanguageHandler.LR;
		}
		if (island.isPresident(uuid))
		{
			return ConfigHandler.getIslandRank(island.getNumCitizens()).getNode("presidentTitle").getString();
		}
		if (island.isPresident(uuid))
		{
			return LanguageHandler.LQ;
		}
		return LanguageHandler.IS;
	}

	public static void calculateWorldChunks()
	{
		worldChunks = new Hashtable<UUID, Hashtable<Vector2i, ArrayList<Island>>>();
		for (Island island : islands.values())
		{
			addToWorldChunks(island);
		}
	}

	public static void addToWorldChunks(Island island)
	{
		Rect r = island.getArea();
		if (!worldChunks.containsKey(r.getWorld()))
		{
			worldChunks.put(r.getWorld(), new Hashtable<Vector2i, ArrayList<Island>>());
		}
		Hashtable<Vector2i, ArrayList<Island>> chunks = worldChunks.get(r.getWorld());
		for (int i = IntMath.divide(r.getMinX(), 16, RoundingMode.FLOOR); i < IntMath.divide(r.getMaxX(), 16, RoundingMode.FLOOR) + 1; i++)
		{
			for (int j = IntMath.divide(r.getMinY(), 16, RoundingMode.FLOOR); j < IntMath.divide(r.getMaxY(), 16, RoundingMode.FLOOR) + 1; j++)
			{
				Vector2i vect = new Vector2i(i, j);
				if (!chunks.containsKey(vect))
				{
					chunks.put(vect, new ArrayList<Island>());
				}
				if (!chunks.get(vect).contains(island))
				{
					chunks.get(vect).add(island);
				}
			}
		}	
	}
	
	public static boolean isFakePlayer(Player player) {
		String uuid = player.getUniqueId().toString();
		for (int i = 0; i < FAKE_PLAYERS.length; ++i) {
			if (uuid.equals(FAKE_PLAYERS[i]))
				return true;
		}
		return false;
	}
	
	// lastIslandWalkedOn

	public static Island getLastIslandWalkedOn(UUID uuid)
	{
		return lastIslandWalkedOn.get(uuid);
	}

	public static void setLastIslandWalkedOn(UUID uuid, Island island)
	{
		lastIslandWalkedOn.put(uuid, island);
	}

	public static Zone getLastZoneWalkedOn(UUID uuid)
	{
		return lastZoneWalkedOn.get(uuid);
	}

	public static void setLastZoneWalkedOn(UUID uuid, Zone zone)
	{
		lastZoneWalkedOn.put(uuid, zone);
	}

	// points

	public static Point getFirstPoint(UUID uuid)
	{
		return firstPoints.get(uuid);
	}

	public static void setFirstPoint(UUID uuid, Point point)
	{
		firstPoints.put(uuid, point);
	}

	public static void removeFirstPoint(UUID uuid)
	{
		firstPoints.remove(uuid);
	}

	public static Point getSecondPoint(UUID uuid)
	{
		return secondPoints.get(uuid);
	}

	public static void setSecondPoint(UUID uuid, Point point)
	{
		secondPoints.put(uuid, point);
	}

	public static void removeSecondPoint(UUID uuid)
	{
		secondPoints.remove(uuid);
	}

	// requests

	public static Request getJoinRequest(UUID islandUUID, UUID uuid)
	{
		for (Request req : joinRequests)
		{
			if (req.match(islandUUID, uuid))
			{
				return req;
			}
		}
		return null;
	}

	public static void addJoinRequest(Request req)
	{
		joinRequests.add(req);
	}

	public static void removeJoinRequest(Request req)
	{
		joinRequests.remove(req);
	}

	public static Request getInviteRequest(UUID islandUUID, UUID uuid)
	{
		for (Request req : inviteRequests)
		{
			if (req.match(islandUUID, uuid))
			{
				return req;
			}
		}
		return null;
	}

	public static void addInviteRequest(Request req)
	{
		inviteRequests.add(req);
	}

	public static void removeInviteRequest(Request req)
	{
		inviteRequests.remove(req);
	}

//	public static void giveStartKit(Inventory inventory)
//	{
//		inventory.clear();
//		ConfigurationNode template = ConfigHandler.getNode("templates", "startkit");
//		for (ConfigurationNode item : template.getChildrenList()) {
//			try {
//				inventory.offer(item.getValue(TypeToken.of(ItemStack.class)));
//			} catch (ObjectMappingException e) {
//				IslandsPlugin.getLogger().error("Unable to give item startkit from template");
//				e.printStackTrace();
//			}
//		}
//	}
	
	public static void resetPlayer(Player player)
	{
//		if (Sponge.getPluginManager().getPlugin("baubles").isPresent()) {
//			System.out.println("baubles present");
//			Optional<BaublesApi> baubles = (Optional<BaublesApi>) Sponge.getPluginManager().getPlugin("baubles").get().getInstance();
//			if (baubles.isPresent()) {
//				System.out.println("baubles api got");
//				IInventory baubleInv = baubles.get().getBaubles(player);
//			}
//		}
		player.getInventory().clear();
		player.getEnderChestInventory().clear();
        player.offer(Keys.HEALTH, player.get(Keys.MAX_HEALTH).orElse(1.0));
        player.offer(Keys.FOOD_LEVEL,player.foodLevel().getDefault());
        player.offer(Keys.SATURATION,player.saturation().getDefault());
	}
	
	public static void generateIslandTemplate(Location<World> loc)
	{
		if (schematic == null) {
			for (int x = -2; x <= 2; ++x) {
				for (int z = -2; z <= 2; ++z) {
					Location<World> location = new Location<World>(loc.getExtent(), loc.getBlockX() + x, loc.getBlockY() - 1, loc.getBlockZ() + z);
					location.setBlockType(BlockTypes.DIRT);

				}
			}
            return ;
        }
		
	schematic.apply(loc, BlockChangeFlags.ALL);
		
//		ConfigurationNode template = ConfigHandler.getNode("templates", "island");
//		if (template.hasListChildren()) {
//			for (ConfigurationNode node : template.getChildrenList()) {
//				Location<World> location = new Location<World>(loc.getExtent(), loc.getBlockX() + node.getNode("x").getInt(), loc.getBlockY() + node.getNode("y").getInt(), loc.getBlockZ() + node.getNode("z").getInt());
//				try {
//					BlockState block = node.getNode("block").getValue(TypeToken.of(BlockState.class));
//					location.setBlock(block, IslandsPlugin.getCause());
//				} catch (ObjectMappingException e) {
//					IslandsPlugin.getLogger().error("Unable to retrieve block from template");
//					e.printStackTrace();
//				}
//			}
//			
//		} else {
//			for (int x = -2; x <= 2; ++x) {
//				for (int z = -2; z <= 2; ++z) {
//					Location<World> location = new Location<World>(loc.getExtent(), loc.getBlockX() + x, loc.getBlockY() - 1, loc.getBlockZ() + z);
//					location.setBlockType(BlockTypes.DIRT, IslandsPlugin.getCause());
//
//				}
//			}
//		}
	}

	// saves

//	public static void saveStartKit(CarriedInventory<? extends Carrier> inventory) {
//		ConfigHandler.getNode("templates").removeChild("startkit");
//		ConfigurationNode template = ConfigHandler.getNode("templates", "startkit");
//		for (Inventory slot : inventory.slots()) {
//			if (slot.peek().isPresent()) {
//				try {
//					template.getAppendedNode().setValue(TypeToken.of(ItemStack.class), slot.peek().get());
//				} catch (ObjectMappingException e) {
//					IslandsPlugin.getLogger().error("Unable to save item startkit");
//					e.printStackTrace();
//				}
//			}
//		}
//		ConfigHandler.save();
//	}
	
	public static void saveTemplate(Location<World> player, Vector3i min, Vector3i max)
	{
		ArchetypeVolume volume = player.getExtent().createArchetypeVolume(min, max, player.getBlockPosition());
		
		schematic = Schematic.builder()
				.volume(volume)
				.paletteType(BlockPaletteTypes.LOCAL)
				.build();
		
		DataContainer schematicData = DataTranslators.SCHEMATIC.translate(schematic);
		
		try {
			DataFormats.NBT.writeTo(new GZIPOutputStream(new FileOutputStream(schematicFile)), schematicData);
		} catch (Exception e) {
			IslandsPlugin.getLogger().error("Unable to store schematic: " + e.getMessage());
		}
		
//		ConfigHandler.getNode("templates").removeChild("island");
//		ConfigurationNode template = ConfigHandler.getNode("templates", "island");
//
//		for (int y = min.getY(); y < max.getY(); ++y) {
//			for (int x = min.getX(); x <= max.getX(); ++x) {
//				for (int z = min.getZ(); z <= max.getZ(); ++z) {
//					Location<World> location = new Location<World>(player.getExtent(), x, y, z);
//					BlockState block = location.getBlock();
//					if (block.getType() != BlockTypes.AIR)
//					{
//						ConfigurationNode node = template.getAppendedNode();
//						try {
//							node.getNode("block").setValue(TypeToken.of(BlockState.class), block);
//							node.getNode("x").setValue(x - player.getBlockX());
//							node.getNode("y").setValue(y - player.getBlockY());
//							node.getNode("z").setValue(z - player.getBlockZ());
//						} catch (ObjectMappingException e) {
//							IslandsPlugin.getLogger().error("Unable to store block at location <" + x + ", " + y + ", " + z + ">: " + block.getType().getName());
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//
//		ConfigHandler.save();
	}

	public static void saveIsland(UUID uuid)
	{
		Island island = islands.get(uuid);
		if (island == null)
		{
			IslandsPlugin.getLogger().warn("Trying to save null island !");
			return;
		}
		File file = new File(islandsDir, uuid.toString() + ".json");
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
			}
			String json = gson.toJson(island, Island.class);
			Files.write(file.toPath(), json.getBytes());
		}
		catch (IOException e)
		{
			IslandsPlugin.getLogger().error("Error while saving file " + file.getName() + " for island " + island.getName());
		}
	}
}
