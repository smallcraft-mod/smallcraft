package io.github.svew.smallcraft.registries

import io.github.svew.smallcraft.Smallcraft
import io.github.svew.smallcraft.core.ScRegistry
import io.github.svew.smallcraft.item.DirtItemRegistration
import io.github.svew.smallcraft.item.RopeItemRegistration
import io.github.svew.smallcraft.item.StoneItemRegistration
import net.minecraft.world.item.Item
import net.minecraftforge.registries.ForgeRegistries

@Suppress("unused")
object ModItems : ScRegistry<Item>(ForgeRegistries.ITEMS, Smallcraft.MOD_ID)
{
    val ROPE_ITEM by register(RopeItemRegistration)
    val STONE_ITEM by register(StoneItemRegistration)
    val DIRT_ITEM by register(DirtItemRegistration)
}