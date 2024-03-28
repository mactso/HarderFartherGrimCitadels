package com.mactso.harderfarthergrimcitadels.events;

import com.mactso.harderfarthergrimcitadels.managers.GrimCitadelManager;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerLoginEventHandler {
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent event) {
		Level level = event.getEntity().level();
		if (level.isClientSide)
			return;
		Player sp = event.getEntity();
		if (sp == null)
			return;
		if (!(sp instanceof ServerPlayer))
			return;

		GrimCitadelManager.sendAllGCPosToClient((ServerPlayer) sp);
//    			SyncFogToClientsPacket msg = new SyncFogToClientsPacket(
//    					MyConfig.getGrimFogRedPercent(),
//    					MyConfig.getGrimFogGreenPercent(),
//    					MyConfig.getGrimFogBluePercent());
//   				Network.sendToClient(msg, (ServerPlayer)sp);
	}

}
