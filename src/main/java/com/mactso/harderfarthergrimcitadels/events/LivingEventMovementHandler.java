package com.mactso.harderfarthergrimcitadels.events;

import com.mactso.harderfarthergrimcitadels.Main;
import com.mactso.harderfarthergrimcitadels.client.GrimSongManager;
import com.mactso.harderfarthergrimcitadels.config.MyConfig;
import com.mactso.harderfarthergrimcitadels.item.ModItems;
import com.mactso.harderfarthergrimcitadels.managers.GrimCitadelManager;
import com.mactso.harderfarthergrimcitadels.network.GrimClientSongPacket;
import com.mactso.harderfarthergrimcitadels.network.Network;
import com.mactso.harderfarthergrimcitadels.network.SyncDifficultyToClientsPacket;
import com.mactso.harderfarthergrimcitadels.sounds.ModSounds;
import com.mactso.harderfarthergrimcitadels.utility.Glooms;
import com.mactso.harderfarthergrimcitadels.utility.Utility;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;;

@Mod.EventBusSubscriber()
public class LivingEventMovementHandler {

	/**
	 * @param event
	 */
	@SubscribeEvent
	public static void onLivingUpdate(LivingTickEvent event) {

		LivingEntity le = event.getEntity();
		RandomSource rand = le.level().getRandom();

		if (le.level().isClientSide()) {
			if (le instanceof Player cp) {
				GrimCitadelManager.playGCOptionalSoundCues(cp);
			}
			if ((le instanceof Player cp) && (GrimCitadelManager.isInRangeOfGC(le.blockPosition()))
					&& (rand.nextInt(144000) == 42)) {
				GrimSongManager.startSong(ModSounds.NUM_LAKE_DESTINY);
			}
			return;
		}

		ServerLevel serverLevel = (ServerLevel) le.level();

		if (le instanceof ServerPlayer sp) {
			Utility.debugMsg(2, "LivingEventMovementHandler");
			checkLifeHeart(sp);
			long gameTime = serverLevel.getGameTime();
			if (gameTime % 10 != le.getId() % 10) // only need to check every half second.
				return;
			
			
			// TODO Get Difficulty from Proxy.  Get difficulty from GrimManager
			// Update difficulty cache on Client.
			int x=3;
			float difficulty = Main.difficultyCallProxy.getDifficulty(le.blockPosition());
			float grimDifficulty = GrimCitadelManager.getGrimDifficulty(le);
			float timeDifficulty = 0.0f;  // zero as far as grim citadel manager knows.

			if (le instanceof ServerPlayer ) {
				SyncDifficultyToClientsPacket msg = new SyncDifficultyToClientsPacket(difficulty,grimDifficulty,timeDifficulty);
				Network.sendToClient(msg, sp);
			}

			
			if (difficulty > 0) {
				if (GrimCitadelManager.isInRangeOfGC(le.blockPosition())) {
					if ((difficulty > Utility.Pct95)) {
						Utility.slowFlyingMotion(le);
					}
				}



				if ((difficulty > Utility.Pct84)) {
					if (sp.hasEffect(MobEffects.SLOW_FALLING)) {
						sp.removeEffect(MobEffects.SLOW_FALLING);
					}
				}

				Utility.debugMsg(2, le, "Living Event " + EntityType.getKey((event.getEntity().getType())).toString()
						+ " dif: " + difficulty);
				
				if (Main.difficultyCallProxy.getDifficulty(le.blockPosition()) > 0) {
					if (GrimCitadelManager.isInRangeOfGC(le.blockPosition())) {
						Glooms.doGlooms(serverLevel, gameTime, difficulty, le, Glooms.GRIM);
						if ((le instanceof ServerPlayer) && (rand.nextInt(144000) == 4242)
								&& (difficulty > Utility.Pct09)) {
							Network.sendToClient(new GrimClientSongPacket(ModSounds.NUM_DUSTY_MEMORIES), sp);
						}
					}
				}

			}

		}

	}

	private static void checkLifeHeart(ServerPlayer sp) {

		boolean hasLifeHeart = sp.getInventory().contains(ModItems.LIFE_HEART_STACK);

		if ((sp.getHealth() < sp.getMaxHealth()) && (hasLifeHeart)) {
			int dice = MyConfig.getGrimLifeheartPulseSeconds() * Utility.TICKS_PER_SECOND;
			int roll = sp.level().getRandom().nextInt(dice);
			int duration = Utility.FOUR_SECONDS;
			if (roll == 42) { // once per 2 minutes

				int slot = sp.getInventory().findSlotMatchingItem(ModItems.LIFE_HEART_STACK);
				int row = slot / 9;
				float volume = 0.0f;
				int healingpower = 0;
				int healingduration = 0;

				switch (row) {
				case 0: // Hotbar
					volume = 0.48f;
					healingpower = 3;
					healingduration = Utility.FOUR_SECONDS;
					break;
				case 3: // 3rd Inventory Row
					volume = 0.36f;
					healingpower = 2;
					healingduration = Utility.FOUR_SECONDS * 2;
					break;
				case 2: // 2nd Inventory Row
					volume = 0.24f;
					healingpower = 2;
					healingduration = Utility.FOUR_SECONDS;
					break;
				case 1: // 1st Inventory Row
				default:
					volume = 0.12f;
					healingpower = 3;
					healingduration = Utility.FOUR_SECONDS * 3;
					break;
				}

				sp.level().playSound(null, sp.blockPosition(), SoundEvents.NOTE_BLOCK_CHIME.get(),
						SoundSource.PLAYERS, volume, 0.86f);
				Utility.updateEffect((LivingEntity) sp, healingpower, MobEffects.REGENERATION,
						Utility.FOUR_SECONDS);
			}

		}
	}

}
