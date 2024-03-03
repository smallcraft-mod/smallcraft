package io.github.svew.smallcraft.item

import io.github.svew.smallcraft.block.RopeBlock
import io.github.svew.smallcraft.block.tryReelRopeIn
import io.github.svew.smallcraft.core.ScBlockItem
import io.github.svew.smallcraft.core.registrations.ScBlockItemRegistration
import io.github.svew.smallcraft.registries.ModBlocks.ROPE_BLOCK
import io.github.svew.smallcraft.util.scanLinearWhile
import net.minecraft.core.Direction
import net.minecraft.tags.FluidTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext

object RopeItemRegistration : ScBlockItemRegistration<RopeItem>()
{
    override val id = "rope"
    override val new = { RopeItem(this) }
    override val block get() = ROPE_BLOCK
    override val properties: Item.Properties = Item.Properties()
    override val creativeModeTabs = setOf(CreativeModeTabs.TOOLS_AND_UTILITIES)
}

class RopeItem(registration: ScBlockItemRegistration<RopeItem>)
    : ScBlockItem(registration)
{
    override fun useOn(context: UseOnContext): InteractionResult
    {
        if (!context.isSecondaryUseActive) return super.useOn(context)

        val pos = context.clickedPos
        val level = context.level

        if (level.getBlockState(pos).block !is RopeBlock) return super.useOn(context)

        return tryReelRopeIn(level, pos, context.player)
    }

    override fun updatePlacementContext(context: BlockPlaceContext): BlockPlaceContext?
    {
        val pos = (context as UseOnContext).clickedPos
        val level = context.level

        // Let RopeBlock.canSurvive handle placement on non-rope blocks
        if (level.getBlockState(pos).block !is RopeBlock) return context

        val (bottomRopePos, _, _) = scanLinearWhile(level, pos, Direction.DOWN, ROPE_BLOCK).last()
        if (bottomRopePos.y == level.minBuildHeight) return null // Can't place lower than is possible

        val placementRopePos = bottomRopePos.below()
        val placementState = level.getBlockState(placementRopePos)
        if (!placementState.canBeReplaced()) return null

        val fluidState = placementState.fluidState
        if (!fluidState.isEmpty && fluidState.`is`(FluidTags.WATER) == false) return null // We're in lava or something

        return BlockPlaceContext.at(context, placementRopePos, Direction.DOWN)
    }
}