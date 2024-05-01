package com.mactso.harderfarthergrimcitadels.events;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraftforge.client.event.ViewportEvent.ComputeFogColor;
import net.minecraftforge.client.event.ViewportEvent.RenderFog;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FogColorsEventHandler {
	
	private static float sliderColorPercent = 1.0f;
	private static float sliderFogThickness = 1.0f;
	private static float sliderStartFogDistance = 1.0f;
	
	private static double RedFromServer = .85f;
	private static double GreenFromServer = 0.2f;
	private static double BlueFromServer = 0.3f;

	// r,g,b should always be 0 to 1.0f
	public static void setFogRGB(double r, double g, double b) {

		RedFromServer = r;
		GreenFromServer = g;
		BlueFromServer = b;
		
		if ((ClientSideDifficultyCache.getGrimDifficulty() == 0)) {
			RedFromServer = r * 0.77;
			GreenFromServer = Math.min(1, g*1.1);
		}

	}

	private long colorTick = 0;

	private long fogTick = 0;

	private int antiSpam = 0;


	private void adjustFogDistance(RenderFog event, float closeFogPercent, float farFogPercent) {

		if ((closeFogPercent < 1) || (farFogPercent < 1)) {
//			if (antiSpam%100 == 0)
//			System.out.println("fogclose%:" + closeFogPercent + " fogfar%:" + farFogPercent  );

			float f1 = RenderSystem.getShaderFogStart();
			float f2 = RenderSystem.getShaderFogEnd();

			f1 = (f1 * closeFogPercent) * farFogPercent;
			f2 *= farFogPercent;

			
			RenderSystem.setShaderFogStart(f1);
			RenderSystem.setShaderFogEnd(f2);
		}

	}

	private float doSlideToPercent(float slider, float target) {
		final double slideAmount = 0.005f;
		if (slider > target+0.005f) {
			slider -= slideAmount;
			// TODO
			System.out.println("Slide down to " + slider);
		} else if (slider < target-0.005f) {
			slider += slideAmount;
			System.out.println("Slide up to " + slider);
		} else {
			slider = target;
		}
		return slider;
	}

	// clientside gui event
	@SubscribeEvent
	public void onFogColorCheck(ComputeFogColor event) {

		Minecraft m = Minecraft.getInstance();
		LocalPlayer cp = m.player;
		long gametick = cp.level().getGameTime();
		// System.out.println ("gametick = " + gametick);
		float percent = ClientSideDifficultyCache.getGrimDifficulty();
		if ((percent > 0) && (percent < 0.1f)){
			percent = 0.1f;
		} 
//		else if (percent > 0.8f) {
//			percent = 0.8f;
//		}

		float modifier = percent ;
        float oldRed = event.getRed() * (1-modifier);
        float oldGreen = event.getGreen() * (1-modifier);
        float oldBlue = event.getBlue() * (1-modifier);

        float pctRed = (float) RedFromServer * modifier;
        float pctGreen = (float) GreenFromServer * modifier; 
        float pctBlue = (float) BlueFromServer * modifier; 

        float newRed = oldRed + pctRed;
        float newGreen = oldGreen+ pctGreen;
        float newBlue = oldBlue + pctBlue;
        
        
//        System.out.println( "evt:" + event.getRed() + ", " + event.getGreen() + " ," + event.getBlue() );
//        System.out.println ("old:" + oldRed + ", " + oldGreen + ", " + oldBlue + " .");
//        System.out.println ("new:" + newRed + ", " + newGreen + ", " + newBlue + " .");
		event.setRed((float) newRed);
		event.setGreen((float) newGreen);
		event.setBlue((float) newBlue);		
	
	}

	// Density of Fog- not Color
	@SubscribeEvent
	public void onFogRender(RenderFog event) {
//		FogMode sky = FogMode.FOG_SKY;
		if (event.getMode() == FogMode.FOG_TERRAIN) {
			Minecraft m = Minecraft.getInstance();
			LocalPlayer cp = m.player;
			long gametick = cp.level().getGameTime();
			if ((fogTick != gametick)) {
				fogTick = gametick;

				float percent = ClientSideDifficultyCache.getGrimDifficulty();

				if ((percent > 0.0f) && (percent < 0.05f)) {
					percent = 0.05f;
				}
				
				if (percent > 0.9) {
					percent -= (percent - 0.8)*2.5;
				}
				percent = Math.max(0, percent);
				percent = Math.min(percent, 1.0f);

				sliderStartFogDistance = doSlideToPercent(sliderStartFogDistance, 1 - percent);
				sliderFogThickness = doSlideToPercent(sliderFogThickness, 1 - percent);
			}


			adjustFogDistance(event, sliderStartFogDistance, sliderFogThickness);

		}

	}

}
