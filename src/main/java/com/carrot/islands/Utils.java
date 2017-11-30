package com.carrot.islands;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.LiteralText.Builder;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.carrot.islands.object.Island;
import com.carrot.islands.object.Rect;
import com.carrot.islands.object.Zone;
import com.flowpowered.math.vector.Vector3i;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Utils
{
	public static final int CLICKER_NONE = 0;
	public static final int CLICKER_DEFAULT = 1;
	public static final int CLICKER_ADMIN = 2;

	// serialization

	public static String locToString(Location<World> loc)
	{
		return loc.getExtent().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ();
	}
	
	public static void safeTP(Player player, Location<World> loc) {
		while (loc.getBlockY() < 260
				&& loc.getBlock().getType() != BlockTypes.AIR
				&& loc.add(new Vector3i(0, 1, 0)).getBlock().getType() != BlockTypes.AIR
				&& loc.add(new Vector3i(0, 2, 0)).getBlock().getType() != BlockTypes.AIR) {
			loc = loc.add(new Vector3i(0, 1, 0));
		}
		player.setLocation(loc);
	}

	public static Location<World> locFromString(String str)
	{
		String[] splited = str.split(Pattern.quote("|"));
		if (splited.length != 4)
		{
			IslandsPlugin.getLogger().warn("Invalid location format for string " + str);
			return null;
		}
		try
		{
			World world = Sponge.getServer().getWorld(splited[0]).get();
			return world.getLocation(Double.parseDouble(splited[1]), Double.parseDouble(splited[2]), Double.parseDouble(splited[3]));
		}
		catch (NoSuchElementException e)
		{
			IslandsPlugin.getLogger().warn("Invalid location format for string " + str);
		}
		catch (NumberFormatException e)
		{
			IslandsPlugin.getLogger().warn("Invalid location format for string " + str);
		}
		return null;
	}

	public static String rectToString(Rect rect)
	{
		return rect.getMinX() + ";" + rect.getMaxX() + ";" + rect.getMinY() + ";" + rect.getMaxY();
	}

	public static Rect rectFromString(String str)
	{
		String[] splited = str.split(";");
		return new Rect(null, Integer.parseInt(splited[0]), Integer.parseInt(splited[1]), Integer.parseInt(splited[2]), Integer.parseInt(splited[3]));
	}

	// formatting

	public static Text formatIslandDescription(Island island, int clicker)
	{
		Builder builder = Text.builder("");
		builder.append(
				Text.of(TextColors.GOLD, "----------{ "),
				Text.of(TextColors.YELLOW,
						((ConfigHandler.getNode("others", "enableIslandRanks").getBoolean()) 
								? ConfigHandler.getIslandRank(island.getNumCitizens()).getNode("islandTitle").getString()
										: LanguageHandler.IB)
						+ " - " + island.getName()),
				Text.of(TextColors.GOLD, " }----------\n"));

		if (!island.isAdmin())
		{
			builder.append(

					Text.of(TextColors.GOLD, "\n" + LanguageHandler.ID + ": ", Text.builder(island.getBiomeName()).color(TextColors.YELLOW).onClick(TextActions.runCommand("/is biome")).build()),
					Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX),
					Text.of(TextColors.GOLD, "\n" + LanguageHandler.IG + "(", TextColors.YELLOW, + island.getNumSpawns() + "/" + island.getMaxSpawns(), TextColors.GOLD, "): ", TextColors.YELLOW, formatIslandSpawns(island, TextColors.YELLOW, clicker)),
					Text.of(TextColors.GOLD, "\n" + LanguageHandler.IH + ": "),
					citizenClickable(TextColors.YELLOW, DataHandler.getPlayerName(island.getPresident())),
					Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.II + ": "));
			structureX(
					island.getMinisters().iterator(),
					builder,
					(b) -> b.append(Text.of(TextColors.GRAY, LanguageHandler.IP)),
					(b, uuid) -> b.append(citizenClickable(TextColors.YELLOW, DataHandler.getPlayerName(uuid))),
					(b) -> b.append(Text.of(TextColors.YELLOW, ", ")));
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IJ + ": "));
			structureX(
					island.getCitizens().iterator(),
					builder,
					(b) -> b.append(Text.of(TextColors.GRAY, LanguageHandler.IP)),
					(b, uuid) -> b.append(citizenClickable(TextColors.YELLOW, DataHandler.getPlayerName(uuid))),
					(b) -> b.append(Text.of(TextColors.YELLOW, ", ")));
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));
		}
		else
		{
			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IY + ": ", TextColors.GREEN, LanguageHandler.JG));
		}

		if (clicker == CLICKER_NONE)
		{
			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IK + ":\n    " + LanguageHandler.IL + ": "));
			builder.append(Text.of((island.getPerm(Island.TYPE_OUTSIDER, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JD));
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.of((island.getPerm(Island.TYPE_OUTSIDER, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JE));
			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IJ + ": "));
			builder.append(Text.of((island.getPerm(Island.TYPE_CITIZEN, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JD));
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.of((island.getPerm(Island.TYPE_CITIZEN, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JE));

			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IM + ":"));
			for (Entry<String, Boolean> e : island.getFlags().entrySet())
			{
				if (e.getKey().toLowerCase().equals("mobs"))
					continue;
				
				builder.append(Text.of(TextColors.GOLD, "\n    " + StringUtils.capitalize(e.getKey().toLowerCase()) + ": "));
				builder.append(Text.of((e.getValue()) ? TextColors.YELLOW : TextColors.DARK_GRAY, LanguageHandler.IT));
				builder.append(Text.of(TextColors.GOLD, "/"));
				builder.append(Text.of((e.getValue()) ? TextColors.DARK_GRAY : TextColors.YELLOW, LanguageHandler.IU));
			}
		}
		else if (clicker == CLICKER_DEFAULT)
		{
			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IK + ":"));
			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IL + ": "));
			builder.append(Text.builder(LanguageHandler.JD)
					.color((island.getPerm(Island.TYPE_OUTSIDER, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/is perm " + Island.TYPE_OUTSIDER + " " + Island.PERM_BUILD)).build());
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.builder(LanguageHandler.JE)
					.color((island.getPerm(Island.TYPE_OUTSIDER, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/is perm " + Island.TYPE_OUTSIDER + " " + Island.PERM_INTERACT)).build());
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IJ + ": "));
			builder.append(Text.builder(LanguageHandler.JD)
					.color((island.getPerm(Island.TYPE_CITIZEN, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/is perm " + Island.TYPE_CITIZEN + " " + Island.PERM_BUILD)).build());
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.builder(LanguageHandler.JE)
					.color((island.getPerm(Island.TYPE_CITIZEN, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/is perm " + Island.TYPE_CITIZEN + " " + Island.PERM_INTERACT)).build());
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IM + ":"));
			for (Entry<String, Boolean> e : island.getFlags().entrySet())
			{
				if (e.getKey().toLowerCase().equals("mobs"))
					continue;
				builder.append(Text.of(TextColors.GOLD, "\n    " + StringUtils.capitalize(e.getKey().toLowerCase()) + ": "));
				builder.append(Text.builder(LanguageHandler.IT).color((e.getValue()) ? TextColors.YELLOW : TextColors.DARK_GRAY).onClick(TextActions.runCommand("/is flag " + e.getKey() + " true")).build());
				builder.append(Text.of(TextColors.GOLD, "/"));
				builder.append(Text.builder(LanguageHandler.IU).color((e.getValue()) ? TextColors.DARK_GRAY : TextColors.YELLOW).onClick(TextActions.runCommand("/is flag " + e.getKey() + " false")).build());
				builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));
			}
		}
		else if (clicker == CLICKER_ADMIN)
		{
			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IK + ":"));
			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IL + ": "));
			builder.append(Text.builder(LanguageHandler.JD)
					.color((island.getPerm(Island.TYPE_OUTSIDER, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/ia perm " + island.getRealName() + " " + Island.TYPE_OUTSIDER + " " + Island.PERM_BUILD)).build());
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.builder(LanguageHandler.JE)
					.color((island.getPerm(Island.TYPE_OUTSIDER, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/ia perm " + island.getRealName() + " " + Island.TYPE_OUTSIDER + " " + Island.PERM_INTERACT)).build());
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IJ + ": "));
			builder.append(Text.builder(LanguageHandler.JD)
					.color((island.getPerm(Island.TYPE_CITIZEN, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/ia perm " + island.getRealName() + " " + Island.TYPE_CITIZEN + " " + Island.PERM_BUILD)).build());
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.builder(LanguageHandler.JE)
					.color((island.getPerm(Island.TYPE_CITIZEN, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/ia perm " + island.getRealName() + " " + Island.TYPE_CITIZEN + " " + Island.PERM_INTERACT)).build());
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IM + ":"));
			for (Entry<String, Boolean> e : island.getFlags().entrySet())
			{
				if (e.getKey().toLowerCase().equals("mobs"))
					continue;
				builder.append(Text.of(TextColors.GOLD, "\n    " + StringUtils.capitalize(e.getKey().toLowerCase()) + ": "));
				builder.append(Text.builder(LanguageHandler.IT).color((e.getValue()) ? TextColors.YELLOW : TextColors.DARK_GRAY).onClick(TextActions.runCommand("/ia flag " + island.getRealName() + " " + e.getKey() + " true")).build());
				builder.append(Text.of(TextColors.GOLD, "/"));
				builder.append(Text.builder(LanguageHandler.IU).color((e.getValue()) ? TextColors.DARK_GRAY : TextColors.YELLOW).onClick(TextActions.runCommand("/ia flag " + island.getRealName() + " " + e.getKey() + " false")).build());
				builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));
			}
		}

		return builder.build();
	}

	public static Text formatCitizenDescription(String name)
	{
		UUID uuid = DataHandler.getPlayerUUID(name);
		if (uuid == null)
		{
			return Text.of(TextColors.RED, LanguageHandler.IQ);
		}

		Builder builder = Text.builder("");
		builder.append(
				Text.of(TextColors.GOLD, "----------{ "),
				Text.of(TextColors.YELLOW,
						DataHandler.getCitizenTitle(uuid) + " - " + name),
				Text.of(TextColors.GOLD, " }----------")
				);


		builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IB + ": "));
		Island island = DataHandler.getIslandOfPlayer(uuid);
		if (island != null)
		{
			builder.append(islandClickable(TextColors.YELLOW, island.getRealName()));
			if (island.isPresident(uuid))
			{
				builder.append(Text.of(TextColors.YELLOW, " (" + LanguageHandler.IH + ")"));
			}
			else if (island.isMinister(uuid))
			{
				builder.append(Text.of(TextColors.YELLOW, " (" + LanguageHandler.II + ")"));
			}

			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IZ + ": "));
			boolean ownNothing = true;
			for (Zone zone : island.getZones().values())
			{
				if (uuid.equals(zone.getOwner()) && zone.isNamed())
				{
					if (ownNothing)
					{
						ownNothing = false;
					}
					else
					{
						builder.append(Text.of(TextColors.YELLOW, ", "));
					}
					builder.append(zoneClickable(TextColors.YELLOW, zone.getRealName()));
				}
			}
			if (ownNothing)
			{
				builder.append(Text.of(TextColors.GRAY, LanguageHandler.IP));
			}
		}
		else
		{
			builder.append(Text.of(TextColors.GRAY, LanguageHandler.IP));
		}

		return builder.build();
	}

	public static Text formatZoneDescription(Zone zone, Island island, int clicker)
	{
		Builder builder = Text.builder("");
		UUID owner = zone.getOwner();
		builder.append(
				Text.of(TextColors.GOLD, "----------{ "),
				Text.of(TextColors.YELLOW, "" + LanguageHandler.IC + " - " + zone.getName()),
				Text.of(TextColors.GOLD, " }----------"),
				Text.of(TextColors.GOLD, "\n" + LanguageHandler.IB + ": "),
				Text.of(TextColors.YELLOW, island.getName()),
				Text.of(TextColors.GOLD, "\n" + LanguageHandler.IN + ": "),
				(owner == null) ? Text.of(TextColors.GRAY, LanguageHandler.IP) : citizenClickable(TextColors.YELLOW, DataHandler.getPlayerName(owner)),
						Text.of(TextColors.GOLD, "\n" + LanguageHandler.IO + ": ")
				);
		structureX(
				zone.getCoowners().iterator(),
				builder,
				(b) -> b.append(Text.of(TextColors.GRAY, LanguageHandler.IP)),
				(b, uuid) -> b.append(citizenClickable(TextColors.YELLOW, DataHandler.getPlayerName(uuid))),
				(b) -> b.append(Text.of(TextColors.YELLOW, ", ")));


		if (clicker == CLICKER_NONE)
		{
			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IK + ":\n    " + LanguageHandler.IL + ": "));
			builder.append(Text.of((zone.getPerm(Island.TYPE_OUTSIDER, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JD));
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.of((zone.getPerm(Island.TYPE_OUTSIDER, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JE));
			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IJ + ": "));
			builder.append(Text.of((zone.getPerm(Island.TYPE_CITIZEN, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JD));
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.of((zone.getPerm(Island.TYPE_CITIZEN, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JE));
			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IO + ": "));
			builder.append(Text.of((zone.getPerm(Island.TYPE_COOWNER, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JD));
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.of((zone.getPerm(Island.TYPE_COOWNER, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED, LanguageHandler.JE));

			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IM + ":"));
			for (Entry<String, Boolean> e : zone.getFlags().entrySet())
			{
				if (e.getKey().toLowerCase().equals("mobs"))
				continue;
				builder.append(Text.of(TextColors.GOLD, "\n    " + StringUtils.capitalize(e.getKey().toLowerCase()) + ": "));
				builder.append(Text.of((e.getValue()) ? TextColors.YELLOW : TextColors.DARK_GRAY, LanguageHandler.IT));
				builder.append(Text.of(TextColors.GOLD, "/"));
				builder.append(Text.of((e.getValue()) ? TextColors.DARK_GRAY : TextColors.YELLOW, LanguageHandler.IU));
			}
		}
		else if (clicker == CLICKER_DEFAULT)
		{
			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IK + ":"));

			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IL + ": "));
			builder.append(Text.builder(LanguageHandler.JD)
					.color((zone.getPerm(Island.TYPE_OUTSIDER, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/z perm " + Island.TYPE_OUTSIDER + " " + Island.PERM_BUILD)).build());
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.builder(LanguageHandler.JE)
					.color((zone.getPerm(Island.TYPE_OUTSIDER, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/z perm " + Island.TYPE_OUTSIDER + " " + Island.PERM_INTERACT)).build());
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IJ + ": "));
			builder.append(Text.builder(LanguageHandler.JD)
					.color((zone.getPerm(Island.TYPE_CITIZEN, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/z perm " + Island.TYPE_CITIZEN + " " + Island.PERM_BUILD)).build());
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.builder(LanguageHandler.JE)
					.color((zone.getPerm(Island.TYPE_CITIZEN, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/z perm " + Island.TYPE_CITIZEN + " " + Island.PERM_INTERACT)).build());
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

			builder.append(Text.of(TextColors.GOLD, "\n    " + LanguageHandler.IO + ": "));
			builder.append(Text.builder(LanguageHandler.JD)
					.color((zone.getPerm(Island.TYPE_COOWNER, Island.PERM_BUILD)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/z perm " + Island.TYPE_COOWNER + " " + Island.PERM_BUILD)).build());
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.builder(LanguageHandler.JE)
					.color((zone.getPerm(Island.TYPE_COOWNER, Island.PERM_INTERACT)) ? TextColors.GREEN : TextColors.RED)
					.onClick(TextActions.runCommand("/z perm " + Island.TYPE_COOWNER + " " + Island.PERM_INTERACT)).build());
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

			builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IM + ":"));
			for (Entry<String, Boolean> e : zone.getFlags().entrySet())
			{
				if (e.getKey().toLowerCase().equals("mobs"))
					continue;
				builder.append(Text.of(TextColors.GOLD, "\n    " + StringUtils.capitalize(e.getKey().toLowerCase()) + ": "));
				builder.append(Text.builder(LanguageHandler.IT).color((e.getValue()) ? TextColors.YELLOW : TextColors.DARK_GRAY).onClick(TextActions.runCommand("/z flag " + e.getKey() + " true")).build());
				builder.append(Text.of(TextColors.GOLD, "/"));
				builder.append(Text.builder(LanguageHandler.IU).color((e.getValue()) ? TextColors.DARK_GRAY : TextColors.YELLOW).onClick(TextActions.runCommand("/z flag " + e.getKey() + " false")).build());
				builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));
			}
		}

		return builder.build();
	}

	public static Text formatWorldDescription(String name)
	{
		Builder builder = Text.builder("");
		builder.append(
				Text.of(TextColors.GOLD, "----------{ "),
				Text.of(TextColors.YELLOW, name),
				Text.of(TextColors.GOLD, " }----------")
				);

		boolean enabled = ConfigHandler.getNode("worlds").getNode(name).getNode("enabled").getBoolean();

		builder.append(Text.of(TextColors.GOLD, "\nEnabled: "));
		builder.append(Text.builder(LanguageHandler.IT)
				.color((enabled) ? TextColors.YELLOW : TextColors.DARK_GRAY)
				.onClick(TextActions.runCommand("/iw enable " + name)).build());
		builder.append(Text.of(TextColors.GOLD, "/"));
		builder.append(Text.builder(LanguageHandler.IU)
				.color((enabled) ? TextColors.DARK_GRAY : TextColors.YELLOW)
				.onClick(TextActions.runCommand("/iw disable " + name)).build());

		if (!enabled)
		{
			return builder.build();
		}

		builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IK + ": "));

		boolean canBuild = ConfigHandler.getNode("worlds").getNode(name).getNode("perms", "build").getBoolean();
		builder.append(Text.builder(LanguageHandler.JD).color((canBuild) ? TextColors.GREEN : TextColors.RED).onClick(TextActions.runCommand("/iw perm " + Island.PERM_BUILD)).build());

		builder.append(Text.of(TextColors.GOLD, "/"));

		boolean canInteract = ConfigHandler.getNode("worlds").getNode(name).getNode("perms", "interact").getBoolean();
		builder.append(Text.builder(LanguageHandler.JE).color((canInteract) ? TextColors.GREEN : TextColors.RED).onClick(TextActions.runCommand("/iw perm " + Island.PERM_INTERACT)).build());

		builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));

		builder.append(Text.of(TextColors.GOLD, "\n" + LanguageHandler.IM + ":"));
		for (Entry<Object, ? extends CommentedConfigurationNode> e : ConfigHandler.getNode("worlds").getNode(name).getNode("flags").getChildrenMap().entrySet())
		{
			String flag = e.getKey().toString();
			if (flag.toLowerCase().equals("mobs"))
				continue;
			boolean b = e.getValue().getBoolean();
			builder.append(Text.of(TextColors.GOLD, "\n    " + StringUtils.capitalize(flag.toLowerCase()) + ": "));
			builder.append(Text.builder(LanguageHandler.IT).color((b) ? TextColors.YELLOW : TextColors.DARK_GRAY).onClick(TextActions.runCommand("/iw flag " + flag + " true")).build());
			builder.append(Text.of(TextColors.GOLD, "/"));
			builder.append(Text.builder(LanguageHandler.IU).color((b) ? TextColors.DARK_GRAY : TextColors.YELLOW).onClick(TextActions.runCommand("/iw flag " + flag + " false")).build());
			builder.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX));
		}

		return builder.build();
	}

	public static Text formatIslandSpawns(Island island, TextColor color)
	{
		return formatIslandSpawns(island, color, CLICKER_DEFAULT);
	}
	
	public static Text formatIslandSpawns(Island island, TextColor color, int clicker)
	{
		return formatIslandSpawns(island, color, "spawn", clicker);
	}

	public static Text formatIslandSpawns(Island island, TextColor color, String cmd)
	{
		return formatIslandSpawns(island, color, cmd, CLICKER_DEFAULT);
	}

	public static Text formatIslandSpawns(Island island, TextColor color, String cmd, int clicker)
	{
		if (clicker == CLICKER_DEFAULT)
		{
			return structureX(
					island.getSpawns().keySet().iterator(),
					Text.builder(),
					(b) -> b.append(Text.of(TextColors.GRAY, LanguageHandler.IP)),
					(b, spawnName) -> b.append(Text.builder(spawnName).color(color).onClick(TextActions.runCommand("/is " + cmd + " " + spawnName)).build()),
					(b) -> b.append(Text.of(color, ", "))).build();
		}
		if (clicker == CLICKER_ADMIN || island.getFlag("public"))
		{
			return structureX(
					island.getSpawns().keySet().iterator(),
					Text.builder(),
					(b) -> b.append(Text.of(TextColors.GRAY, LanguageHandler.IP)),
					(b, spawnName) -> b.append(Text.builder(spawnName).color(color).onClick(TextActions.runCommand("/is visit " + island.getRealName() + " " + spawnName)).build()),
					(b) -> b.append(Text.of(color, ", "))).build();
		}
		return structureX(
				island.getSpawns().keySet().iterator(),
				Text.builder(),
				(b) -> b.append(Text.of(TextColors.GRAY, LanguageHandler.IP)),
				(b, spawnName) -> b.append(Text.builder(spawnName).color(color).build()),
				(b) -> b.append(Text.of(color, ", "))).build();

	}
	
	public static Text formatBiomes(Island island)
	{
			return structureX(
					DataHandler.getBiomes().iterator(),
					Text.builder(),
					(b) -> b.append(Text.of(TextColors.GRAY, LanguageHandler.IP)),
					(b, biome) -> b.append((biome.getName().equals(island.getBiomeName()) ? Text.builder(biome.getName()).color(TextColors.GREEN).build() : Text.builder(biome.getName()).color(TextColors.YELLOW).onClick(TextActions.runCommand("/is biome " + biome.getName().replace(" ", "_"))).build())),
					(b) -> b.append(Text.of(TextColors.YELLOW, ", "))).build();
	}

	// clickable

	public static Text islandClickable(TextColor color, String name)
	{
		if (name == null)
		{
			return Text.of(color, LanguageHandler.IQ);
		}
		return Text.builder(name.replace("_", " ")).color(color).onClick(TextActions.runCommand("/is info " + name)).build();
	}

	public static Text citizenClickable(TextColor color, String name)
	{
		if (name == null)
		{
			return Text.of(color, LanguageHandler.IQ);
		}
		return Text.builder(name).color(color).onClick(TextActions.runCommand("/is citizen " + name)).build();
	}
	
	public static Text biomeClickable(TextColor color, String name)
	{
		if (name == null)
		{
			return Text.of(color, LanguageHandler.IQ);
		}
		return Text.builder(name).color(color).onClick(TextActions.runCommand("/is biome " + name)).build();
	}

	public static Text zoneClickable(TextColor color, String name)
	{
		if (name == null)
		{
			return Text.of(color, LanguageHandler.IQ);
		}
		return Text.builder(name.replace("_", " ")).color(color).onClick(TextActions.runCommand("/z info " + name)).build();
	}

	public static Text worldClickable(TextColor color, String name)
	{
		if (name == null)
		{
			return Text.of(color, LanguageHandler.IQ);
		}
		return Text.builder(name).color(color).onClick(TextActions.runCommand("/iw info " + name)).build();
	}

	// structure X

	public static <T, U> T structureX(Iterator<U> iter, T obj, Consumer<T> ifNot, BiConsumer<T, U> forEach, Consumer<T> separator)
	{
		if (!iter.hasNext())
		{
			ifNot.accept(obj);
		}
		else
		{
			while (iter.hasNext())
			{
				forEach.accept(obj, iter.next());
				if (iter.hasNext())
				{
					separator.accept(obj);
				}
			}
		}
		return obj;
	}
	
	@SuppressWarnings("serial")
	public static <K, V> Map<K, V> createLRUMap(final int maxEntries) {
	    return new LinkedHashMap<K, V>(maxEntries, 0.75f, true) {
			@Override
	        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
	            return size() > maxEntries;
	        }
	    };
	}
}
