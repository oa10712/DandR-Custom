package com.sixteencolorgames.dandrcustom.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;

public class ModWorldGenerator extends WorldGenerator implements IWorldGenerator {

	static Random rand2 = new Random();

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;

		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, rand, blockX + 8, blockZ + 8);
			break;
		case 0:
			generateOverworld(world, rand, blockX + 8, blockZ + 8);
			break;
		case 1:
			generateEnd(world, rand, blockX + 8, blockZ + 8);
			break;

		}

	}

	private void generateOverworld(World world, Random rand, int blockX, int blockZ) {
		if (Math.random() * 10000 <= 5) {
			int y = getGroundFromAbove(world, blockX, blockZ);
			BlockPos pos = new BlockPos(blockX, y, blockZ);
			WorldGenerator structure = new HellTemple1();
			structure.generate(world, rand, pos);
		}
		if (true) {
			int y = getGroundFromAbove(world, blockX, blockZ);
			BlockPos pos = new BlockPos(blockX, y, blockZ);
			WorldGenerator structure = new Greenhouse();
			structure.generate(world, rand, pos);
		}
	}

	private void generateNether(World world, Random rand, int chunkX, int chunkZ) {
	}

	private void generateEnd(World world, Random rand, int chunkX, int chunkZ) {
	}

	public static int getGroundFromAbove(World world, int x, int z) {
		int y = 255;
		boolean foundGround = false;
		while (!foundGround && y-- >= 31) {
			Block blockAt = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundGround = blockAt == Blocks.GRASS || blockAt == Blocks.SAND || blockAt == Blocks.SNOW
					|| blockAt == Blocks.SNOW_LAYER || blockAt == Blocks.GLASS || blockAt == Blocks.MYCELIUM;
		}

		return y;
	}

	public static boolean canSpawnHere(Template template, World world, BlockPos posAboveGround, int sub_levels) {
		int zwidth = template.getSize().getZ();
		int xwidth = template.getSize().getX();

		// check all the corners to see which ones are replaceable
		boolean corner1 = isCornerValid(world, posAboveGround, sub_levels);
		boolean corner2 = isCornerValid(world, posAboveGround.add(xwidth, 0, zwidth), sub_levels);

		// if Y > 20 and all corners pass the test, it's okay to spawn the
		// structure
		return posAboveGround.getY() > 31 && corner1 && corner2;
	}

	public static boolean isCornerValid(World world, BlockPos pos, int sub_levels) {
		int variation = 3;
		int highestBlock = getGroundFromAbove(world, pos.getX(), pos.getZ());

		if (highestBlock > pos.getY() - variation + sub_levels && highestBlock < pos.getY() + variation + sub_levels)
			return true;

		return false;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		// TODO Auto-generated method stub
		return false;
	}
}