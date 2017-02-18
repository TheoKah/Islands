package com.carrot.islands;

import java.io.File;
import java.io.IOException;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import com.carrot.islands.object.Island;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigHandler
{
	private static File configFile;
	private static ConfigurationLoader<CommentedConfigurationNode> configManager;
	private static CommentedConfigurationNode config;

	public static void init(File rootDir)
	{
		configFile = new File(rootDir, "config.conf");
		configManager = HoconConfigurationLoader.builder().setPath(configFile.toPath()).build();
	}
	
	public static void load()
	{
		load(null);
	}
	
	public static void load(CommandSource src)
	{
		// load file
		try
		{
			if (!configFile.exists())
			{
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				config = configManager.load();
				configManager.save(config);
			}
			config = configManager.load();
		}
		catch (IOException e)
		{
			IslandsPlugin.getLogger().error(LanguageHandler.CY);
			e.printStackTrace();
			if (src != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CY));
			}
		}
		
		// check integrity
				
		Utils.ensurePositiveNumber(config.getNode("others", "islandRadius"), 256);
		Utils.ensurePositiveNumber(config.getNode("others", "islandMultiplicatorLocation"), 640);
		Utils.ensurePositiveNumber(config.getNode("others", "islandHeigth"), 150);
		Utils.ensurePositiveNumber(config.getNode("others", "maxIslandSpawns"), 3);
		Utils.ensurePositiveNumber(config.getNode("others", "minIslandNameLength"), 3);
		Utils.ensurePositiveNumber(config.getNode("others", "maxIslandNameLength"), 13);
		Utils.ensurePositiveNumber(config.getNode("others", "minZoneNameLength"), 3);
		Utils.ensurePositiveNumber(config.getNode("others", "maxZoneNameLength"), 13);
		Utils.ensureBoolean(config.getNode("others", "enableIslandRanks"), true);
		Utils.ensureBoolean(config.getNode("others", "enableIslandTag"), true);
		Utils.ensureString(config.getNode("others", "gravestoneBlock"), "gravestone:gravestone");	
		
		
		Utils.ensureBoolean(config.getNode("islands", "flags", "pvp"), false);
		Utils.ensureBoolean(config.getNode("islands", "flags", "mobs"), false);
		Utils.ensureBoolean(config.getNode("islands", "flags", "fire"), false);
		Utils.ensureBoolean(config.getNode("islands", "flags", "explosions"), false);
		Utils.ensureBoolean(config.getNode("islands", "flags", "open"), false);
		Utils.ensureBoolean(config.getNode("islands", "flags", "public"), false);
		
		Utils.ensureBoolean(config.getNode("islands", "perms").getNode(Island.TYPE_OUTSIDER).getNode(Island.PERM_BUILD), false);
		Utils.ensureBoolean(config.getNode("islands", "perms").getNode(Island.TYPE_OUTSIDER).getNode(Island.PERM_INTERACT), false);
		Utils.ensureBoolean(config.getNode("islands", "perms").getNode(Island.TYPE_CITIZEN).getNode(Island.PERM_BUILD), false);
		Utils.ensureBoolean(config.getNode("islands", "perms").getNode(Island.TYPE_CITIZEN).getNode(Island.PERM_INTERACT), true);
		
		Utils.ensureBoolean(config.getNode("zones", "perms").getNode(Island.TYPE_OUTSIDER).getNode(Island.PERM_BUILD), false);
		Utils.ensureBoolean(config.getNode("zones", "perms").getNode(Island.TYPE_OUTSIDER).getNode(Island.PERM_INTERACT), false);
		Utils.ensureBoolean(config.getNode("zones", "perms").getNode(Island.TYPE_CITIZEN).getNode(Island.PERM_BUILD), false);
		Utils.ensureBoolean(config.getNode("zones", "perms").getNode(Island.TYPE_CITIZEN).getNode(Island.PERM_INTERACT), true);
		Utils.ensureBoolean(config.getNode("zones", "perms").getNode(Island.TYPE_COOWNER).getNode(Island.PERM_BUILD), true);
		Utils.ensureBoolean(config.getNode("zones", "perms").getNode(Island.TYPE_COOWNER).getNode(Island.PERM_INTERACT), true);
		
		if (config.getNode("others", "enableIslandRanks").getBoolean())
		{
			if (!config.getNode("islandRanks").hasListChildren() || config.getNode("islandRanks").getChildrenList().isEmpty())
			{
				CommentedConfigurationNode rank;

				rank = config.getNode("islandRanks").getAppendedNode();
				rank.getNode("numCitizens").setValue(1);
				rank.getNode("islandTitle").setValue("Land");
				rank.getNode("presidentTitle").setValue("Leader");

				rank = config.getNode("islandRanks").getAppendedNode();
				rank.getNode("numCitizens").setValue(3);
				rank.getNode("islandTitle").setValue("Federation");
				rank.getNode("presidentTitle").setValue("Count");

				rank = config.getNode("islandRanks").getAppendedNode();
				rank.getNode("numCitizens").setValue(6);
				rank.getNode("islandTitle").setValue("Dominion");
				rank.getNode("presidentTitle").setValue("Duke");

				rank = config.getNode("islandRanks").getAppendedNode();
				rank.getNode("numCitizens").setValue(10);
				rank.getNode("islandTitle").setValue("Kingdom");
				rank.getNode("presidentTitle").setValue("King");

				rank = config.getNode("islandRanks").getAppendedNode();
				rank.getNode("numCitizens").setValue(15);
				rank.getNode("islandTitle").setValue("Empire");
				rank.getNode("presidentTitle").setValue("Emperor");
			}
			boolean defaultRankMissing = true;
			for (CommentedConfigurationNode rank : config.getNode("islandRanks").getChildrenList())
			{
				Utils.ensurePositiveNumber(rank.getNode("numCitizens"), 1000000);
				Utils.ensureString(rank.getNode("islandTitle"), "NO_TITLE");
				Utils.ensureString(rank.getNode("presidentTitle"), "NO_TITLE");
				if (rank.getNode("numCitizens").getInt() == 0)
					defaultRankMissing = false;
			}
			if (defaultRankMissing) {
				CommentedConfigurationNode rank = config.getNode("islandRanks").getAppendedNode();
				rank.getNode("numCitizens").setValue(0);
				rank.getNode("islandTitle").setValue("Virtual");
				rank.getNode("presidentTitle").setValue("Leader");
			}
		}
		
		for (World world : Sponge.getServer().getWorlds())
		{
			CommentedConfigurationNode node = config.getNode("worlds").getNode(world.getName());
			
			Utils.ensureBoolean(node.getNode("enabled"), true);
			if (node.getNode("enabled").getBoolean())
			{

				Utils.ensurePositiveNumber(node.getNode("nextIsland"), 1);
				
				Utils.ensureBoolean(node.getNode("perms").getNode(Island.PERM_BUILD), true);
				Utils.ensureBoolean(node.getNode("perms").getNode(Island.PERM_INTERACT), true);
				
				Utils.ensureBoolean(node.getNode("flags", "pvp"), true);
				Utils.ensureBoolean(node.getNode("flags", "mobs"), true);
				Utils.ensureBoolean(node.getNode("flags", "fire"), true);
				Utils.ensureBoolean(node.getNode("flags", "explosions"), true);
			}
			else
			{
				node.removeChild("perms");
				node.removeChild("flags");
			}
		}
		save();
		if (src != null)
		{
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.CZ));
		}
	}

	public static void save()
	{
		try
		{
			configManager.save(config);
		}
		catch (IOException e)
		{
			IslandsPlugin.getLogger().error("Could not save config file !");
		}
	}

	public static CommentedConfigurationNode getNode(String... path)
	{
		return config.getNode((Object[]) path);
	}
	
	public static CommentedConfigurationNode getIslandRank(int numCitizens)
	{
		CommentedConfigurationNode rank = config.getNode("islandRanks")
				.getChildrenList()
				.stream()
				.filter(node -> node.getNode("numCitizens").getInt() <= numCitizens)
				.max((CommentedConfigurationNode a, CommentedConfigurationNode b) ->
						Integer.compare(a.getNode("numCitizens").getInt(), b.getNode("numCitizens").getInt()))
				.get();
		return rank;
	}
	
	public static class Utils
	{
		public static void ensureString(CommentedConfigurationNode node, String def)
		{
			if (node.getString() == null)
			{
				node.setValue(def);
			}
		}

		public static void ensurePositiveNumber(CommentedConfigurationNode node, Number def)
		{
			if (!(node.getValue() instanceof Number) || node.getDouble(-1) < 0)
			{
				node.setValue(def);
			}
		}
		
		public static void ensureBoolean(CommentedConfigurationNode node, boolean def)
		{
			if (!(node.getValue() instanceof Boolean))
			{
				node.setValue(def);
			}
		}
	}
}
