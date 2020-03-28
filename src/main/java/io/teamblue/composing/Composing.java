package io.teamblue.composing;

import net.fabricmc.api.ModInitializer;

import io.teamblue.composing.item.ComposingItems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Composing implements ModInitializer {
	public static final String MODID = "composing";
	public static final Logger logger = LogManager.getLogger();

	@Override
	public void onInitialize() {
		ComposingItems.init();
	}
}
