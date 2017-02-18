package com.carrot.islands.cmdelement;

import java.util.stream.Collectors;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.biome.BiomeType;

import com.carrot.islands.DataHandler;

public class BiomeNameElement extends PatternMatchingCommandElement
{
	public BiomeNameElement(Text key)
	{
		super(key);
	}
	
	@Override
	protected Iterable<String> getChoices(CommandSource src)
	{
		return DataHandler
				.getBiomes()
				.stream()
				.map(biome -> biome.getName().replace(" ", "_"))
				.collect(Collectors.toList());
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException
	{
		String format = choice.replace("_", " ");
		for (BiomeType biome : DataHandler.getBiomes()) {
			if (biome.getName().equalsIgnoreCase(format))
				return biome;
		}
		return null;
	}

	public Text getUsage(CommandSource src)
	{
		return Text.EMPTY;
	}
}
