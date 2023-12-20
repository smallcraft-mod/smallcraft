package io.github.svew.smallcraft.item

import io.github.svew.smallcraft.Smallcraft
import io.github.svew.smallcraft.block.RopeBlock
import net.minecraft.core.Direction
import net.minecraft.tags.FluidTags
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext

class RopeItem(block: RopeBlock) : BlockItem(block, Properties()) {

    override fun updatePlacementContext(context: BlockPlaceContext): BlockPlaceContext? {
        val pos = (context as UseOnContext).clickedPos
        Smallcraft.LOGGER.debug("=======================================")
        Smallcraft.LOGGER.debug("Clicked on position ${pos.x}, ${pos.y}")
        val level = context.level

        if (level.getBlockState(pos).block !is RopeBlock) {
            Smallcraft.LOGGER.debug("Block is not a rope block, will be placed if there's a supporting block above")
            // Let RopeBlock.canSurvive handle placement on non-rope blocks
            return context
        }

        val iterPos = pos.mutable()
        for (i in 0..128) {
            iterPos.move(Direction.DOWN)
            val state = level.getBlockState(iterPos)
            Smallcraft.LOGGER.debug("Inspecting block at ${iterPos.x}, ${iterPos.y}. Found ${state.block.name}")

            if (state.block is RopeBlock) {
                Smallcraft.LOGGER.debug("Found a rope, let's move down...")
                continue
            }
            val fluidState = state.fluidState
            if (fluidState.isEmpty == false && fluidState.`is`(FluidTags.WATER) == false) {
                Smallcraft.LOGGER.debug("We're in lava or something")
                return null // We're in lava or something
            }
            if (state.canBeReplaced()) {
                return BlockPlaceContext.at(context, iterPos, Direction.DOWN)
            }
            Smallcraft.LOGGER.debug("Block could not be replaced")
            return null
        }
        return null
    }
}