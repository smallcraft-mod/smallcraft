@file:Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
package io.github.svew.smallcraft.block

import io.github.svew.smallcraft.Smallcraft
import io.github.svew.smallcraft.Smallcraft.LADDER_BLOCK
import io.github.svew.smallcraft.core.ScBlock
import io.github.svew.smallcraft.core.ScBlockRegistration
import io.github.svew.smallcraft.util.IScanIteration
import io.github.svew.smallcraft.util.scanLinear
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

object LadderBlockRegistration : ScBlockRegistration<LadderBlock>()
{
    override val id = "ladder"
    override val new = { LadderBlock(this) }
    override val tags = arrayOf(BlockTags.CLIMBABLE)
    override val properties: BlockBehaviour.Properties = BlockBehaviour.Properties.of()
            .forceSolidOff()
            .strength(0.4f)
            .sound(SoundType.LADDER)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
}

class LadderBlock(registration: ScBlockRegistration<LadderBlock>)
    : ScBlock(registration, States)
    , SimpleWaterloggedBlock
{
    object States : ScBlockStates()
    {
        val WATERLOGGED = create(BlockStateProperties.WATERLOGGED, false)
        val FACING = create(HorizontalDirectionalBlock.FACING, Direction.NORTH)
    }

    object Constants
    {
        val EAST_AABB: VoxelShape = box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
        val WEST_AABB: VoxelShape = box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        val SOUTH_AABB: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
        val NORTH_AABB: VoxelShape = box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
        const val MAX_CONNECTED_CHAIN_LENGTH = 5
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, collisionContext: CollisionContext): VoxelShape
    {
        return when (state.getValue(States.FACING))
        {
            Direction.NORTH -> Constants.NORTH_AABB
            Direction.SOUTH -> Constants.SOUTH_AABB
            Direction.WEST  -> Constants.WEST_AABB
            Direction.EAST  -> Constants.EAST_AABB
            else            -> Constants.EAST_AABB
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean
    {
        if (isSupportedBelow(level, pos)) return true
        if (isSupportedBehind(level, pos, state)) return true
        if (isConnectedBelow(level, pos, state)) return true
        return false
    }

    override fun updateShape(state: BlockState, direction: Direction, neighborState: BlockState, level: LevelAccessor, pos: BlockPos, neighborPos: BlockPos): BlockState
    {
        if (direction.opposite == state.getValue(States.FACING) || direction == Direction.DOWN)
        {
            if (!state.canSurvive(level, pos))
            {
                return Blocks.AIR.defaultBlockState()
            }
        }

        if (state.getValue(States.WATERLOGGED))
        {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level))
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState?
    {
        val level = context.level
        val clickedPos = context.clickedPos

        var state = this.defaultBlockState()
        val fluidState = context.level.getFluidState(context.clickedPos)
        val lookingDirections = context.nearestLookingDirections

        for (direction in lookingDirections)
        {
            if (!direction.axis.isHorizontal) continue

            state = state.setValue(States.FACING, direction.opposite)
            if (state.canSurvive(level, clickedPos))
            {
                return state.setValue(States.WATERLOGGED, fluidState.type === Fluids.WATER)
            }
        }

        return null
    }

    override fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult
    {
        if (player.isSecondaryUseActive == false) return InteractionResult.PASS
        return tryReelDownLadder(level, pos, player)
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState
    {
        return state.setValue(States.FACING, rotation.rotate(state.getValue(States.FACING)))
    }

    override fun mirror(state: BlockState, mirror: Mirror): BlockState
    {
        return state.rotate(mirror.getRotation(state.getValue(States.FACING)))
    }

    override fun getFluidState(state: BlockState): FluidState
    {
        if (state.getValue(States.WATERLOGGED)) return Fluids.WATER.getSource(false)
        return Fluids.EMPTY.defaultFluidState()
    }

    override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState)
    {
        // Check if there are any unsupported ladders above that need to be destroyed
        val facing = state.getValue(States.FACING)
        for((currPos, currState, i) in scanLadderChain(level, pos, Direction.UP, facing))
        {
            if (isSupportedBehind(level, currPos, currState)) break
            level.destroyBlock(currPos, true)
        }
    }
}

// Returns true if a ladder with the given state would be connected to a chain of unsupported ladders below it.
// The length of this chain is limited to MAX_CONNECTED_CHAIN_LENGTH ladders long.
// Any ladders added atop a chain of that length are not part of the chain, and are not connected.
private fun isConnectedBelow(level: LevelReader, pos: BlockPos, state: BlockState? = null): Boolean
{
    val thisState = state ?: level.getBlockState(pos)
    val thisFacing = thisState.getValue(LadderBlock.States.FACING)
    for ((currPos, currState, i) in scanLadderChain(level, pos.below(), Direction.DOWN, thisFacing))
    {
        // Do this first, because a ladder block attached from behind doesn't count in unsupported chain
        if (isSupportedBehind(level, currPos, currState)) return true

        // Unattached ladder chain is too long for this ladder block to be connected
        if (i + 1 >= LadderBlock.Constants.MAX_CONNECTED_CHAIN_LENGTH) return false

        if (isSupportedBelow(level, currPos)) return true
    }
    return false // No ladder blocks below, or hanging ladders?
}

// Returns true if a ladder with the given state would be supported and attached to a block behind it
private fun isSupportedBehind(level: BlockGetter, pos: BlockPos, state: BlockState): Boolean
{
    val thisDirection = state.getValue(LadderBlock.States.FACING)
    val behindPos = pos.relative(thisDirection.opposite)
    val behindState = level.getBlockState(behindPos)
    if (behindState.`is`(LADDER_BLOCK)) return false
    return behindState.isFaceSturdy(level, behindPos, thisDirection)
}

// Returns true if a ladder at the given position would be supported by the block beneath it.
// Ladders cannot support other ladder blocks.
// For that, a ladder block must check if it's connected to the below ladder block or not.
private fun isSupportedBelow(level: BlockGetter, pos: BlockPos): Boolean
{
    if (LadderBlock.Constants.MAX_CONNECTED_CHAIN_LENGTH == 0) return false
    val belowPos = pos.below()
    val belowState = level.getBlockState(belowPos)
    if (belowState.`is`(LADDER_BLOCK)) return false
    return belowState.isFaceSturdy(level, belowPos, Direction.UP)
}

fun tryReelDownLadder(level: Level, pos: BlockPos, player: Player?): InteractionResult
{
    if (player != null && !player.abilities.mayBuild) return InteractionResult.PASS
    if (player != null && !player.abilities.instabuild)
    {
        val stack = ItemStack(Smallcraft.LADDER_ITEM)
        val openSlot = player.inventory.getSlotWithRemainingSpace(stack)
        if (openSlot == -1) return InteractionResult.PASS // TODO("Add animation to show that inventory is full")
        player.inventory.add(openSlot, stack)
    }

    val (lastPos, _, _) = scanLadderChain(level, pos, Direction.UP).last()
    level.destroyBlock(lastPos, false, player)
    return InteractionResult.sidedSuccess(level.isClientSide)
}

fun scanLadderChain(level: LevelReader, startingPos: BlockPos, direction: Direction, facing: Direction? = null): Sequence<IScanIteration>
{
    if (direction != Direction.UP && direction != Direction.DOWN) throw IllegalArgumentException("Ladders can only go up or down")
    val facing = facing ?: level.getBlockState(startingPos)
                                .getValue(LadderBlock.States.FACING)
    fun predicate(iter: IScanIteration): Boolean
    {
        val state = iter.state
        if (state.`is`(LADDER_BLOCK) == false) return false
        if (state.getValue(LadderBlock.States.FACING) != facing) return false
        return true
    }
    return scanLinear(level, startingPos, direction)
        .asSequence()
        .takeWhile { it -> predicate(it) }
}