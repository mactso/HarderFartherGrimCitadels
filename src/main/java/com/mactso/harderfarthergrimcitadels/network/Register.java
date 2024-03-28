package com.mactso.harderfarthergrimcitadels.network;

public class Register {
	public static void initPackets()
	{
//	    Network.registerMessage(SyncRemoveOneGCFromOneClientPacket.class,
//	    		SyncRemoveOneGCFromOneClientPacket::writePacketData,
//	    		SyncRemoveOneGCFromOneClientPacket::readPacketData,
//	    		SyncRemoveOneGCFromOneClientPacket::processSyncRemoveOneGCFromOneClientPacket);

		Network.registerMessage(SyncAllGCWithClientPacket.class,
	    		SyncAllGCWithClientPacket::writePacketData,
	    		SyncAllGCWithClientPacket::readPacketData,
	    		SyncAllGCWithClientPacket::processPacket);

		Network.registerMessage(SyncFogToClientsPacket.class,
				SyncFogToClientsPacket::writePacketData,
				SyncFogToClientsPacket::readPacketData,
				SyncFogToClientsPacket::processPacket);

		Network.registerMessage(SyncDifficultyToClientsPacket.class,
				SyncDifficultyToClientsPacket::writePacketData,
				SyncDifficultyToClientsPacket::readPacketData,
				SyncDifficultyToClientsPacket::processPacket);

		
		Network.registerMessage(GrimClientSongPacket.class,
				GrimClientSongPacket::writePacketData,
				GrimClientSongPacket::readPacketData,
				GrimClientSongPacket::processPacket);
	}
}
