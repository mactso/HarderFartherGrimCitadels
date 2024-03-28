package com.mactso.harderfarthergrimcitadels.events;

import com.mactso.harderfarthergrimcitadels.Main;
import com.mactso.harderfarthergrimcitadels.item.ModItems;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
public class HandleTabSetup {

	@SubscribeEvent
	public static void handleTabSetup (BuildCreativeModeTabContentsEvent event) {

		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(ModItems.BURNISHING_STONE);
			event.accept(ModItems.LIFE_HEART);
		} else if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			event.accept(ModItems.DEAD_BRANCHES);
		}

	}
}
