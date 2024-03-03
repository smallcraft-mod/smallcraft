package io.github.svew.smallcraft.block

import io.github.svew.smallcraft.core.ScBlockLootProvider
import io.github.svew.smallcraft.core.registrations.ScBlockRegistration
import io.github.svew.smallcraft.registries.ModItems
import net.minecraft.world.level.block.SnowyDirtBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

object PodzolBlockRegistration : ScBlockRegistration<SnowyDirtBlock>()
{
    override val id = "podzol"
    override val name = "Podzol"
    override val new = { SnowyDirtBlock(properties) }
    override val properties: BlockBehaviour.Properties = BlockBehaviour.Properties.of()
        .mapColor(MapColor.PODZOL)
        .strength(0.5F)
        .sound(SoundType.GRAVEL)
    override fun addLootTable(reg: ScBlockLootProvider) = reg.createSingleItemTable(ModItems.DIRT_ITEM, 4)
}