package com.sixteencolorgames.dandrcustom;

import net.minecraft.init.Blocks;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.Logger;

import com.sixteencolorgames.dandrcustom.world.ModWorldGenerator;


@Mod(modid = DandrCustomMod.MODID, name = DandrCustomMod.NAME, version = DandrCustomMod.VERSION, acceptableRemoteVersions = "*")
public class DandrCustomMod {
	public static final String MODID = "dandrcustom";
	public static final String NAME = "D&R Custom Mod";
	public static final String VERSION = "1.0";

	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
    	GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 0); 
	}
}
