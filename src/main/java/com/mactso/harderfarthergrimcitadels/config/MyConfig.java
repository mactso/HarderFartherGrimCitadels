package com.mactso.harderfarthergrimcitadels.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//16.2 - 1.0.0.0 HarderFarther

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.harderfarthergrimcitadels.Main;

import net.minecraft.core.BlockPos;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig {


	private static final Logger LOGGER = LogManager.getLogger();
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	
	static
	{
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static int getDebugLevel() {
		return debugLevel;
	}

	public static void setDebugLevel(int newValue) {
		if (newValue <0 || newValue > 2) // TODO: this should be redundant 
			newValue = 0;
		 debugLevel = newValue;
	}

	
	public static boolean isUsingCore() {
		return usingCore;
	}

	
	public static void setUsingCore(boolean usingCore) {
		MyConfig.usingCore = usingCore;
	}

	
	public static int getGrimCitadelsRadius() {
		return grimCitadelsRadius;
	}

	
	public static void setGrimCitadelsRadius(int grimCitadelsRadius) {
		MyConfig.grimCitadelsRadius = grimCitadelsRadius;
		COMMON.grimCitadelsRadius.set(grimCitadelsRadius);
	}

	public static int getGrimCitadelMaxBoostValue() {
		return grimCitadelMaxBoostPercent;
	}

	public static float getGrimCitadelMaxBoostPercent() {
		return (float)(grimCitadelMaxBoostPercent)/100;
	}
	
	public static void setGrimCitadelMaxBoostPercent(int newValue) {
		MyConfig.grimCitadelMaxBoostPercent = newValue;
		COMMON.grimCitadelMaxBoostPercent.set(newValue);
	}
	
	public static int getGrimCitadelsCount() {
		return grimCitadelsCount;
	}

	public static int getGrimCitadelBonusDistance() {
		return grimCitadelBonusDistance;
	}
	
	public static int getGrimCitadelBonusDistanceSq() {
		return grimCitadelBonusDistanceSq;
	}
	
	public static int getGrimCitadelPlayerCurseDistance() {
		return grimCitadelPlayerCurseDistance;
	}

	public static int getGrimCitadelPlayerCurseDistanceSq() {
		return grimCitadelPlayerCurseDistanceSq;
	}

	public static List<BlockPos> getGrimCitadelsBlockPosList() {
		return grimCitadelsBlockPosList;
	}

	public static void setGrimCitadelsBlockPosList(List<BlockPos> grimCitadelsBlockPosList) {
		MyConfig.grimCitadelsBlockPosList = grimCitadelsBlockPosList;
	}
	
	public static boolean isGrimEffectTrees() {
		return grimEffectTrees;
	}

	public static void setGrimEffectTrees(boolean grimEffectTrees) {
		MyConfig.grimEffectTrees = grimEffectTrees;
	}

	public static boolean isGrimEffectAnimals() {
		return grimEffectAnimals;
	}
	
	public static boolean isGrimEffectPigs() {
		return grimEffectPigs;
	}

	public static boolean isGrimEffectVillagers() {
		return grimEffectVillagers;
	}
	
	public static int getGrimLifeheartPulseSeconds() {
		return grimLifeheartPulseSeconds;
	}

	public static double getGrimFogRedPercent() {
		return grimFogRedPercent;
	}

	public static double getGrimFogBluePercent() {
		return grimFogBluePercent;
	}

	public static double getGrimFogGreenPercent() {
		return grimFogGreenPercent;
	}
	
	public static void setGrimFogRedPercent(double grimFogRedPercent) {
		MyConfig.grimFogRedPercent = grimFogRedPercent/100;
		COMMON.grimFogRedPercent.set(grimFogRedPercent/100);
	}
	public static void setGrimFogGreenPercent(double grimFogGreenPercent) {
		MyConfig.grimFogGreenPercent = grimFogGreenPercent/100;
		COMMON.grimFogGreenPercent.set(grimFogGreenPercent/100);
	}

	public static void setGrimFogBluePercent(double grimFogBluePercent) {
		MyConfig.grimFogBluePercent = grimFogBluePercent/100;
		COMMON.grimFogBluePercent.set(grimFogBluePercent/100);
	}

	private static int      debugLevel;
	private static boolean  usingCore;

	private static boolean  useGrimCitadels;
	private static int      grimCitadelsRadius;
	private static int      grimCitadelsCount;
	private static int 		grimCitadelMaxBoostPercent;
	private static int 	    grimCitadelBonusDistance;
	private static int 	    grimCitadelBonusDistanceSq;
	private static int 		grimCitadelPlayerCurseDistance;
	private static int 		grimCitadelPlayerCurseDistanceSq;

	private static boolean  grimEffectTrees;
	private static boolean  grimEffectAnimals;
	private static boolean  grimEffectPigs;
	private static boolean  grimEffectVillagers;
	private static int      grimLifeheartPulseSeconds;
	
	private static double 	grimFogRedPercent;
	private static double 	grimFogGreenPercent;
	private static double 	grimFogBluePercent;
	
	private static List<? extends String> grimCitadelsList;
	private static List<BlockPos> grimCitadelsBlockPosList;
	
	private static int      minimumSafeAltitude;
	private static int      maximumSafeAltitude;
	public static final int KILLER_ANY   = 0;
	public static final int KILLER_MOB_OR_PLAYER = 1;
	public static final int KILLER_PLAYER = 2;

	@SubscribeEvent
	public static <ModConfig> void onModConfigEvent(final ModConfigEvent configEvent)
	{

		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC)
		{
			bakeConfig();
		}
	}	


	public static void pushValues() {
		COMMON.debugLevel.set(debugLevel);

		COMMON.useGrimCitadels.set(useGrimCitadels);
		COMMON.grimCitadelsRadius.set(grimCitadelsRadius);
		COMMON.grimCitadelsCount.set(grimCitadelsCount);
		COMMON.grimCitadelsList.set(grimCitadelsList);
		COMMON.grimCitadelMaxBoostPercent.set(grimCitadelMaxBoostPercent);
		COMMON.grimCitadelBonusDistance.set(grimCitadelBonusDistance);
		COMMON.grimCitadelPlayerCurseDistance.set(grimCitadelPlayerCurseDistance);
		
		COMMON.grimEffectTrees.set(grimEffectTrees);
		COMMON.grimEffectAnimals.set(grimEffectAnimals);
		COMMON.grimEffectPigs.set(grimEffectPigs);
		COMMON.grimEffectVillagers.set(grimEffectVillagers);
		COMMON.grimLifeheartPulseSeconds.set(grimLifeheartPulseSeconds);
		
		COMMON.grimFogRedPercent.set (grimFogRedPercent);
		COMMON.grimFogBluePercent.set (grimFogBluePercent);
		COMMON.grimFogGreenPercent.set (grimFogGreenPercent);
	}
	
	public static void setUseGrimCitadels(boolean newValue) {
		COMMON.useGrimCitadels.set(newValue);
		useGrimCitadels = COMMON.useGrimCitadels.get();
	}

	
	public static void setBonusRange(int newRange) {
		COMMON.grimCitadelBonusDistance.set(newRange);
		COMMON.grimCitadelPlayerCurseDistance.set((int)(newRange*0.7f));
		bakeGrimRanges();
	}

	private static void bakeGrimRanges() {
		grimCitadelBonusDistance = COMMON.grimCitadelBonusDistance.get();
		grimCitadelBonusDistanceSq = grimCitadelBonusDistance*grimCitadelBonusDistance;
		grimCitadelPlayerCurseDistance = COMMON.grimCitadelPlayerCurseDistance.get();
		grimCitadelPlayerCurseDistanceSq = grimCitadelPlayerCurseDistance * grimCitadelPlayerCurseDistance;
	}

	// remember need to push each of these values separately once we have commands.
	// this copies file changes into the running program variables.
	
	public static void bakeConfig()
	{
		debugLevel = COMMON.debugLevel.get();
		
		grimCitadelsBlockPosList = getBlockPositions(COMMON.grimCitadelsList.get());
		grimCitadelsList = COMMON.grimCitadelsList.get();
		grimCitadelsCount = COMMON.grimCitadelsCount.get();
		grimCitadelsRadius= COMMON.grimCitadelsRadius.get();
		grimCitadelMaxBoostPercent = COMMON.grimCitadelMaxBoostPercent.get();
		bakeGrimRanges();

		grimEffectTrees = COMMON.grimEffectTrees.get();
		grimEffectAnimals = COMMON.grimEffectAnimals.get();
		grimEffectPigs = COMMON.grimEffectPigs.get();
		grimEffectVillagers = COMMON.grimEffectVillagers.get();
		grimLifeheartPulseSeconds = COMMON.grimLifeheartPulseSeconds.get();
		
		grimFogRedPercent = COMMON.grimFogRedPercent.get();
		grimFogBluePercent = COMMON.grimFogBluePercent.get();
		grimFogGreenPercent = COMMON.grimFogGreenPercent.get();
		
		if (debugLevel > 0) {
			System.out.println("Harder Farther Grim Citadels Debug Level: " + debugLevel );
		}
	}
	


	private static List<BlockPos> getBlockPositions(List<? extends String> list) {

		List< BlockPos> returnList = new ArrayList<>();
		for (String pos : list) {
			 String[] posParts = pos.split(",");
			 int x = Integer.valueOf(posParts[0]);
			 int y = -1;
			 int z = Integer.valueOf(posParts[1]);
			 returnList.add(new BlockPos(x,y,z));
		}
		return returnList;
	}

	private static String[] extract(List<? extends String> value)
	{
		return value.toArray(new String[value.size()]);
	}
	
	public static class Common {

		public final IntValue debugLevel;
		
		public final BooleanValue useGrimCitadels;
		public final IntValue grimCitadelsRadius;
		public final IntValue grimCitadelsCount;
		public final IntValue grimCitadelBonusDistance;
		public final IntValue grimCitadelPlayerCurseDistance;
		public final IntValue grimCitadelMaxBoostPercent;
		
		public final BooleanValue grimEffectTrees;
		public final BooleanValue grimEffectAnimals;
		public final BooleanValue grimEffectPigs;
		public final BooleanValue grimEffectVillagers;
		public final IntValue grimLifeheartPulseSeconds;

		
		public final DoubleValue grimFogRedPercent;
		public final DoubleValue grimFogBluePercent;
		public final DoubleValue grimFogGreenPercent;

		public final ConfigValue<List<? extends String>> grimCitadelsList;		
		
		public Common(ForgeConfigSpec.Builder builder) {
			
			List<String> defGrimCitadelsList = Arrays.asList(
					"3600,3500","3500,-100", "3500,-3550",
					"0,3596",             "128,-3500",
					"-2970,3516", "-3517,80", "-3528,-3756");
			
			builder.push("Harder Farther Control Values");
			builder.push("Debug Settings");			
			debugLevel = builder
					.comment("Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log")
					.translation(Main.MODID + ".config." + "debugLevel")
					.defineInRange("debugLevel", () -> 0, 0, 2);
			builder.pop();

			builder.push("Grim Citadel Settings");
			
			useGrimCitadels = builder
					.comment("Use Grim Citadels (true) ")
					.translation(Main.MODID + ".config." + "useGrimCitadels")
					.define ("useGrimCitadels", () -> true);
			
			grimCitadelsList = builder
					.comment("Grim Citadels List")
					.translation(Main.MODID + ".config" + "grimCitadelsList")
					.defineList("grimCitadelsList", defGrimCitadelsList, Common::isString);

			grimCitadelsCount = builder
					.comment("grimCitadelsCount : number of grim Citadels kept in the game (if 0 will count down til none left)")
					.translation(Main.MODID + ".config." + "grimCitadelsCount")
					.defineInRange("grimCitadelsCount", () -> 5, 0, 16);	

			grimCitadelsRadius= builder
					.comment("grimCitadelsRadius: Sug.  4-5 for one player, 5-7 for multiplayer.  Higher may slow server briefly while building.")
					.translation(Main.MODID + ".config." + "grimCitadelsRadius")
					.defineInRange("grimCitadelsRadius", () -> 5, 4, 11);	
			
			grimCitadelBonusDistance = builder
					.comment("grimCitadelBonusDistance : Mobs get increasing bonuses when closer to grim citadel")
					.translation(Main.MODID + ".config." + "grimCitadelBonusDistance")
					.defineInRange("grimCitadelBonusDistance", () -> 1750, 500, 6000);	

			grimCitadelPlayerCurseDistance = builder
					.comment("grimCitadelPlayerCurseDistance : Players get penalties this far from a grim citadel")
					.translation(Main.MODID + ".config." + "grimCitadelPlayerCurseDistance")
					.defineInRange("grimCitadelPlayerCurseDistance", () -> 1250, 255, 6000);	
			
			grimCitadelMaxBoostPercent = builder
					.comment("grimCitadelMaxBoostPercent : max Boost a grim citadel can give")
					.translation(Main.MODID + ".config." + "grimCitadelMaxBoostPercent")
					.defineInRange("grimCitadelMaxBoostPercent", () -> 96, 0, 100);
			
			builder.pop();
			builder.push("Grim Effects Settings");					

			grimEffectTrees = builder
					.comment("grimEffectTrees : Master Switch Trees suffer Grim Effects. ")
					.translation(Main.MODID + ".config." + "grimEffectTrees")
					.define ("grimEffectTrees", () -> true);

			
			grimEffectAnimals = builder
					.comment("grimEffectAnimals : Master Switch Animals suffer grim effects. ")
					.translation(Main.MODID + ".config." + "grimEffectAnimals")
					.define ("grimEffectAnimals", () -> true);

			grimEffectPigs = builder
					.comment("grimEffectPig : Pigs in grim area become Piglins, Zombified Piglins, or Hoglins over time. ")
					.translation(Main.MODID + ".config." + "grimEffectPigs")
					.define ("grimEffectPigs", () -> true);

			grimEffectVillagers = builder
					.comment("grimEffectVillagers : Villagers in grim area become witches. ")
					.translation(Main.MODID + ".config." + "grimEffectVillagers")
					.define ("grimEffectVillagers", () -> true);
			grimLifeheartPulseSeconds = builder
					.comment("grimLifeheartPulseSeconds : number of grim Citadels kept in the game (if 0 will count down til none left)")
					.translation(Main.MODID + ".config." + "grimLifeheartPulseSeconds")
					.defineInRange("grimLifeheartPulseSeconds", () -> 120, 60, 600);	
			
			builder.push("Grim Fog Color Settings");			
			grimFogRedPercent = builder
					.comment("grimFogRedPercent : Grim Fog Red Component Multiplier")
					.translation(Main.MODID + ".config." + "grimFogRedPercent")
					.defineInRange("grimFogRedPercent", () -> 0.95, 0.0, 1.0);	

			grimFogBluePercent = builder
					.comment("grimFogBluePercent : Grim Fog Blue Component Multiplier")
					.translation(Main.MODID + ".config." + "grimFogBluePercent")
					.defineInRange("grimFogBluePercent", () -> 0.05, 0.0, 1.0);	

			grimFogGreenPercent = builder
					.comment("grimFogGreenPercent : Grim Fog Green Component Multiplier")
					.translation(Main.MODID + ".config." + "grimFogGreenPercent")
					.defineInRange("grimFogGreenPercent", () -> 0.05, 0.0, 1.0);	

			builder.pop();
			builder.pop();
			builder.pop();
			
		}
		

		public static boolean isString(Object o)
		{
			return (o instanceof String);
		}
	}
	
}

