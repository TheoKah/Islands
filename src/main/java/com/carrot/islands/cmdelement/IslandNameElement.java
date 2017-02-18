package com.carrot.islands.cmdelement;

import java.util.stream.Collectors;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;

import com.carrot.islands.DataHandler;

public class IslandNameElement extends PatternMatchingCommandElement
{
	public IslandNameElement(Text key)
	{
		super(key);
	}
	
	@Override
	protected Iterable<String> getChoices(CommandSource src)
	{
		return DataHandler
				.getIslands()
				.values()
				.stream()
				.map(nation -> nation.getName())
				.collect(Collectors.toList());
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException
	{
		return choice;
	}

	public Text getUsage(CommandSource src)
	{
		return Text.EMPTY;
	}
}
