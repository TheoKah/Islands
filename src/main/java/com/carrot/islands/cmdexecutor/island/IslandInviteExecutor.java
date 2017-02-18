package com.carrot.islands.cmdexecutor.island;

import java.util.Optional;
import java.util.UUID;

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

public class IslandInviteExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player hostPlayer = (Player) src;
			if (!ctx.<Player>getOne("player").isPresent())
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "/is invite <player>"));
				return CommandResult.success();
			}
			Island island = DataHandler.getIslandOfPlayer(hostPlayer.getUniqueId());
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CI));
				return CommandResult.success();
			}
			if (!island.isStaff(hostPlayer.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CK));
				return CommandResult.success();
			}
			Player guestPlayer = ctx.<Player>getOne("player").get();
			
			if (island.isCitizen(guestPlayer.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EW));
				return CommandResult.success();
			}
			
			Request req = DataHandler.getInviteRequest(island.getUUID(), guestPlayer.getUniqueId());
			if (req != null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.EX));
				return CommandResult.success();
			}
			req = DataHandler.getJoinRequest(island.getUUID(), guestPlayer.getUniqueId());
			if (req != null)
			{
				DataHandler.removeJoinRequest(req);
				for (UUID uuid : island.getCitizens())
				{
					Optional<Player> optPlayer = Sponge.getServer().getPlayer(uuid);
					if (optPlayer.isPresent())
						optPlayer.get().sendMessage(Text.of(TextColors.AQUA, LanguageHandler.EY.replaceAll("\\{PLAYER\\}", guestPlayer.getName())));
				}
				island.addCitizen(guestPlayer.getUniqueId());
				guestPlayer.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.EZ.replaceAll("\\{NATION\\}", island.getName())));
				DataHandler.saveIsland(island.getUUID());;
				return CommandResult.success();
			}
			DataHandler.addInviteRequest(new Request(island.getUUID(), guestPlayer.getUniqueId()));

			String str = LanguageHandler.FA.replaceAll("\\{NATION\\}", island.getName());
			guestPlayer.sendMessage(Text.builder()
					.append(Text.of(TextColors.AQUA, str.split("\\{CLICKHERE\\}")[0]))
					.append(Text.builder(LanguageHandler.JA)
							.onClick(TextActions.runCommand("/island join " + island.getName()))
							.color(TextColors.DARK_AQUA)
							.build())
					.append(Text.of(TextColors.AQUA, str.split("\\{CLICKHERE\\}")[1])).build());

			src.sendMessage(Text.of(TextColors.GREEN, LanguageHandler.FB.replaceAll("\\{RECEIVER\\}", guestPlayer.getName())));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
