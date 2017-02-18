package com.carrot.islands;

import java.io.File;
import java.io.IOException;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class LanguageHandler
{
	public static String AA;
	public static String AB;
	public static String AC;
	public static String AD;
	public static String AE;
	public static String AF;
	public static String AI;
	public static String AJ;
	public static String AK;
	public static String AL;
	public static String AM;
	public static String AN;
	public static String AO;
	public static String AP;
	public static String AQ;
	public static String AR;
	public static String AS;
	public static String AU;
	public static String AW;
	public static String AX;
	public static String AZ;
	
	public static String BA;
	public static String BB;
	public static String BC;
	public static String BD;
	public static String BE;
	public static String BF;
	public static String BG;
	public static String BH;
	public static String BI;
	public static String BK;

	public static String BL;
	public static String BM;
	public static String BN;
	public static String BO;
	public static String BP;
	public static String BQ;
	public static String BR;
	public static String BS;
	public static String BT;
	
	public static String BU;
	public static String BV;
	public static String BW;
	public static String BX;
	public static String BY;
	public static String BZ;

	public static String CA;
	public static String CB;
	public static String CC;
	public static String CD;
	public static String CE;
	public static String CF;
	public static String CG;
	public static String CH;
	public static String CI;
	public static String CJ;
	public static String CK;
	public static String CN;
	public static String CO;
	public static String CP;
	public static String CQ;
	public static String CR;
	public static String CS;
	public static String CT;
	public static String CU;
	public static String CV;
	public static String CW;
	public static String CY;
	public static String CZ;

	public static String DQ;
	public static String DS;
	public static String DT;
	public static String DU;

	public static String EA;
	public static String EB;
	public static String EC;
	public static String EK;
	public static String EL;
	public static String EM;
	public static String EN;
	public static String EP;
	public static String EQ;
	public static String ER;
	public static String ES;
	public static String ET;
	public static String EV;
	public static String EW;
	public static String EX;
	public static String EY;
	public static String EZ;
	public static String FA;
	public static String FB;
	public static String FC;
	public static String FD;
	public static String FE;
	public static String FF;
	public static String FG;
	public static String FH;
	public static String FI;
	public static String FJ;
	public static String FK;
	public static String FL;
	public static String FM;
	public static String FN;
	public static String FO;
	public static String FP;
	public static String FQ;
	public static String FR;
	public static String FS;
	public static String FT;
	public static String FU;
	public static String FV;
	public static String FW;
	public static String FX;
	public static String FY;
	public static String FZ;
	public static String GA;
	public static String GB;
	public static String GC;
	public static String GD;
	public static String GE;
	public static String GF;
	public static String GG;
	public static String GI;
	public static String GJ;
	public static String GK;
	public static String GL;
	public static String GM;
	public static String GN;
	public static String GO;
	public static String GP;
	public static String GQ;
	public static String GR;
	public static String GS;
	public static String GT;
	public static String GU;
	public static String GV;
	public static String GW;
	public static String GX;
	public static String GY;
	public static String GZ;
	public static String HA;
	public static String HB;
	public static String HC;
	public static String HD;
	public static String HE;
	public static String HF;
	public static String HG;
	public static String HH;
	public static String HI;
	public static String HJ;
	public static String HK;
	public static String HL;
	public static String HM;
	public static String HR;
	public static String HS;
	public static String HT;
	public static String HU;
	public static String HV;

	public static String IA;
	public static String IB;
	public static String IC;
	public static String ID;
	public static String IG;
	public static String IH;
	public static String II;
	public static String IJ;
	public static String IK;
	public static String IL;
	public static String IM;
	public static String IN;
	public static String IO;
	public static String IP;
	public static String IQ;
	public static String IS;
	public static String IT;
	public static String IU;
	public static String IX;
	public static String IY;
	public static String IZ;
	
	public static String JA;
	public static String JB;
	public static String JC;
	public static String JD;
	public static String JE;
	public static String JG;
	public static String JH;
	
	public static String KA;
	public static String KB;
	
	
	private static File languageFile;
	private static ConfigurationLoader<CommentedConfigurationNode> languageManager;
	private static CommentedConfigurationNode language;
	private static CommentedConfigurationNode defaultLanguage;
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
		
		defaultLanguage = HoconConfigurationLoader.builder().build().createEmptyNode();
		
		defaultLanguage.getNode("AA").setValue("get island details");
		defaultLanguage.getNode("AB").setValue("get details of the island your standing on");
		defaultLanguage.getNode("AC").setValue("get the list of all islands");
		defaultLanguage.getNode("AD").setValue("create a new island");
		defaultLanguage.getNode("AE").setValue("change island biome");
		defaultLanguage.getNode("AF").setValue("if your island have a spawn named 'home', tp to it");
		defaultLanguage.getNode("AI").setValue("invite player in your island");
		defaultLanguage.getNode("AJ").setValue("ask island staff to let you in the island");
		defaultLanguage.getNode("AK").setValue("kick player out of your island");
		defaultLanguage.getNode("AL").setValue("leave your island");
		defaultLanguage.getNode("AM").setValue("resign as the island president");
		defaultLanguage.getNode("AN").setValue("manage ministers");
		defaultLanguage.getNode("AO").setValue("set island perm");
		defaultLanguage.getNode("AP").setValue("set island flag");
		defaultLanguage.getNode("AQ").setValue("teleport to spawn with the given name");
		defaultLanguage.getNode("AR").setValue("set spawn with the given name");
		defaultLanguage.getNode("AS").setValue("delete spawn with the given name");
		defaultLanguage.getNode("AU").setValue("get player details");
		defaultLanguage.getNode("AW").setValue("toggle island chat");
		defaultLanguage.getNode("AX").setValue("teleport to spawn of a public island");
		
		defaultLanguage.getNode("BA").setValue("get zone details");
		defaultLanguage.getNode("BB").setValue("get details of the zone your standing on");
		defaultLanguage.getNode("BC").setValue("create a new zone");
		defaultLanguage.getNode("BD").setValue("manage coowners");
		defaultLanguage.getNode("BE").setValue("set zone owner");
		defaultLanguage.getNode("BF").setValue("make this zone owner free");
		defaultLanguage.getNode("BG").setValue("set zone perm");
		defaultLanguage.getNode("BH").setValue("set zone flag");
		defaultLanguage.getNode("BK").setValue("delete specified zone (or standing on)");

		defaultLanguage.getNode("AZ").setValue("reloads config file");
		defaultLanguage.getNode("BR").setValue("save island template");
		defaultLanguage.getNode("BI").setValue("set first kit from inventory");
		defaultLanguage.getNode("BL").setValue("create admin island");
		defaultLanguage.getNode("BM").setValue("delete given island");
		defaultLanguage.getNode("BN").setValue("set island's name");
		defaultLanguage.getNode("BO").setValue("set island's president");
		defaultLanguage.getNode("BP").setValue("make player join island");
		defaultLanguage.getNode("BQ").setValue("make player leave island");
		defaultLanguage.getNode("BS").setValue("set island perm");
		defaultLanguage.getNode("BT").setValue("set island flag");
		
		defaultLanguage.getNode("BU").setValue("get world details");
		defaultLanguage.getNode("BV").setValue("get the list of all worlds");
		defaultLanguage.getNode("BW").setValue("enable islands in specified world");
		defaultLanguage.getNode("BX").setValue("disable islands in specified world");
		defaultLanguage.getNode("BY").setValue("set world perm");
		defaultLanguage.getNode("BZ").setValue("set world flag");

		defaultLanguage.getNode("CA").setValue("You must be an in-game player to perform that command");
		defaultLanguage.getNode("CB").setValue("Invalid island name");
		defaultLanguage.getNode("CC").setValue("Invalid player name");
		defaultLanguage.getNode("CD").setValue("Invalid argument, you must use \"add\" or \"remove\"");
		defaultLanguage.getNode("CE").setValue("Invalid successor name");
		defaultLanguage.getNode("CF").setValue("Invalid zone name");
		defaultLanguage.getNode("CG").setValue("You must specify island name");
		defaultLanguage.getNode("CH").setValue("You must specify player name");
		defaultLanguage.getNode("CI").setValue("You must be in an island to perform that command");
		defaultLanguage.getNode("CJ").setValue("You must be president of your island to perform that command");
		defaultLanguage.getNode("CK").setValue("You must be president or minister of your island to perform that command");
		defaultLanguage.getNode("CN").setValue("Island {ISLAND} fell into ruins !");
		defaultLanguage.getNode("CO").setValue("There is no island created yet");
		defaultLanguage.getNode("CP").setValue("You don't have permission to list all zones of that island");
		defaultLanguage.getNode("CQ").setValue("That player is already president");
		defaultLanguage.getNode("CR").setValue("That player is not part of the island");
		defaultLanguage.getNode("CS").setValue("Islands plugin is disabled for this world");
		defaultLanguage.getNode("CT").setValue("Invalid world name");
		defaultLanguage.getNode("CU").setValue("You must specify world name");
		defaultLanguage.getNode("CV").setValue("Islands plugin is already enabled for this world");
		defaultLanguage.getNode("CW").setValue("Islands plugin is already disabled for this world");
		defaultLanguage.getNode("CY").setValue("Could not load or create config file");
		defaultLanguage.getNode("CZ").setValue("Config file has been reloaded");

		defaultLanguage.getNode("DQ").setValue("You must be standing in an island to perform that command");
		defaultLanguage.getNode("DS").setValue("rename zone");
		defaultLanguage.getNode("DT").setValue("You are now speaking in your island's private channel");
		defaultLanguage.getNode("DU").setValue("You are no longer speaking in your island's private channel");
		
		defaultLanguage.getNode("EA").setValue("You must select a region with a golden axe first (right/left click)");
		defaultLanguage.getNode("EB").setValue("Building your island...");
		defaultLanguage.getNode("EC").setValue("Changing biome...");
		defaultLanguage.getNode("EK").setValue("You must leave your island to perform that command");
		defaultLanguage.getNode("EL").setValue("That name is already taken");
		defaultLanguage.getNode("EM").setValue("Island name must be alphanumeric");
		defaultLanguage.getNode("EN").setValue("Island name must contain at least {MIN} and at most {MAX} characters");
		defaultLanguage.getNode("EP").setValue("{PLAYER} has created a new island named {ISLAND}");
		defaultLanguage.getNode("EQ").setValue("You successfully created island {ISLAND}");
		defaultLanguage.getNode("ER").setValue("Click to delete spawn {SPAWNLIST} ");
		defaultLanguage.getNode("ES").setValue("Your island doen't have any spawn with that name");
		defaultLanguage.getNode("ET").setValue("Successfully removed island spawn");
		defaultLanguage.getNode("EV").setValue("You are not standing on any island's region");
		defaultLanguage.getNode("EW").setValue("That player is already in your island");
		defaultLanguage.getNode("EX").setValue("Your island already invited this citizen");
		defaultLanguage.getNode("EY").setValue("{PLAYER} joined the island");
		defaultLanguage.getNode("EZ").setValue("You joined island {ISLAND}");
		defaultLanguage.getNode("FA").setValue("You were invited to join island {ISLAND}, {CLICKHERE} to accept invitation");
		defaultLanguage.getNode("FB").setValue("Request was send to {RECEIVER}");
		defaultLanguage.getNode("FC").setValue("You already asked that island");
		defaultLanguage.getNode("FD").setValue("There are no players in the island's staff connected yet");
		defaultLanguage.getNode("FE").setValue("{PLAYER} wants to join your island, {CLICKHERE} to accept request");
		defaultLanguage.getNode("FF").setValue("That player is not in your island");
		defaultLanguage.getNode("FG").setValue("You can't kick yourself out of your island, use /is leave to quit the island");
		defaultLanguage.getNode("FH").setValue("You can't kick the president out of your island");
		defaultLanguage.getNode("FI").setValue("You can't kick a fellow minister out of your island");
		defaultLanguage.getNode("FJ").setValue("{PLAYER} was kicked out of your island");
		defaultLanguage.getNode("FK").setValue("You were kicked out of your island by {PLAYER}");
		defaultLanguage.getNode("FL").setValue("You must first resign as president before you leave the island, use /is resign");
		defaultLanguage.getNode("FM").setValue("You left your island");
		defaultLanguage.getNode("FN").setValue("{PLAYER} left the island");
		defaultLanguage.getNode("FO").setValue("You can't add/remove yourself from the ministers of your island");
		defaultLanguage.getNode("FP").setValue("{PLAYER} is already minister of your island");
		defaultLanguage.getNode("FQ").setValue("{PLAYER} was successfully added to the ministers of your island");
		defaultLanguage.getNode("FR").setValue("{PLAYER} added you to the ministers of your island");
		defaultLanguage.getNode("FS").setValue("{PLAYER} is already not minister of your island");
		defaultLanguage.getNode("FT").setValue("{PLAYER} was successfully removed from the ministers of your island");
		defaultLanguage.getNode("FU").setValue("{PLAYER} removed you from the ministers of your island");
		defaultLanguage.getNode("FV").setValue("{SUCCESSOR} replaces now {PLAYER} as island's president");
		defaultLanguage.getNode("FW").setValue("Island {OLDNAME} changed its name to {NEWNAME}");
		defaultLanguage.getNode("FX").setValue("Island spawn must be set inside your territory");
		defaultLanguage.getNode("FY").setValue("Spawn name must be alphanumeric and must contain between {MIN} and {MAX} characters");
		defaultLanguage.getNode("FZ").setValue("Successfully changed the island spawn");
		defaultLanguage.getNode("GA").setValue("You can teleport to {SPAWNLIST} ");
		defaultLanguage.getNode("GB").setValue("Invalid spawn name, choose between {SPAWNLIST} ");
		defaultLanguage.getNode("GC").setValue("Teleported you to the island spawn");
		defaultLanguage.getNode("GD").setValue("You must be standing on a zone to perform that command");
		defaultLanguage.getNode("GE").setValue("You can set biome to {BIOMELIST} ");
		defaultLanguage.getNode("GF").setValue("No spawn named 'home' found. Make one with /is setspawn home");
		defaultLanguage.getNode("GG").setValue("You are now the new owner of zone {ZONE}");
		defaultLanguage.getNode("GI").setValue("You're not standing on any zone of your island");
		defaultLanguage.getNode("GJ").setValue("You must be owner of that zone to perform that command");
		defaultLanguage.getNode("GK").setValue("You can't add/remove yourself from the coowners of your zone");
		defaultLanguage.getNode("GL").setValue("{PLAYER} is already coowner of your zone");
		defaultLanguage.getNode("GM").setValue("{PLAYER} was successfully added to the coowners of your zone");
		defaultLanguage.getNode("GN").setValue("{PLAYER} added you to the coowners of zone {ZONE}");
		defaultLanguage.getNode("GO").setValue("{PLAYER} is already not coowner of your zone");
		defaultLanguage.getNode("GP").setValue("{PLAYER} was successfully removed from the coowners of your zone");
		defaultLanguage.getNode("GQ").setValue("{PLAYER} removed you from the coowners of zone {ZONE}");
		defaultLanguage.getNode("GR").setValue("There already is a zone with that name in your island");
		defaultLanguage.getNode("GS").setValue("There is a zone that instersects with your selection");
		defaultLanguage.getNode("GT").setValue("You have successfully created a zone named {ZONE}");
		defaultLanguage.getNode("GU").setValue("You are now the owner of zone {ZONE} inside of your island");
		defaultLanguage.getNode("GV").setValue("You must own this zone to perform that command");
		defaultLanguage.getNode("GW").setValue("Zone {ZONE} has now no owner");
		defaultLanguage.getNode("GX").setValue("You must be standing on your zone to perform that command");
		defaultLanguage.getNode("GY").setValue("You must own this zone to perform that command");
		defaultLanguage.getNode("GZ").setValue("You must specify zone name or stand on it");
		defaultLanguage.getNode("HA").setValue("{ISLAND}'s zones are {ZONELIST}");
		defaultLanguage.getNode("HB").setValue("Player is already owner of the zone");
		defaultLanguage.getNode("HC").setValue("New owner must be part of your island");
		defaultLanguage.getNode("HD").setValue("{PLAYER} is now the new owner of zone {ZONE}");
		defaultLanguage.getNode("HE").setValue("{PLAYER} set you as the owner of zone {ZONE}");
		defaultLanguage.getNode("HF").setValue("Your selection contains a zone of your island");
		defaultLanguage.getNode("HG").setValue("Selected zone is not inside your island's region");
		defaultLanguage.getNode("HH").setValue("You don't have permission to build here");
		defaultLanguage.getNode("HI").setValue("You don't have permission to interact here");
		defaultLanguage.getNode("HJ").setValue("Player is not part of an island");
		defaultLanguage.getNode("HK").setValue("Player is president of his island, use /ia setpres");
		defaultLanguage.getNode("HL").setValue("Success !");
		defaultLanguage.getNode("HM").setValue("You've successfully deleted zone {ZONE} in your island");
		defaultLanguage.getNode("HR").setValue("Your island can't have more than {MAX} spawns");
		defaultLanguage.getNode("HS").setValue("You renamed the zone to {ZONE}");
		defaultLanguage.getNode("HT").setValue("This island is not public");
		defaultLanguage.getNode("HU").setValue("Teleport will start in 2 seconds");
		defaultLanguage.getNode("HV").setValue("Template stored");
		defaultLanguage.getNode("HW").setValue("Unknown Biome");

		defaultLanguage.getNode("IA").setValue("Void");
		defaultLanguage.getNode("IB").setValue("Island");
		defaultLanguage.getNode("IC").setValue("Zone");
		defaultLanguage.getNode("ID").setValue("Biome");
		defaultLanguage.getNode("IG").setValue("Spawn");
		defaultLanguage.getNode("IH").setValue("President");
		defaultLanguage.getNode("II").setValue("Ministers");
		defaultLanguage.getNode("IJ").setValue("Citizens");
		defaultLanguage.getNode("IK").setValue("Permissions");
		defaultLanguage.getNode("IL").setValue("Outsiders");
		defaultLanguage.getNode("IM").setValue("Flags");
		defaultLanguage.getNode("IN").setValue("Owner");
		defaultLanguage.getNode("IO").setValue("Coowners");
		defaultLanguage.getNode("IP").setValue("None");
		defaultLanguage.getNode("IQ").setValue("Unknown");
		defaultLanguage.getNode("IS").setValue("Player");
		defaultLanguage.getNode("IT").setValue("ENABLED");
		defaultLanguage.getNode("IU").setValue("DISABLED");
		defaultLanguage.getNode("IX").setValue("click");
		defaultLanguage.getNode("IY").setValue("Admin");
		defaultLanguage.getNode("IZ").setValue("Zones");
		
		defaultLanguage.getNode("JA").setValue("click here");
		defaultLanguage.getNode("JB").setValue("Island List");
		defaultLanguage.getNode("JC").setValue("World List");
		defaultLanguage.getNode("JD").setValue("BUILD");
		defaultLanguage.getNode("JE").setValue("INTERACT");
		defaultLanguage.getNode("JG").setValue("true");
		defaultLanguage.getNode("JH").setValue("false");
		
		defaultLanguage.getNode("KA").setValue("First position set to {COORD}");
		defaultLanguage.getNode("KB").setValue("Second position set to {COORD}");

	}
	
	public static void load()
	{
		AA = getOrDefault("AA");
		AB = getOrDefault("AB");
		AC = getOrDefault("AC");
		AD = getOrDefault("AD");
		AE = getOrDefault("AE");
		AF = getOrDefault("AF");
		AI = getOrDefault("AI");
		AJ = getOrDefault("AJ");
		AK = getOrDefault("AK");
		AL = getOrDefault("AL");
		AM = getOrDefault("AM");
		AN = getOrDefault("AN");
		AO = getOrDefault("AO");
		AP = getOrDefault("AP");
		AQ = getOrDefault("AQ");
		AR = getOrDefault("AR");
		AS = getOrDefault("AS");
		AU = getOrDefault("AU");
		AW = getOrDefault("AW");
		AX = getOrDefault("AX");
		AZ = getOrDefault("AZ");
		BA = getOrDefault("BA");
		BB = getOrDefault("BB");
		BC = getOrDefault("BC");
		BD = getOrDefault("BD");
		BE = getOrDefault("BE");
		BF = getOrDefault("BF");
		BG = getOrDefault("BG");
		BH = getOrDefault("BH");
		BI = getOrDefault("BI");
		BK = getOrDefault("BK");
		BL = getOrDefault("BL");
		BM = getOrDefault("BM");
		BN = getOrDefault("BN");
		BO = getOrDefault("BO");
		BP = getOrDefault("BP");
		BQ = getOrDefault("BQ");
		BR = getOrDefault("BR");
		BS = getOrDefault("BS");
		BT = getOrDefault("BT");
		BU = getOrDefault("BU");
		BV = getOrDefault("BV");
		BW = getOrDefault("BW");
		BX = getOrDefault("BX");
		BY = getOrDefault("BY");
		BZ = getOrDefault("BZ");
		CA = getOrDefault("CA");
		CB = getOrDefault("CB");
		CC = getOrDefault("CC");
		CD = getOrDefault("CD");
		CE = getOrDefault("CE");
		CF = getOrDefault("CF");
		CG = getOrDefault("CG");
		CH = getOrDefault("CH");
		CI = getOrDefault("CI");
		CJ = getOrDefault("CJ");
		CK = getOrDefault("CK");
		CN = getOrDefault("CN");
		CO = getOrDefault("CO");
		CP = getOrDefault("CP");
		CQ = getOrDefault("CQ");
		CR = getOrDefault("CR");
		CS = getOrDefault("CS");
		CT = getOrDefault("CT");
		CU = getOrDefault("CU");
		CV = getOrDefault("CV");
		CW = getOrDefault("CW");
		CY = getOrDefault("CY");
		CZ = getOrDefault("CZ");
		DQ = getOrDefault("DQ");
		DS = getOrDefault("DS");
		DT = getOrDefault("DT");
		DU = getOrDefault("DU");
		EA = getOrDefault("EA");
		EB = getOrDefault("EB");
		EC = getOrDefault("EC");
		EK = getOrDefault("EK");
		EL = getOrDefault("EL");
		EM = getOrDefault("EM");
		EN = getOrDefault("EN");
		EP = getOrDefault("EP");
		EQ = getOrDefault("EQ");
		ER = getOrDefault("ER");
		ES = getOrDefault("ES");
		ET = getOrDefault("ET");
		EV = getOrDefault("EV");
		EW = getOrDefault("EW");
		EX = getOrDefault("EX");
		EY = getOrDefault("EY");
		EZ = getOrDefault("EZ");
		FA = getOrDefault("FA");
		FB = getOrDefault("FB");
		FC = getOrDefault("FC");
		FD = getOrDefault("FD");
		FE = getOrDefault("FE");
		FF = getOrDefault("FF");
		FG = getOrDefault("FG");
		FH = getOrDefault("FH");
		FI = getOrDefault("FI");
		FJ = getOrDefault("FJ");
		FK = getOrDefault("FK");
		FL = getOrDefault("FL");
		FM = getOrDefault("FM");
		FN = getOrDefault("FN");
		FO = getOrDefault("FO");
		FP = getOrDefault("FP");
		FQ = getOrDefault("FQ");
		FR = getOrDefault("FR");
		FS = getOrDefault("FS");
		FT = getOrDefault("FT");
		FU = getOrDefault("FU");
		FV = getOrDefault("FV");
		FW = getOrDefault("FW");
		FX = getOrDefault("FX");
		FY = getOrDefault("FY");
		FZ = getOrDefault("FZ");
		GA = getOrDefault("GA");
		GB = getOrDefault("GB");
		GC = getOrDefault("GC");
		GD = getOrDefault("GD");
		GE = getOrDefault("GE");
		GF = getOrDefault("GF");
		GG = getOrDefault("GG");
		GI = getOrDefault("GI");
		GJ = getOrDefault("GJ");
		GK = getOrDefault("GK");
		GL = getOrDefault("GL");
		GM = getOrDefault("GM");
		GN = getOrDefault("GN");
		GO = getOrDefault("GO");
		GP = getOrDefault("GP");
		GQ = getOrDefault("GQ");
		GR = getOrDefault("GR");
		GS = getOrDefault("GS");
		GT = getOrDefault("GT");
		GU = getOrDefault("GU");
		GV = getOrDefault("GV");
		GW = getOrDefault("GW");
		GX = getOrDefault("GX");
		GY = getOrDefault("GY");
		GZ = getOrDefault("GZ");
		HA = getOrDefault("HA");
		HB = getOrDefault("HB");
		HC = getOrDefault("HC");
		HD = getOrDefault("HD");
		HE = getOrDefault("HE");
		HF = getOrDefault("HF");
		HG = getOrDefault("HG");
		HH = getOrDefault("HH");
		HI = getOrDefault("HI");
		HJ = getOrDefault("HJ");
		HK = getOrDefault("HK");
		HL = getOrDefault("HL");
		HM = getOrDefault("HM");
		HR = getOrDefault("HR");
		HS = getOrDefault("HS");
		HT = getOrDefault("HT");
		HU = getOrDefault("HU");
		HV = getOrDefault("HV");
		IA = getOrDefault("IA");
		IB = getOrDefault("IB");
		IC = getOrDefault("IC");
		ID = getOrDefault("ID");
		IG = getOrDefault("IG");
		IH = getOrDefault("IH");
		II = getOrDefault("II");
		IJ = getOrDefault("IJ");
		IK = getOrDefault("IK");
		IL = getOrDefault("IL");
		IM = getOrDefault("IM");
		IN = getOrDefault("IN");
		IO = getOrDefault("IO");
		IP = getOrDefault("IP");
		IQ = getOrDefault("IQ");
		IS = getOrDefault("IS");
		IT = getOrDefault("IT");
		IU = getOrDefault("IU");
		IX = getOrDefault("IX");
		IY = getOrDefault("IY");
		IZ = getOrDefault("IZ");
		JA = getOrDefault("JA");
		JB = getOrDefault("JB");
		JC = getOrDefault("JC");
		JD = getOrDefault("JD");
		JE = getOrDefault("JE");
		JG = getOrDefault("JG");
		JH = getOrDefault("JH");

		KA = getOrDefault("KA");
		KB = getOrDefault("KB");
		
		save();
	}
	
	private static String getOrDefault(String key)
	{
		String str = language.getNode(key).getString();
		if (str == null)
		{
			str = defaultLanguage.getNode(key).getString();
			language.getNode(key).setValue(str);
		}
		return str;
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
