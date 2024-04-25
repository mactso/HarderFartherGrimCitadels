package com.mactso.harderfarthergrimcitadels.proxy;

import com.mactso.harderfarthergrimcitadels.managers.GrimCitadelManager;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public class LocalCoreCalls implements IHarderFartherCoreProxy {

	public static BlockPos CENTER = BlockPos.containing(0,64,0);
	
	public float getDifficulty (LivingEntity le) {

		return GrimCitadelManager.getGrimDifficulty(le);
	}

	@Override
	public void addGrimBlockPosListEntry(BlockPos pos, int range) {
		/* not used in grimCitadels mod */
	}
	@Override
	public void delGrimBlockPosListEntry(BlockPos pos) {
		/* not used in grimCitadels mod */
	}
	
	@Override
	public void addLifeBlockPosListEntry(BlockPos pos, int lifeRange) {
		/* not used in GrimCitadels mod */
	}

	@Override
	public void delLifeBlockPosListEntry(BlockPos pos) {
		/* not used in GrimCitadels mod */
	}

	
	@Override
	public float getDifficulty(BlockPos pos) {
		// return difficulty based on grim citadel list only.
		double maxDistance = 30000.0;  // TASK TODO
		double difficulty = ( CENTER.distManhattan(pos)) / maxDistance ;
		return (float) difficulty;
	}
	

	
}
