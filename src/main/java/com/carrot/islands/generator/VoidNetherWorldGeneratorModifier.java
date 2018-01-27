package com.carrot.islands.generator;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;

public class VoidNetherWorldGeneratorModifier implements WorldGeneratorModifier {

	@Override
	public String getId() {
		return "carrot:voidnether";
	}

	@Override
	public String getName() {
		return "Carrot's Void Nether";
	}

	@Override
	public void modifyWorldGenerator(WorldProperties world, DataContainer settings, WorldGenerator worldGenerator) {
		worldGenerator.setBaseGenerationPopulator((world1, buffer, biomes) -> { });
	}

}
