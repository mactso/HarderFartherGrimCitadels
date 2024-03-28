//package com.mactso.harderfarthergrimcitadels.events;
//
//import com.mactso.harderfarthergrimcitadels.config.MyConfig;
//import net.minecraftforge.event.TickEvent.Phase;
//import net.minecraftforge.event.TickEvent.PlayerTickEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//
//public class PlayerTickEventHandler {
//	
//	@SubscribeEvent
//	public void onTick(PlayerTickEvent event) {
//
//		if ((event.phase != Phase.START))
//			return;
//		if (MyConfig.isMakeHarderOverTime()) {
//			HarderTimeManager.doScarySpookyThings(event.player);
//		}
//			
//	}
//}
