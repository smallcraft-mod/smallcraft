package io.github.svew.smallcraft.registries

import io.github.svew.smallcraft.Smallcraft
import io.github.svew.smallcraft.block.RopeBlockRegistration
import io.github.svew.smallcraft.core.ScRegistry
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.ForgeRegistries

@Suppress("unused")
object ModBlocks : ScRegistry<Block>(ForgeRegistries.BLOCKS, Smallcraft.MOD_ID)
{
    val ROPE_BLOCK by register(RopeBlockRegistration)
}