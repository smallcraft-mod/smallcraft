package io.github.svew.smallcraft.block

import io.github.svew.smallcraft.core.ScBlockLootProvider
import io.github.svew.smallcraft.core.registrations.ScBlockRegistration
import io.github.svew.smallcraft.registries.ModItems.STONE_ITEM
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor

object StoneBlockRegistration : ScBlockRegistration<Block>()
{
    override val id = "stone"
    override val name = "Stone"
    override val new = { Block(this.properties) }
    override val properties: Properties = Properties.of()
        .mapColor(MapColor.STONE)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .destroyTime(1.5f)
        .explosionResistance(6.0f)
    override fun addLootTable(reg: ScBlockLootProvider) = reg.createSingleItemTable(STONE_ITEM, 4)
}

