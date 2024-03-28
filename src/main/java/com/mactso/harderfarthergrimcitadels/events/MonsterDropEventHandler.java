//package com.mactso.harderfarthergrimcitadels.events;
//
//import java.util.Collection;
//import net.minecraft.util.RandomSource;
//
//import com.mactso.harderfarthergrimcitadels.config.MyConfig;
//import com.mactso.harderfarthergrimcitadels.manager.GrimCitadelManager;
//import com.mactso.harderfarthergrimcitadels.manager.harderfarthergrimcitadelsManager;
//import com.mactso.harderfarthergrimcitadels.manager.LootManager;
//import com.mactso.harderfarthergrimcitadels.timer.CapabilityChunkLastMobDeathTime;
//import com.mactso.harderfarthergrimcitadels.timer.IChunkLastMobDeathTime;
//import com.mactso.harderfarthergrimcitadels.utility.Utility;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.animal.AbstractFish;
//import net.minecraft.world.entity.animal.Animal;
//import net.minecraft.world.entity.animal.WaterAnimal;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.monster.Enemy;
//import net.minecraft.world.entity.monster.Slime;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.chunk.ChunkAccess;
//import net.minecraft.world.level.chunk.LevelChunk;
//import net.minecraftforge.event.entity.living.LivingDropsEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//
//public class MonsterDropEventHandler {
//
//	public static long lastMobDeathTime = 0;
//
//	public static float minCommonDistancePct = 0.01f; // (1% of max distance before common loot)
//	public static float minUncommonDistancePct = 0.1f; // (10% of max distance before uncommon loot)
//	public static float minRareDistancePct = 0.95f; // (95% of max distance before rare loot)
//
//
//
//	
//	
//	private boolean doLimitDropSpeed(ServerLevel serverLevel, Entity eventEntity, BlockPos pos) {
//		long worldTime = serverLevel.getGameTime();
//		ChunkAccess ichunk = serverLevel.getChunk(pos);
//		IChunkLastMobDeathTime cap;
//		boolean cancel = false;
//
//		if (ichunk instanceof LevelChunk chunk) {
//			cap = chunk.getCapability(CapabilityChunkLastMobDeathTime.LASTMOBDEATHTIME).orElse(null);
//			lastMobDeathTime = 0;
//			if (cap != null) {
//				lastMobDeathTime = cap.getLastKillTime();
//				long nextLootTime = lastMobDeathTime + MyConfig.getMobFarmingLimitingTimer();
//				if (worldTime < nextLootTime) {
//					Utility.debugMsg(2, pos,
//							"Mobs Dying Too Quickly at: " + (int) eventEntity.getX() + ", " + (int) eventEntity.getY()
//									+ ", " + (int) eventEntity.getZ() + ", " + " loot and xp denied.  Current Time:"
//									+ worldTime + " nextLoot Time: " + nextLootTime + ".");
//					cancel = true;
//				} else {
//					Utility.debugMsg(1, pos,
//							"Mobs Dropping Loot at : " + (int) eventEntity.getX() + ", " + (int) eventEntity.getY()
//									+ ", " + (int) eventEntity.getZ() + " Current Time:" + worldTime
//									+ " nextLoot Time: " + nextLootTime + ".");
//				}
//				cap.setLastKillTime(worldTime);
//			}
//		}
//		return cancel;
//	}
//
//	
//	@SubscribeEvent  // serverside only.
//	public boolean onMonsterDropsEvent(LivingDropsEvent event) {
//
//		LivingEntity le = event.getEntity();
//		DamageSource dS = event.getSource();
//
//		if (!isDropsSpecialLoot(event, le, dS))
//			return false;
//
//		ServerLevel serverLevel = (ServerLevel) le.level;
//
//		RandomSource rand = serverLevel.getRandom();
//		BlockPos pos = BlockPos.containing(le.getX(), le.getY(), le.getZ());
//
//		// in this section prevent ALL drops if players are killing mobs too quickly.
//
//		boolean cancel = doLimitDropSpeed(serverLevel, le, pos);
//		if (cancel) {
//			event.setCanceled(true);
//			return false;
//		}
//
//		// In this section, give bonus loot
////		loot in lootutility now
//
////		Collection<ItemEntity> eventItems = event.getDrops();
////		boosts in core
////		LootManager.doXPBottleDrop(le, eventItems, rand);
////
////		float boostDifficulty = ma.getDifficultyHere(serverLevel,le);
////		if (boostDifficulty == 0)
////			return false;
////		if (boostDifficulty > MyConfig.getGrimCitadelMaxBoostPercent()) {
////			if (boostDifficulty == GrimCitadelManager.getGrimDifficulty(le)) {
////				boostDifficulty = MyConfig.getGrimCitadelMaxBoostPercent();
////			}
////		}		
//		
//		float odds = 100 + (333 * boostDifficulty);
//		float health = le.getMaxHealth(); // todo debugging
//		int d1000 = (int) (Math.ceil(rand.nextDouble() * 1000));
//
//		if (d1000 > odds) {
//			Utility.debugMsg(1, pos, "No Loot Upgrade: Roll " + d1000 + " odds " + odds);
//			return false;
//		}
//
//		d1000 = (int) (Math.ceil(le.level.getRandom().nextDouble() * 1000));
//		if (d1000 < 640) {
//			d1000 += odds / 10;
//		}
//
//		Mob me = (Mob) event.getEntity();
//		ItemStack itemStackToDrop = LootManager.doGetLootStack(le, me, boostDifficulty, d1000);
//
//		ItemEntity myItemEntity = new ItemEntity(le.level, le.getX(), le.getY(),
//				le.getZ(), itemStackToDrop);
//
//		eventItems.add(myItemEntity);
//
//		
//		Utility.debugMsg(1, pos, le.getName().getString() + " died and dropped loot: "
//				+ itemStackToDrop.getItem().toString());
//		return true;
//	}
//
//
//
//
//	
//	
//	private boolean isDropsSpecialLoot(LivingDropsEvent event, LivingEntity eventEntity, DamageSource dS) {
//
//		if (!(MyConfig.isMakeMonstersHarderFarther()))
//			return false;
//
//		if (!(MyConfig.isUseLootDrops()))
//			return false;
//
//		if (event.getEntity() == null) {
//			return false;
//		}
//
//		if (!(eventEntity.level instanceof ServerLevel)) {
//			return false;
//		}
//
//		// Has to have been killed by a player to drop bonus loot.
//		if ((dS != null) && (dS.getEntity() == null)) {
//			return false;
//		}
//
//		if (!(dS.getEntity() instanceof ServerPlayer)) {
//			return false;
//		}
//
//		if (!(eventEntity instanceof Enemy)) { 
//			return false;
//		}
//
//		if (eventEntity instanceof Slime) {
//			Slime se = (Slime) eventEntity;
//
//			if (se.getSize() < 4) {
//				return false;
//			}
//		}
//
//		if (!(eventEntity instanceof Enemy)) {
//
//			if (eventEntity instanceof AbstractFish) {
//				return false;
//			}
//
//			if (eventEntity instanceof WaterAnimal) {
//				return false;
//			}
//
//			if (eventEntity instanceof Animal) {
//				return false;
//			}
//			
//			return false;
//		}
//
//		return true;
//	}
//
//}
