package com.mactso.harderfarthergrimcitadels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mactso.harderfarthergrimcitadels.block.ModBlocks;
import com.mactso.harderfarthergrimcitadels.blockentities.ModBlockEntities;
import com.mactso.harderfarthergrimcitadels.commands.ModCommands;
import com.mactso.harderfarthergrimcitadels.config.MyConfig;
import com.mactso.harderfarthergrimcitadels.events.BlockEvents;
import com.mactso.harderfarthergrimcitadels.events.FogColorsEventHandler;
import com.mactso.harderfarthergrimcitadels.events.LivingEventMovementHandler;
import com.mactso.harderfarthergrimcitadels.events.PlayerLoginEventHandler;
import com.mactso.harderfarthergrimcitadels.events.PlayerTeleportHandler;
import com.mactso.harderfarthergrimcitadels.events.WorldTickHandler;
import com.mactso.harderfarthergrimcitadels.item.ModItems;
import com.mactso.harderfarthergrimcitadels.managers.GrimCitadelManager;
import com.mactso.harderfarthergrimcitadels.network.Register;
import com.mactso.harderfarthergrimcitadels.proxy.IHarderFartherCoreProxy;
import com.mactso.harderfarthergrimcitadels.proxy.LocalCoreCalls;
import com.mactso.harderfarthergrimcitadels.proxy.RemoteCoreCalls;
import com.mactso.harderfarthergrimcitadels.sounds.ModSounds;
import com.mactso.harderfarthergrimcitadels.utility.Utility;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@Mod("harderfarthergrimcitadels")
public class Main {

	    public static final String MODID = "harderfarthergrimcitadels"; 
		private static final Logger LOGGER = LogManager.getLogger();
	    public static IHarderFartherCoreProxy difficultyCallProxy;

		public Main()
	    {
	    	System.out.println(MODID + ": Registering Mod.");
	  		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	  		
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC );
	  		
	    	if (ModList.get().isLoaded("harderfarthercore")) {
	    		difficultyCallProxy = new RemoteCoreCalls ();
	    		MyConfig.setUsingCore(true);
	    	} else {
	    		difficultyCallProxy = new LocalCoreCalls ();
	    		MyConfig.setUsingCore(false);

	    	}

			
	    }
		
		@OnlyIn(Dist.CLIENT)
	    @SubscribeEvent
	    public void setupClient(final FMLClientSetupEvent event) {
	    	
			MinecraftForge.EVENT_BUS.register(new FogColorsEventHandler());
			ModBlocks.setRenderLayer();
			
	    }
	    
	    @SubscribeEvent
	    public void setupCommon(final FMLCommonSetupEvent event)
	    {
	        Register.initPackets();
	    }

		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
				Utility.debugMsg(0, MODID + ": Registering Handlers");

				MinecraftForge.EVENT_BUS.register(WorldTickHandler.class);
				MinecraftForge.EVENT_BUS.register(PlayerLoginEventHandler.class);
				MinecraftForge.EVENT_BUS.register(PlayerTeleportHandler.class);
				MinecraftForge.EVENT_BUS.register(LivingEventMovementHandler.class);
				MinecraftForge.EVENT_BUS.register(BlockEvents.class);
 		} 
		
		@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	    public static class ModEvents
	    {

		    @SubscribeEvent
		    public static void onRegister(final RegisterEvent event)
		    {

		    	@Nullable
				IForgeRegistry<Object> fr = event.getForgeRegistry();
		    	
		    	@NotNull
				ResourceKey<? extends Registry<?>> key = event.getRegistryKey();
		    	if (key.equals(ForgeRegistries.Keys.BLOCKS))
		    		ModBlocks.register(event.getForgeRegistry());
		    	else if (key.equals(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES))
		    		ModBlockEntities.register(event.getForgeRegistry());
		    	else if (key.equals(ForgeRegistries.Keys.ITEMS))
		    		ModItems.register(event.getForgeRegistry());
		    	else if (key.equals(ForgeRegistries.Keys.SOUND_EVENTS))
		    		ModSounds.register(event.getForgeRegistry());
		    }
		    
	    }	
		
		
		
		@Mod.EventBusSubscriber()
		public static class ForgeEvents {

			@SubscribeEvent
			public static void onServerAbout(ServerAboutToStartEvent event)
			{
				GrimCitadelManager.load(event.getServer());
			}
			
			@SubscribeEvent
			public static void onServerStopping(ServerStoppingEvent event)
			{
				// TODO Does harder farther core need a cleanup as well?
				GrimCitadelManager.clear();
				Utility.debugMsg(0, MODID + "Cleanup Successful");
			}

			@SubscribeEvent 		
			public static void onCommandsRegistry(final RegisterCommandsEvent event) {
				Utility.debugMsg(0, Main.MODID + " : Registering Command Dispatcher");
				ModCommands.register(event.getDispatcher());			
			}

		
		
		}
		
		
		
}
