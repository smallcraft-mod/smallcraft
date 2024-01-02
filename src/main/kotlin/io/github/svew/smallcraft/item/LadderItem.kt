package io.github.svew.smallcraft.item

import io.github.svew.smallcraft.Smallcraft.LADDER_BLOCK
import io.github.svew.smallcraft.block.scanLadderChain
import io.github.svew.smallcraft.block.tryReelDownLadder
import io.github.svew.smallcraft.core.ScBlockItem
import io.github.svew.smallcraft.core.ScBlockItemRegistration
import net.minecraft.core.Direction
import net.minecraft.tags.FluidTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext

object LadderItemRegistration : ScBlockItemRegistration<LadderItemBehaviour>()
{
    override val block get() = LADDER_BLOCK
    override val id = "ladder"
    override val creativeModeTabs = setOf(CreativeModeTabs.FUNCTIONAL_BLOCKS)
    override val new = { LadderItemBehaviour(this) }
    override val properties: Item.Properties = Item.Properties()
}

class LadderItemBehaviour(registration: ScBlockItemRegistration<LadderItemBehaviour>)
    : ScBlockItem(registration)
{
    override fun useOn(context: UseOnContext): InteractionResult
    {
        if (!context.isSecondaryUseActive) return super.useOn(context)
        return tryReelDownLadder(context.level, context.clickedPos, context.player)
    }

    override fun updatePlacementContext(context: BlockPlaceContext): BlockPlaceContext?
    {
        val pos = if (context.replacingClickedOnBlock()) { context.clickedPos }
                  else                                   { context.clickedPos.relative(context.clickedFace.opposite) }
        val level = context.level

        // Let RopeBlock.canSurvive handle placement on non-rope blocks
        if (level.getBlockState(pos).`is`(LADDER_BLOCK) == false) return context

        val list = scanLadderChain(level, pos, Direction.UP).toList()
        val (topLadderPos, _, _) = list.last()

        val newLadderPos = topLadderPos.above()
        val replacedState = level.getBlockState(newLadderPos)
        if (!replacedState.canBeReplaced()) return null

        val fluidState = replacedState.fluidState
        if (!fluidState.isEmpty && fluidState.`is`(FluidTags.WATER) == false) return null

        return BlockPlaceContext.at(context, newLadderPos, Direction.UP)
    }
}
