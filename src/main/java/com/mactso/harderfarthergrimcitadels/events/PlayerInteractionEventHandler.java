package com.mactso.harderfarthergrimcitadels.events;

import com.mactso.harderfarthergrimcitadels.managers.GrimCitadelManager;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerInteractionEventHandler {

	// this wasn't being fired at all on water placement.  Not sure why not.
	
	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getEntity();
//		if (player.isCreative())
//			return;
		Level level = player.level();
		if (level.isClientSide)
			return;

		
		if (GrimCitadelManager.isInGrimProtectedArea(event.getPos())) {
			event.setCanceled(true);
		}
	}


}
