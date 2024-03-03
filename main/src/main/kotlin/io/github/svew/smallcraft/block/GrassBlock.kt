package io.github.svew.smallcraft.block

import io.github.svew.smallcraft.core.ScBlockLootProvider
import io.github.svew.smallcraft.core.ScBlockStates
import io.github.svew.smallcraft.core.registrations.Model
import io.github.svew.smallcraft.core.registrations.ScBlockRegistration
import io.github.svew.smallcraft.registries.ModItems
import net.minecraft.world.level.block.GrassBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.MapColor

object GrassBlockRegistration : ScBlockRegistration<GrassBlock>()
{
    override val id = "grass_block"
    override val name = "Grass"
    override val new = { GrassBlock(properties) }
    override val properties: BlockBehaviour.Properties = BlockBehaviour.Properties.of()
        .mapColor(MapColor.GRASS)
        .randomTicks()
        .strength(0.6f)
        .sound(SoundType.GRASS)
    override val blockStateGenerator = SnowyDirtBlockStates.variantBuilder(obj) { snowy -> when(snowy)
    {
        true -> listOf(Model("minecraft:block/grass_block_snow"))
        false -> listOf(
            Model("minecraft:block/grass_block"),
            Model("minecraft:block/grass_block", y=90),
            Model("minecraft:block/grass_block", y=180),
            Model("minecraft:block/grass_block", y=270))
    } }
    override fun addLootTable(reg: ScBlockLootProvider) = reg.createSingleItemTable(ModItems.DIRT_ITEM, 4)
}

object SnowyDirtBlockStates : ScBlockStates.C1<Boolean>()
{
    var SNOWY = create1(BlockStateProperties.SNOWY, false)
}