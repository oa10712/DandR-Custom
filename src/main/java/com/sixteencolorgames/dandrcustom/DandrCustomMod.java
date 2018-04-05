package com.sixteencolorgames.dandrcustom;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;

import com.sixteencolorgames.dandrcustom.world.ModWorldGenerator;

@Mod(modid = DandrCustomMod.MODID, name = DandrCustomMod.NAME, version = DandrCustomMod.VERSION, acceptableRemoteVersions = "*")
public class DandrCustomMod {
	public static final String MODID = "dandrcustom";
	public static final String NAME = "D&R Custom Mod";
	public static final String VERSION = "1.0";

	private static Logger logger;
	private static Item bonusItem = Items.RABBIT_FOOT;

	private static ArrayList<IBlockState> validVein = new ArrayList();
	private static int maxVeinSize = 64;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 0);

		validVein.add(Blocks.IRON_ORE.getDefaultState());
		validVein.add(Blocks.GOLD_ORE.getDefaultState());
		validVein.add(Blocks.COAL_ORE.getDefaultState());
		validVein.add(Blocks.LAPIS_ORE.getDefaultState());
		validVein.add(Blocks.EMERALD_ORE.getDefaultState());
		validVein.add(Blocks.DIAMOND_ORE.getDefaultState());
		validVein.add(Blocks.REDSTONE_ORE.getDefaultState());
		validVein.add(Blocks.LIT_REDSTONE_ORE.getDefaultState());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onBlockBreakEvent(BreakEvent event) {
		if (event.getWorld().isRemote) {
			return;
		}
		EntityPlayer player = event.getPlayer();
		ItemStack main = player.getHeldItemMainhand();
		World world = event.getWorld();
		if (main.getTagCompound() != null && main.getTagCompound().getBoolean("vein_mine")
				&& validVein.contains(world.getBlockState(event.getPos()))) {
			ArrayList<BlockPos> blo = new ArrayList();
			blo.add(event.getPos());
			IBlockState blockState = world.getBlockState(event.getPos());
			int count = 0;
			boolean more = true;
			while (more) {
				BlockPos origin = blo.get(count);
				for (int x = -1; x < 2; x++) {
					for (int y = -1; y < 2; y++) {
						for (int z = -1; z < 2; z++) {
							BlockPos test = origin.add(x, y, z);
							if (world.getBlockState(test) == blockState) {
								if (!blo.contains(test)) {
									blo.add(test);
								}
							}
						}
					}
				}

				count++;
				if (count == blo.size() || count == maxVeinSize) {
					more = false;
				}
			}
			for (BlockPos pos : blo) {
				world.destroyBlock(pos, true);
			}
		}
	}
}
