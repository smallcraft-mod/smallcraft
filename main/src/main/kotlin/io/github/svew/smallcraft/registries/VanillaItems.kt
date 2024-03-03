package io.github.svew.smallcraft.registries

import io.github.svew.smallcraft.core.ScRegistry
import io.github.svew.smallcraft.item.LadderItemRegistration
import net.minecraft.world.item.Item
import net.minecraftforge.registries.ForgeRegistries

@Suppress("unused")
object VanillaItems : ScRegistry<Item>(ForgeRegistries.ITEMS, "minecraft")
{
    val LADDER_ITEM by register(LadderItemRegistration)
}