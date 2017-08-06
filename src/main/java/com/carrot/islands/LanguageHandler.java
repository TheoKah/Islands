package com.carrot.islands;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class LanguageHandler
{
	public static String AA = "get island details";
	public static String AB = "get details of the island your standing on";
	public static String AC = "get the list of all islands";
	public static String AD = "create a new island";
	public static String AE = "change island biome";
	public static String AF = "if your island have a spawn named 'home', tp to it";
	public static String AI = "invite player in your island";
	public static String AJ = "ask island staff to let you in the island";
	public static String AK = "kick player out of your island";
	public static String AL = "leave your island";
	public static String AM = "resign as the island president";
	public static String AN = "manage ministers";
	public static String AO = "set island perm";
	public static String AP = "set island flag";
	public static String AQ = "teleport to spawn with the given name";
	public static String AR = "set spawn with the given name";
	public static String AS = "delete spawn with the given name";
	public static String AU = "get player details";
	public static String AW = "toggle island chat";
	public static String AX = "teleport to spawn of a public island";

	public static String BA = "get zone details";
	public static String BB = "get details of the zone your standing on";
	public static String BC = "create a new zone";
	public static String BD = "manage coowners";
	public static String BE = "set zone owner";
	public static String BF = "make this zone owner free";
	public static String BG = "set zone perm";
	public static String BH = "set zone flag";
	public static String BK = "delete specified zone (or standing on)";

	public static String AZ = "reloads config file";
	public static String BR = "save island template";
	public static String BI = "set first kit from inventory";
	public static String BL = "create admin island";
	public static String BM = "delete given island";
	public static String BN = "set island's name";
	public static String BO = "set island's president";
	public static String BP = "make player join island";
	public static String BQ = "make player leave island";
	public static String BS = "set island perm";
	public static String BT = "set island flag";

	public static String BU = "get world details";
	public static String BV = "get the list of all worlds";
	public static String BW = "enable islands in specified world";
	public static String BX = "disable islands in specified world";
	public static String BY = "set world perm";
	public static String BZ = "set world flag";

	public static String CA = "You must be an in-game player to perform that command";
	public static String CB = "Invalid island name";
	public static String CC = "Invalid player name";
	public static String CD = "Invalid argument, you must use \"add\" or \"remove\"";
	public static String CE = "Invalid successor name";
	public static String CF = "Invalid zone name";
	public static String CG = "You must specify island name";
	public static String CH = "You must specify player name";
	public static String CI = "You must be in an island to perform that command, type /is ? for more help";
	public static String CJ = "You must be president of your island to perform that command";
	public static String CK = "You must be president or minister of your island to perform that command";
	public static String CN = "Island {ISLAND} fell into ruins !";
	public static String CO = "There is no island created yet";
	public static String CP = "You don't have permission to list all zones of that island";
	public static String CQ = "That player is already president";
	public static String CR = "That player is not part of the island";
	public static String CS = "Islands plugin is disabled for this world";
	public static String CT = "Invalid world name";
	public static String CU = "You must specify world name";
	public static String CV = "Islands plugin is already enabled for this world";
	public static String CW = "Islands plugin is already disabled for this world";
	public static String CX = "Invalid argument, you must use \"give\", \"take\" or \"set\"";
	public static String CY = "Could not load or create config file";
	public static String CZ = "Config file has been reloaded";

	public static String DQ = "You must be standing in an island to perform that command";
	public static String DS = "rename zone";
	public static String DT = "You are now speaking in your island's private channel";
	public static String DU = "You are no longer speaking in your island's private channel";
	public static String DV = "You are now spying islands' private channels";
	public static String DW = "You are no longer spying islands' private channels";
	public static String DX = "spy on islands' private channels";

	public static String EA = "You must select a region with a golden axe first (right/left click)";
	public static String EB = "Building your island...";
	public static String EC = "Changing biome...";
	public static String EK = "You must leave your island to perform that command";
	public static String EL = "That name is already taken";
	public static String EM = "Island name must be alphanumeric";
	public static String EN = "Island name must contain at least {MIN} and at most {MAX} characters";
	public static String EP = "{PLAYER} has created a new island named {ISLAND}";
	public static String EQ = "You successfully created island {ISLAND}";
	public static String ER = "Click to delete spawn {SPAWNLIST} ";
	public static String ES = "Your island doen't have any spawn with that name";
	public static String ET = "Successfully removed island spawn";
	public static String EV = "You are not standing on any island's region";
	public static String EW = "That player is already in your island";
	public static String EX = "Your island already invited this citizen";
	public static String EY = "{PLAYER} joined the island";
	public static String EZ = "You joined island {ISLAND}";
	public static String FA = "You were invited to join island {ISLAND}, {CLICKHERE} to accept invitation";
	public static String FB = "Request was send to {RECEIVER}";
	public static String FC = "You already asked that island";
	public static String FD = "There are no players in the island's staff connected yet";
	public static String FE = "{PLAYER} wants to join your island, {CLICKHERE} to accept request";
	public static String FF = "That player is not in your island";
	public static String FG = "You can't kick yourself out of your island, use /is leave to quit the island";
	public static String FH = "You can't kick the president out of your island";
	public static String FI = "You can't kick a fellow minister out of your island";
	public static String FJ = "{PLAYER} was kicked out of your island";
	public static String FK = "You were kicked out of your island by {PLAYER}";
	public static String FL = "You must first resign as president before you leave the island, use /is resign";
	public static String FM = "You left your island";
	public static String FN = "{PLAYER} left the island";
	public static String FO = "You can't add/remove yourself from the ministers of your island";
	public static String FP = "{PLAYER} is already minister of your island";
	public static String FQ = "{PLAYER} was successfully added to the ministers of your island";
	public static String FR = "{PLAYER} added you to the ministers of your island";
	public static String FS = "{PLAYER} is already not minister of your island";
	public static String FT = "{PLAYER} was successfully removed from the ministers of your island";
	public static String FU = "{PLAYER} removed you from the ministers of your island";
	public static String FV = "{SUCCESSOR} replaces now {PLAYER} as island's president";
	public static String FW = "Island {OLDNAME} changed its name to {NEWNAME}";
	public static String FX = "Island spawn must be set inside your territory";
	public static String FY = "Spawn name must be alphanumeric and must contain between {MIN} and {MAX} characters";
	public static String FZ = "Successfully changed the island spawn";
	public static String GA = "You can teleport to {SPAWNLIST} ";
	public static String GB = "Invalid spawn name, choose between {SPAWNLIST} ";
	public static String GC = "Teleported you to the island spawn";
	public static String GD = "You must be standing on a zone to perform that command";
	public static String GE = "You can set biome to {BIOMELIST} ";
	public static String GF = "No spawn named 'home' found. Make one with /is setspawn home";
	public static String GG = "You are now the new owner of zone {ZONE}";
	public static String GI = "You're not standing on any zone of your island";
	public static String GJ = "You must be owner of that zone to perform that command";
	public static String GK = "You can't add/remove yourself from the coowners of your zone";
	public static String GL = "{PLAYER} is already coowner of your zone";
	public static String GM = "{PLAYER} was successfully added to the coowners of your zone";
	public static String GN = "{PLAYER} added you to the coowners of zone {ZONE}";
	public static String GO = "{PLAYER} is already not coowner of your zone";
	public static String GP = "{PLAYER} was successfully removed from the coowners of your zone";
	public static String GQ = "{PLAYER} removed you from the coowners of zone {ZONE}";
	public static String GR = "There already is a zone with that name in your island";
	public static String GS = "There is a zone that instersects with your selection";
	public static String GT = "You have successfully created a zone named {ZONE}";
	public static String GU = "You are now the owner of zone {ZONE} inside of your island";
	public static String GV = "You must own this zone to perform that command";
	public static String GW = "Zone {ZONE} has now no owner";
	public static String GX = "You must be standing on your zone to perform that command";
	public static String GY = "You must own this zone to perform that command";
	public static String GZ = "You must specify zone name or stand on it";
	public static String HA = "{ISLAND}'s zones are {ZONELIST}";
	public static String HB = "Player is already owner of the zone";
	public static String HC = "New owner must be part of your island";
	public static String HD = "{PLAYER} is now the new owner of zone {ZONE}";
	public static String HE = "{PLAYER} set you as the owner of zone {ZONE}";
	public static String HF = "Your selection contains a zone of your island";
	public static String HG = "Selected zone is not inside your island's region";
	public static String HH = "You don't have permission to build here";
	public static String HI = "You don't have permission to interact here";
	public static String HJ = "Player is not part of an island";
	public static String HK = "Player is president of his island, use /ia setpres";
	public static String HL = "Success !";
	public static String HM = "You've successfully deleted zone {ZONE} in your island";
	public static String HR = "Your island can't have more than {MAX} spawns";
	public static String HS = "You renamed the zone to {ZONE}";
	public static String HT = "This island is not public";
	public static String HU = "Teleport will start in 2 seconds";
	public static String HV = "Template stored";
	public static String HW = "Unknown Biome";

	public static String IA = "Void";
	public static String IB = "Island";
	public static String IC = "Zone";
	public static String ID = "Biome";
	public static String IG = "Spawn";
	public static String IH = "President";
	public static String II = "Ministers";
	public static String IJ = "Citizens";
	public static String IK = "Permissions";
	public static String IL = "Outsiders";
	public static String IM = "Flags";
	public static String IN = "Owner";
	public static String IO = "Coowners";
	public static String IP = "None";
	public static String IQ = "Unknown";
	public static String IS = "Citizen";
	public static String IT = "ENABLED";
	public static String IU = "DISABLED";
	public static String IX = "click";
	public static String IY = "Admin";
	public static String IZ = "Zones";

	public static String JA = "click here";
	public static String JB = "Island List";
	public static String JC = "World List";
	public static String JD = "BUILD";
	public static String JE = "INTERACT";
	public static String JG = "true";
	public static String JH = "false";

	public static String KA = "First position set to {COORD}";
	public static String KB = "Second position set to {COORD}";

	public static String HX = "Unnamed";
	public static String LG = "manage extra spawns";
	public static String LH = "manage extra spawns using player name";
	public static String LR = "Hermit";
	public static String LQ = "Minister";
	public static String LL = "That tag is already taken";
	public static String LM = "Nation tag must be alphanumeric";
	public static String LN = "Nation tag must contain at least {MIN} and at most {MAX} characters";
	public static String LO = "Nation {NAME} changed its tag from {OLDTAG} to {NEWTAG}";
	public static String LP = "set nation's tag";
	
	public static String TOAST_PVP = "PvP";
	public static String TOAST_NOPVP = "No PvP";

	private static File languageFile;
	private static ConfigurationLoader<CommentedConfigurationNode> languageManager;
	private static CommentedConfigurationNode language;

	public static void init(File rootDir)
	{
		languageFile = new File(rootDir, "language.conf");
		languageManager = HoconConfigurationLoader.builder().setPath(languageFile.toPath()).build();

		try
		{
			if (!languageFile.exists())
			{
				languageFile.getParentFile().mkdirs();
				languageFile.createNewFile();
				language = languageManager.load();
				languageManager.save(language);
			}
			language = languageManager.load();
		}
		catch (IOException e)
		{
			IslandsPlugin.getLogger().error("Could not load or create language file !");
			e.printStackTrace();
		}

	}

	public static void load()
	{
		Field fields[] = LanguageHandler.class.getFields();
		for (int i = 0; i < fields.length; ++i) {
			if (fields[i].getType() != String.class)
				continue ;
			if (language.getNode(fields[i].getName()).getString() != null) {
				try {
					fields[i].set(String.class, language.getNode(fields[i].getName()).getString());
				} catch (IllegalArgumentException|IllegalAccessException e) {
					IslandsPlugin.getLogger().error("Error whey loading language string " + fields[i].getName());
					e.printStackTrace();
				}
			} else {
				try {
					language.getNode(fields[i].getName()).setValue(fields[i].get(String.class));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					IslandsPlugin.getLogger().error("Error whey saving language string " + fields[i].getName());
					e.printStackTrace();
				}
			}
		}

		save();
	}

	public static void save()
	{
		try
		{
			languageManager.save(language);
		}
		catch (IOException e)
		{
			IslandsPlugin.getLogger().error("Could not save config file !");
		}
	}
}
