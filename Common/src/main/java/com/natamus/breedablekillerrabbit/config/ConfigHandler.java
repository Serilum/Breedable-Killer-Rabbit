package com.natamus.breedablekillerrabbit.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.breedablekillerrabbit.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry(min = 0, max = 1.0) public static double chanceBabyRabbitIsKiller = 0.05;
	@Entry public static boolean removeKillerRabbitNameTag = true;

	public static void initConfig() {
		configMetaData.put("chanceBabyRabbitIsKiller", Arrays.asList(
			"The chance that a baby rabbit is of the killer variant."
		));
		configMetaData.put("removeKillerRabbitNameTag", Arrays.asList(
			"Remove the name 'The Killer Bunny' from the baby killer rabbit."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}