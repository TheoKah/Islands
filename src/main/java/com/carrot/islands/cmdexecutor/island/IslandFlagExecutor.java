package com.carrot.islands.cmdexecutor.island;

import static org.spongepowered.api.util.SpongeApiTranslationHelper.t;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;

public class IslandFlagExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			Island island = DataHandler.getIslandOfPlayer(player.getUniqueId());
			if (island == null)
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CI));
				return CommandResult.success();
			}
			if (!island.isStaff(player.getUniqueId()))
			{
				src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CK));
				return CommandResult.success();
			}
			String flag = ctx.<String>getOne("flag").get();
			if (!player.hasPermission("islands.command.island.flag." + flag))
			{
				player.sendMessage(t("You do not have permission to use this command!"));
				return CommandResult.success();
			}
			boolean bool = (ctx.<Boolean>getOne("bool").isPresent()) ? ctx.<Boolean>getOne("bool").get() : !island.getFlag(flag);
			island.setFlag(flag, bool);
			DataHandler.saveIsland(island.getUUID());
			src.sendMessage(Utils.formatIslandDescription(island, Utils.CLICKER_DEFAULT));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.RED, LanguageHandler.CA));
		}
		return CommandResult.success();
	}
}
