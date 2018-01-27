package com.carrot.islands;

import java.io.File;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

import com.carrot.islands.cmdelement.BiomeNameElement;
import com.carrot.islands.cmdelement.CitizenNameElement;
import com.carrot.islands.cmdelement.IslandNameElement;
import com.carrot.islands.cmdelement.PlayerNameElement;
import com.carrot.islands.cmdelement.WorldNameElement;
import com.carrot.islands.cmdexecutor.island.IslandBiomeExecutor;
import com.carrot.islands.cmdexecutor.island.IslandChatExecutor;
import com.carrot.islands.cmdexecutor.island.IslandCitizenExecutor;
import com.carrot.islands.cmdexecutor.island.IslandCreateExecutor;
import com.carrot.islands.cmdexecutor.island.IslandDelspawnExecutor;
import com.carrot.islands.cmdexecutor.island.IslandHelpExecutor;
import com.carrot.islands.cmdexecutor.island.IslandFlagExecutor;
import com.carrot.islands.cmdexecutor.island.IslandHereExecutor;
import com.carrot.islands.cmdexecutor.island.IslandHomeExecutor;
import com.carrot.islands.cmdexecutor.island.IslandInfoExecutor;
import com.carrot.islands.cmdexecutor.island.IslandInviteExecutor;
import com.carrot.islands.cmdexecutor.island.IslandJoinExecutor;
import com.carrot.islands.cmdexecutor.island.IslandKickExecutor;
import com.carrot.islands.cmdexecutor.island.IslandLeaveExecutor;
import com.carrot.islands.cmdexecutor.island.IslandListExecutor;
import com.carrot.islands.cmdexecutor.island.IslandMinisterExecutor;
import com.carrot.islands.cmdexecutor.island.IslandPermExecutor;
import com.carrot.islands.cmdexecutor.island.IslandResignExecutor;
import com.carrot.islands.cmdexecutor.island.IslandSetnameExecutor;
import com.carrot.islands.cmdexecutor.island.IslandSetspawnExecutor;
import com.carrot.islands.cmdexecutor.island.IslandSettagExecutor;
import com.carrot.islands.cmdexecutor.island.IslandSpawnExecutor;
import com.carrot.islands.cmdexecutor.island.IslandVisitExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminCreateExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminDeleteExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminExtraspawnExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminExtraspawnplayerExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminFlagExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminForcejoinExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminForceleaveExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminPermExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminReloadExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminSetnameExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminSetpresExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminSettagExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminSpyExecutor;
import com.carrot.islands.cmdexecutor.islandadmin.IslandadminTemplateExecutor;
import com.carrot.islands.cmdexecutor.islandworld.IslandworldDisableExecutor;
import com.carrot.islands.cmdexecutor.islandworld.IslandworldEnableExecutor;
import com.carrot.islands.cmdexecutor.islandworld.IslandworldExecutor;
import com.carrot.islands.cmdexecutor.islandworld.IslandworldFlagExecutor;
import com.carrot.islands.cmdexecutor.islandworld.IslandworldInfoExecutor;
import com.carrot.islands.cmdexecutor.islandworld.IslandworldListExecutor;
import com.carrot.islands.cmdexecutor.islandworld.IslandworldPermExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneCoownerExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneCreateExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneDeleteExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneDelownerExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneFlagExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneInfoExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneListExecutor;
import com.carrot.islands.cmdexecutor.zone.ZonePermExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneRenameExecutor;
import com.carrot.islands.cmdexecutor.zone.ZoneSetownerExecutor;
import com.carrot.islands.generator.VoidNetherWorldGeneratorModifier;
import com.carrot.islands.listener.BuildPermListener;
import com.carrot.islands.listener.ChatListener;
import com.carrot.islands.listener.ExplosionListener;
import com.carrot.islands.listener.FireListener;
import com.carrot.islands.listener.GoldenAxeListener;
import com.carrot.islands.listener.InteractPermListener;
import com.carrot.islands.listener.MobSpawningListener;
import com.carrot.islands.listener.PlayerConnectionListener;
import com.carrot.islands.listener.PlayerMoveListener;
import com.carrot.islands.listener.PvpListener;
import com.carrot.islands.object.Island;
import com.carrot.islands.service.IslandsService;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

@Plugin(id = "islands", name = "Islands", authors={"Carrot"})
public class IslandsPlugin
{
	private File rootDir;

	private static IslandsPlugin plugin;

	@Inject
	private Logger logger;

	@Inject
	@ConfigDir(sharedRoot = true)
	private File defaultConfigDir;

	@Inject
	private PluginContainer pluginContainer;
	public PluginContainer getPluginContainer() {
		return pluginContainer;
	}

	@Listener
	public void onInit(GameInitializationEvent event)
	{
		plugin = this;

		rootDir = new File(defaultConfigDir, "islands");

		LanguageHandler.init(rootDir);
		ConfigHandler.init(rootDir);
		DataHandler.init(rootDir);
		
		Sponge.getRegistry().register(WorldGeneratorModifier.class , new VoidNetherWorldGeneratorModifier());

		Sponge.getServiceManager().setProvider(this, IslandsService.class, new IslandsService());
	}

	@Listener
	public void onStart(GameStartedServerEvent event)
	{
		LanguageHandler.load();
		ConfigHandler.load();
		DataHandler.load();

		CommandSpec islandadminReloadCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.reload")
				.arguments()
				.executor(new IslandadminReloadExecutor())
				.build();

		CommandSpec islandadminCreateCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.create")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.executor(new IslandadminCreateExecutor())
				.build();

		CommandSpec islandadminSetpresCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.setpres")
				.arguments(
						GenericArguments.optional(new IslandNameElement(Text.of("island"))),
						GenericArguments.optional(new PlayerNameElement(Text.of("president"))))
				.executor(new IslandadminSetpresExecutor())
				.build();

		CommandSpec islandadminSetnameCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.setname")
				.arguments(
						GenericArguments.optional(new IslandNameElement(Text.of("oldname"))),
						GenericArguments.optional(GenericArguments.string(Text.of("newname"))))
				.executor(new IslandadminSetnameExecutor())
				.build();
		
		CommandSpec islandadminSettagCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.settag")
				.arguments(
						GenericArguments.optional(new IslandNameElement(Text.of("island"))),
						GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
				.executor(new IslandadminSettagExecutor())
				.build();

		CommandSpec islandadminForcejoinCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.forcejoin")
				.arguments(
						GenericArguments.optional(new IslandNameElement(Text.of("island"))),
						GenericArguments.optional(new PlayerNameElement(Text.of("player"))))
				.executor(new IslandadminForcejoinExecutor())
				.build();

		CommandSpec islandadminForceleaveCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.forceleave")
				.arguments(GenericArguments.optional(new PlayerNameElement(Text.of("player"))))
				.executor(new IslandadminForceleaveExecutor())
				.build();

		CommandSpec islandadminDeleteCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.delete")
				.arguments(GenericArguments.optional(new IslandNameElement(Text.of("island"))))
				.executor(new IslandadminDeleteExecutor())
				.build();

		CommandSpec islandadminFlagCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.flag")
				.arguments(
						GenericArguments.optional(new IslandNameElement(Text.of("island"))),
						GenericArguments.optional(GenericArguments.choices(Text.of("flag"), ConfigHandler.getNode("islands", "flags")
								.getChildrenMap()
								.keySet()
								.stream()
								.map(key -> key.toString())
								.collect(Collectors.toMap(flag -> flag, flag -> flag)))),
						GenericArguments.optional(GenericArguments.bool(Text.of("bool"))))
				.executor(new IslandadminFlagExecutor())
				.build();

		CommandSpec islandadminPermCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.perm")
				.arguments(
						GenericArguments.optional(new IslandNameElement(Text.of("island"))),
						GenericArguments.optional(GenericArguments.choices(Text.of("type"),
								ImmutableMap.<String, String> builder()
								.put(Island.TYPE_OUTSIDER, Island.TYPE_OUTSIDER)
								.put(Island.TYPE_CITIZEN, Island.TYPE_CITIZEN)
								.put(Island.TYPE_COOWNER, Island.TYPE_COOWNER)
								.build())),
						GenericArguments.optional(GenericArguments.choices(Text.of("perm"),
								ImmutableMap.<String, String> builder()
								.put(Island.PERM_BUILD, Island.PERM_BUILD)
								.put(Island.PERM_INTERACT, Island.PERM_INTERACT)
								.build())),
						GenericArguments.optional(GenericArguments.bool(Text.of("bool"))))
				.executor(new IslandadminPermExecutor())
				.build();

		CommandSpec islandadminTemplateCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.template")
				.arguments()
				.executor(new IslandadminTemplateExecutor())
				.build();

		CommandSpec islandadminSpyCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.spy")
				.arguments()
				.executor(new IslandadminSpyExecutor())
				.build();

		CommandSpec islandadminExtraspawnCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.extraspawn")
				.arguments(
						GenericArguments.optional(GenericArguments.choices(Text.of("give|take|set"),
								ImmutableMap.<String, String> builder()
								.put("give", "give")
								.put("take", "take")
								.put("set", "set")
								.build())),
						GenericArguments.optional(new IslandNameElement(Text.of("island"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
				.executor(new IslandadminExtraspawnExecutor())
				.build();

		CommandSpec islandadminExtraspawnplayerCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.extraspawnplayer")
				.arguments(
						GenericArguments.optional(GenericArguments.choices(Text.of("give|take|set"),
								ImmutableMap.<String, String> builder()
								.put("give", "give")
								.put("take", "take")
								.put("set", "set")
								.build())),
						GenericArguments.optional(new PlayerNameElement(Text.of("player"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
				.executor(new IslandadminExtraspawnplayerExecutor())
				.build();


		CommandSpec islandadminCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandadmin.execute")
				.executor(new IslandadminExecutor())
				.child(islandadminReloadCmd, "reload")
				.child(islandadminCreateCmd, "create")
				.child(islandadminSetpresCmd, "setpres", "setpresident")
				.child(islandadminSetnameCmd, "setname", "rename")
				.child(islandadminSettagCmd, "settag", "tag")
				.child(islandadminForcejoinCmd, "forcejoin")
				.child(islandadminForceleaveCmd, "forceleave")
				.child(islandadminDeleteCmd, "delete")
				.child(islandadminFlagCmd, "flag")
				.child(islandadminPermCmd, "perm")
				.child(islandadminTemplateCmd, "template")
				.child(islandadminSpyCmd, "spy", "spychat")
				.child(islandadminExtraspawnCmd, "extraspawn")
				.child(islandadminExtraspawnplayerCmd, "extraspawnplayer")
				.build();

		CommandSpec islandInfoCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.info")
				.arguments(GenericArguments.optional(new IslandNameElement(Text.of("island"))))
				.executor(new IslandInfoExecutor())
				.build();

		CommandSpec islandHelpCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.help")
				.arguments()
				.executor(new IslandHelpExecutor())
				.build();

		CommandSpec islandHereCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.here")
				.arguments()
				.executor(new IslandHereExecutor())
				.build();

		CommandSpec islandListCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.list")
				.arguments()
				.executor(new IslandListExecutor())
				.build();

		CommandSpec islandCitizenCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.citizen")
				.arguments(GenericArguments.optional(new PlayerNameElement(Text.of("player"))))
				.executor(new IslandCitizenExecutor())
				.build();

		CommandSpec islandCreateCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.create")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.executor(new IslandCreateExecutor())
				.build();


		CommandSpec islandInviteCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.invite")
				.arguments(GenericArguments.optional(GenericArguments.player(Text.of("player"))))
				.executor(new IslandInviteExecutor())
				.build();

		CommandSpec islandJoinCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.join")
				.arguments(GenericArguments.optional(new IslandNameElement(Text.of("island"))))
				.executor(new IslandJoinExecutor())
				.build();

		CommandSpec islandKickCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.kick")
				.arguments(GenericArguments.optional(new CitizenNameElement(Text.of("player"))))
				.executor(new IslandKickExecutor())
				.build();

		CommandSpec islandLeaveCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.leave")
				.arguments()
				.executor(new IslandLeaveExecutor())
				.build();

		CommandSpec islandResignCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.resign")
				.arguments(GenericArguments.optional(new CitizenNameElement(Text.of("successor"))))
				.executor(new IslandResignExecutor())
				.build();

		CommandSpec islandSpawnCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.spawn")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.executor(new IslandSpawnExecutor())
				.build();

		CommandSpec islandHomeCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.home")
				.arguments()
				.executor(new IslandHomeExecutor())
				.build();

		CommandSpec islandSetspawnCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.setspawn")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.executor(new IslandSetspawnExecutor())
				.build();

		CommandSpec islandDelspawnCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.delspawn")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.executor(new IslandDelspawnExecutor())
				.build();

		CommandSpec islandSetnameCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.setname")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.executor(new IslandSetnameExecutor())
				.build();

		CommandSpec islandSettagCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.settag")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
				.executor(new IslandSettagExecutor())
				.build();

		CommandSpec islandMinisterCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.minister")
				.arguments(
						GenericArguments.optional(GenericArguments.choices(Text.of("add|remove"),
								ImmutableMap.<String, String> builder()
								.put("add", "add")
								.put("remove", "remove")
								.build())),
						GenericArguments.optional(new CitizenNameElement(Text.of("citizen"))))
				.executor(new IslandMinisterExecutor())
				.build();

		CommandSpec islandPermCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.perm")
				.arguments(
						GenericArguments.choices(Text.of("type"),
								ImmutableMap.<String, String> builder()
								.put(Island.TYPE_OUTSIDER, Island.TYPE_OUTSIDER)
								.put(Island.TYPE_CITIZEN, Island.TYPE_CITIZEN)
								.build()),
						GenericArguments.choices(Text.of("perm"),
								ImmutableMap.<String, String> builder()
								.put(Island.PERM_BUILD, Island.PERM_BUILD)
								.put(Island.PERM_INTERACT, Island.PERM_INTERACT)
								.build()),
						GenericArguments.optional(GenericArguments.bool(Text.of("bool"))))
				.executor(new IslandPermExecutor())
				.build();

		CommandSpec islandFlagCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.flag")
				.arguments(
						GenericArguments.choices(Text.of("flag"), ConfigHandler.getNode("islands", "flags")
								.getChildrenMap()
								.keySet()
								.stream()
								.map(key -> key.toString())
								.collect(Collectors.toMap(flag -> flag, flag -> flag))),
						GenericArguments.optional(GenericArguments.bool(Text.of("bool"))))
				.executor(new IslandFlagExecutor())
				.build();

		CommandSpec islandChatCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.chat")
				.arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("msg"))))
				.executor(new IslandChatExecutor())
				.build();

		CommandSpec islandVisitCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.visit")
				.arguments(
						GenericArguments.optional(new IslandNameElement(Text.of("island"))),
						GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.executor(new IslandVisitExecutor())
				.build();

		CommandSpec islandBiomeCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.biome")
				.arguments(GenericArguments.optional(new BiomeNameElement(Text.of("biome"))))
				.executor(new IslandBiomeExecutor())
				.build();

		CommandSpec islandCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.island.execute")
				.executor(new IslandInfoExecutor())
				.child(islandInfoCmd, "info")
				.child(islandHelpCmd, "help", "?")
				.child(islandHereCmd, "here", "h")
				.child(islandListCmd, "list", "l")
				.child(islandCitizenCmd, "citizen", "whois")
				.child(islandCreateCmd, "create", "new")
				.child(islandInviteCmd, "invite", "add")
				.child(islandJoinCmd, "join", "apply")
				.child(islandKickCmd, "kick")
				.child(islandLeaveCmd, "leave", "quit")
				.child(islandResignCmd, "resign")
				.child(islandSetnameCmd, "setname", "rename")
				.child(islandSettagCmd, "settag", "tag")
				.child(islandSpawnCmd, "spawn")
				.child(islandHomeCmd, "home")
				.child(islandSetspawnCmd, "setspawn")
				.child(islandDelspawnCmd, "delspawn")
				.child(islandMinisterCmd, "minister")
				.child(islandPermCmd, "perm")
				.child(islandFlagCmd, "flag")
				.child(islandChatCmd, "chat", "c", "islandchat", "ic")
				.child(islandVisitCmd, "visit")
				.child(islandBiomeCmd, "biome")
				.build();

		CommandSpec zoneInfoCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.info")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("zone"))))
				.executor(new ZoneInfoExecutor())
				.build();

		CommandSpec zoneListCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.list")
				.arguments(GenericArguments.optional(new IslandNameElement(Text.of("island"))))
				.executor(new ZoneListExecutor())
				.build();

		CommandSpec zoneCreateCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.create")
				.arguments(
						GenericArguments.optional(GenericArguments.string(Text.of("name"))),
						GenericArguments.optional(new PlayerNameElement(Text.of("owner"))))
				.executor(new ZoneCreateExecutor())
				.build();

		CommandSpec zoneDeleteCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.delete")
				.arguments()
				.executor(new ZoneDeleteExecutor())
				.build();

		CommandSpec zoneCoownerCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.coowner")
				.arguments(
						GenericArguments.optional(GenericArguments.choices(Text.of("add|remove"),
								ImmutableMap.<String, String> builder()
								.put("add", "add")
								.put("remove", "remove")
								.build())),
						GenericArguments.optional(new PlayerNameElement(Text.of("citizen"))))
				.executor(new ZoneCoownerExecutor())
				.build();

		CommandSpec zoneSetownerCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.setowner")
				.arguments(GenericArguments.optional(new PlayerNameElement(Text.of("owner"))))
				.executor(new ZoneSetownerExecutor())
				.build();

		CommandSpec zoneDelownerCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.delowner")
				.arguments()
				.executor(new ZoneDelownerExecutor())
				.build();

		CommandSpec zoneRenameCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.rename")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.executor(new ZoneRenameExecutor())
				.build();

		CommandSpec zonePermCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.perm")
				.arguments(
						GenericArguments.choices(Text.of("type"),
								ImmutableMap.<String, String> builder()
								.put(Island.TYPE_OUTSIDER, Island.TYPE_OUTSIDER)
								.put(Island.TYPE_CITIZEN, Island.TYPE_CITIZEN)
								.put(Island.TYPE_COOWNER, Island.TYPE_COOWNER)
								.build()),
						GenericArguments.choices(Text.of("perm"),
								ImmutableMap.<String, String> builder()
								.put(Island.PERM_BUILD, Island.PERM_BUILD)
								.put(Island.PERM_INTERACT, Island.PERM_INTERACT)
								.build()),
						GenericArguments.optional(GenericArguments.bool(Text.of("bool"))))
				.executor(new ZonePermExecutor())
				.build();

		CommandSpec zoneFlagCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.flag")
				.arguments(
						GenericArguments.choices(Text.of("flag"), ConfigHandler.getNode("islands", "flags")
								.getChildrenMap()
								.keySet()
								.stream()
								.map(o -> o.toString())
								.collect(Collectors.toMap(flag -> flag, flag -> flag))),
						GenericArguments.optional(GenericArguments.bool(Text.of("bool"))))
				.executor(new ZoneFlagExecutor())
				.build();

		CommandSpec zoneCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.zone.execute")
				.executor(new ZoneExecutor())
				.child(zoneInfoCmd, "info")
				.child(zoneListCmd, "list")
				.child(zoneCreateCmd, "create", "add")
				.child(zoneDeleteCmd, "delete", "remove")
				.child(zoneCoownerCmd, "coowner")
				.child(zoneSetownerCmd, "setowner")
				.child(zoneDelownerCmd, "delowner")
				.child(zoneRenameCmd, "rename")
				.child(zonePermCmd, "perm")
				.child(zoneFlagCmd, "flag")
				.build();

		CommandSpec worldInfoCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandworld.info")
				.arguments(GenericArguments.optional(new WorldNameElement(Text.of("world"))))
				.executor(new IslandworldInfoExecutor())
				.build();

		CommandSpec worldListCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandworld.list")
				.arguments()
				.executor(new IslandworldListExecutor())
				.build();

		CommandSpec worldEnableCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandworld.enable")
				.arguments(GenericArguments.optional(new WorldNameElement(Text.of("world"))))
				.executor(new IslandworldEnableExecutor())
				.build();

		CommandSpec worldDisableCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandworld.disable")
				.arguments(GenericArguments.optional(new WorldNameElement(Text.of("world"))))
				.executor(new IslandworldDisableExecutor())
				.build();

		CommandSpec worldPermCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandworld.perm")
				.arguments(
						GenericArguments.choices(Text.of("perm"),
								ImmutableMap.<String, String> builder()
								.put(Island.PERM_BUILD, Island.PERM_BUILD)
								.put(Island.PERM_INTERACT, Island.PERM_INTERACT)
								.build()),
						GenericArguments.optional(GenericArguments.bool(Text.of("bool"))))
				.executor(new IslandworldPermExecutor())
				.build();

		CommandSpec worldFlagCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandworld.flag")
				.arguments(
						GenericArguments.choices(Text.of("flag"), ConfigHandler.getNode("islands", "flags")
								.getChildrenMap()
								.keySet()
								.stream()
								.map(o -> o.toString())
								.collect(Collectors.toMap(flag -> flag, flag -> flag))),
						GenericArguments.optional(GenericArguments.bool(Text.of("bool"))))
				.executor(new IslandworldFlagExecutor())
				.build();

		CommandSpec debugFlagCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandworld.debug")
				.arguments(
						GenericArguments.optional(GenericArguments.choices(Text.of("start|stop"),
								ImmutableMap.<String, String> builder()
								.put("start", "start")
								.put("stop", "stop")
								.build())))
				.executor(new Debugger.CmdExecutor())
				.build();

		CommandSpec islandworldCmd = CommandSpec.builder()
				.description(Text.of(""))
				.permission("islands.command.islandworld.execute")
				.executor(new IslandworldExecutor())
				.child(worldInfoCmd, "info")
				.child(worldListCmd, "list")
				.child(worldEnableCmd, "enable")
				.child(worldDisableCmd, "disable")
				.child(worldPermCmd, "perm")
				.child(worldFlagCmd, "flag")
				.child(debugFlagCmd, "debug")
				.build();

		Sponge.getCommandManager().register(this, islandadminCmd, "islandadmin", "ia", "islandsadmin", "iadmin");
		Sponge.getCommandManager().register(this, islandCmd, "island", "is", "islands", "i");
		Sponge.getCommandManager().register(this, zoneCmd, "zone", "z");
		Sponge.getCommandManager().register(this, islandworldCmd, "islandworld", "iw", "iworld");

		Sponge.getEventManager().registerListeners(this, new PlayerConnectionListener());
		Sponge.getEventManager().registerListeners(this, new PlayerMoveListener());
		Sponge.getEventManager().registerListeners(this, new GoldenAxeListener());
		Sponge.getEventManager().registerListeners(this, new PvpListener());
		Sponge.getEventManager().registerListeners(this, new FireListener());
		Sponge.getEventManager().registerListeners(this, new ExplosionListener());
		Sponge.getEventManager().registerListeners(this, new MobSpawningListener());
		Sponge.getEventManager().registerListeners(this, new BuildPermListener());
		Sponge.getEventManager().registerListeners(this, new InteractPermListener());
		Sponge.getEventManager().registerListeners(this, new ChatListener());

		logger.info("Plugin ready");
	}

	@Listener
	public void onServerStopping(GameStoppingServerEvent event)
	{
		logger.info("Saving data");
		ConfigHandler.save();
		DataHandler.save();
		logger.info("Plugin stopped");
	}

	public static IslandsPlugin getInstance()
	{
		return plugin;
	}

	public static Logger getLogger()
	{
		return getInstance().logger;
	}

	public static Cause getCause()
	{
		return Sponge.getCauseStackManager().getCurrentCause();
	}
}
