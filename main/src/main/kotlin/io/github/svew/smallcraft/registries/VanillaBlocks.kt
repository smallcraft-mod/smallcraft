package io.github.svew.smallcraft.registries

import io.github.svew.smallcraft.block.*
import io.github.svew.smallcraft.core.ScRegistry
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.ForgeRegistries

@Suppress("unused")
object VanillaBlocks : ScRegistry<Block>(ForgeRegistries.BLOCKS, "minecraft")
{
    val LADDER_BLOCK by register(LadderBlockRegistration)
    val STONE_BLOCK by register(StoneBlockRegistration)
    val DIRT_BLOCK by register(DirtBlockRegistration)
    val GRASS_BLOCK by register(GrassBlockRegistration)
    val PODZOL_BLOCK by register(PodzolBlockRegistration)
}