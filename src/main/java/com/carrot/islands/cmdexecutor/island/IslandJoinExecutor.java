package com.carrot.islands.cmdexecutor.island;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.object.Island;
import com.carrot.islands.object.Request;

public class IslandJoinExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player guestPlayer = (Player) src;
			if (!ctx.<String>getOne("island").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/is join <island>"));
				return CommandResult.success();
			}
			if (DataHandler.getIslandOfPlayer(guestPlayer.getUniqueId()) != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EK));
				return CommandResult.success();
			}
			String islandName = ctx.<String>getOne("island").get();
			Island island = DataHandler.getIsland(islandName);
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CB));
				return CommandResult.success();
			}
			
			Request req = DataHandler.getJoinRequest(island.getUUID(), guestPlayer.getUniqueId());
			if (req != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FC));
				return CommandResult.success();
			}
			req = DataHandler.getInviteRequest(island.getUUID(), guestPlayer.getUniqueId());
			if (island.getFlag("open") || req != null)
			{
				if (req != null)
				{
					DataHandler.removeInviteRequest(req);
				}
				for (UUID uuid : island.getCitizens())
				{
					Optional<Player> optPlayer = Sponge.getServer().getPlayer(uuid);
					if (optPlayer.isPresent())
						optPlayer.get().sendMessage(Text.of(TextColors.GREEN, LanguageHandler.EY.replaceAll("\\{PLAYER\\}", guestPlayer.getName())));
				}
				island.addCitizen(guestPlayer.getUniqueId());
				DataHandler.resetPlayer(guestPlayer);
				guestPlayer.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.EZ.replaceAll("\\{ISLAND\\}", island.getName())));
				DataHandler.saveIsland(island.getUUID());
				return CommandResult.success();
			}
			ArrayList<UUID> islandStaff = island.getStaff();
			List<Player> islandStaffPlayers = islandStaff
					.stream()
					.filter(uuid -> Sponge.getServer().getPlayer(uuid).isPresent())
					.map(uuid -> Sponge.getServer().getPlayer(uuid).get())
					.collect(Collectors.toList());
			
			if (islandStaffPlayers.isEmpty())
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.FD));
				return CommandResult.success();
			}
			DataHandler.addJoinRequest(new Request(island.getUUID(), guestPlayer.getUniqueId()));
			for (Player p : islandStaffPlayers)
			{
				String str = LanguageHandler.FE.replaceAll("\\{PLAYER\\}", guestPlayer.getName());
				p.sendMessage(Text.builder()
						.append(Text.of(TextColors.AQUA, str.split("\\{CLICKHERE\\}")[0]))
						.append(Text.builder(LanguageHandler.JA)
								.onClick(TextActions.runCommand("/island invite " + guestPlayer.getName()))
								.color(TextColors.DARK_AQUA)
								.build())
						.append(Text.of(TextColors.AQUA, str.split("\\{CLICKHERE\\}")[1])).build());
			}
			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.FB.replaceAll("\\{RECEIVER\\}", islandName)));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
