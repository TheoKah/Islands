package com.carrot.islands.cmdexecutor.island;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.biome.BiomeType;

import com.carrot.islands.DataHandler;
import com.carrot.islands.LanguageHandler;
import com.carrot.islands.Utils;
import com.carrot.islands.object.Island;

public class IslandBiomeExecutor implements CommandExecutor
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
			if (!ctx.<BiomeType>getOne("biome").isPresent())
			{
				src.sendMessage(Text.builder()
						.append(Text.of(TextColors.AQUA, LanguageHandler.GE.split("\\{BIOMELIST\\}")[0]))
						.append(Utils.formatBiomes(island))
						.append(Text.of(TextColors.AQUA, LanguageHandler.GE.split("\\{BIOMELIST\\}")[1]))
						.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX)).build());
				return CommandResult.success();
			}
			BiomeType biome = ctx.<BiomeType>getOne("biome").get();
			if (biome == null) {
				player.sendMessage(Text.of(TextColors.RED, LanguageHandler.CW));
				src.sendMessage(Text.builder()
						.append(Text.of(TextColors.AQUA, LanguageHandler.GE.split("\\{BIOMELIST\\}")[0]))
						.append(Utils.formatBiomes(island))
						.append(Text.of(TextColors.AQUA, LanguageHandler.GE.split("\\{BIOMELIST\\}")[1]))
						.append(Text.of(TextColors.DARK_GRAY, " <- " + LanguageHandler.IX)).build());
				return CommandResult.success();
			}
			player.sendMessage(Text.of(TextColors.AQUA, LanguageHandler.EC));
			island.setBiome(biome);
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
