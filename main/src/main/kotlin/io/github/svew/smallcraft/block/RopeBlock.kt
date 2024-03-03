@file:Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
package io.github.svew.smallcraft.block

import io.github.svew.smallcraft.core.ScBlock
import io.github.svew.smallcraft.core.ScBlockLootProvider
import io.github.svew.smallcraft.core.ScBlockStates
import io.github.svew.smallcraft.core.registrations.ScBlockRegistration
import io.github.svew.smallcraft.registries.ModBlocks.ROPE_BLOCK
import io.github.svew.smallcraft.registries.ModItems.ROPE_ITEM
import io.github.svew.smallcraft.util.scanLinearWhile
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
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
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

object RopeBlockRegistration : ScBlockRegistration<RopeBlock>()
{
    override val id = "rope"
    override val name = "Rope"
    override val new = { RopeBlock(this) }
    override val tags = arrayOf(BlockTags.CLIMBABLE)
    override val properties: BlockBehaviour.Properties = BlockBehaviour.Properties
            .copy(Blocks.BROWN_CARPET)
            .noCollission()
            .noOcclusion()
            .strength(0.1F)
            .sound(SoundType.WOOL)
            .noParticlesOnBreak()
    override fun addLootTable(reg: ScBlockLootProvider) = reg.createSingleItemTable(ROPE_ITEM)
}

class RopeBlock(registration: ScBlockRegistration<RopeBlock>)
    : ScBlock(registration, States)
    , SimpleWaterloggedBlock
{
    object States : ScBlockStates.C1<Boolean>()
    {
        val WATERLOGGED = create(BlockStateProperties.WATERLOGGED, false)
    }

    object Constants
    {
        val SHAPE_AABB = box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0)
    }

    override fun propagatesSkylightDown(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Boolean {
        return !blockState.getValue(States.WATERLOGGED);
    }

    override fun isPathfindable(state: BlockState, level: BlockGetter, pos: BlockPos, type: PathComputationType): Boolean {
        return true
    }

    override fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult
    {
        if (!player.isSecondaryUseActive) return InteractionResult.PASS
        return tryReelRopeIn(level, pos, player)
    }

    override fun getCollisionShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Shapes.empty()
    }

    override fun getBlockSupportShape(pState: BlockState, pReader: BlockGetter, pPos: BlockPos): VoxelShape {
        return box(7.0, 0.0, 7.0, 9.0, 1.0, 9.0)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape
    {
        return Constants.SHAPE_AABB
    }

    override fun canBeReplaced(state: BlockState, useContext: BlockPlaceContext): Boolean {
        return useContext.itemInHand.item === this.asItem()
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val posAbove = pos.above()
        return level.getBlockState(posAbove).isFaceSturdy(level, posAbove, Direction.DOWN, SupportType.CENTER)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        var state = super.getStateForPlacement(context) ?: return null
        val hasWaterAtPosition = context.level.getFluidState(context.clickedPos).`is`(FluidTags.WATER)
        state = state.setValue(States.WATERLOGGED, hasWaterAtPosition)
        return state
    }

    override fun isLadder(state: BlockState?, level: LevelReader?, pos: BlockPos?, entity: LivingEntity?): Boolean {
        return super.isLadder(state, level, pos, entity)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(States.WATERLOGGED)) {
            Fluids.WATER.getSource(false)
        } else {
            super.getFluidState(state)
        }
    }

    override fun updateShape(
        state: BlockState,
        facing: Direction,
        facingState: BlockState,
        level: LevelAccessor,
        currentPos: BlockPos,
        facingPos: BlockPos
    ): BlockState {
        if (state.getValue(States.WATERLOGGED) == true) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level))
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos)
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        currentPos: BlockPos,
        changedBlock: Block,
        changedPos: BlockPos,
        isMoving: Boolean
    ) {
        super.neighborChanged(state, level, currentPos, changedBlock, changedPos, isMoving) // Basically a no-op, but good to keep
        if (currentPos.above() != changedPos) {
            return // only care about changes above
        }
        if (canSurvive(state, level, currentPos)) {
            return // no issue, still alive!
        }
        // we're gonna die! and i'm taking you all with me!
        val iterPos = currentPos.mutable()
        while (level.getBlockState(iterPos).`is`(ROPE_BLOCK)) {
            level.destroyBlock(iterPos, /* drop block */ true)
            iterPos.move(Direction.DOWN)
        }
    }
}

/**
 * Given the position of a rope block (at any point along a rope chain),
 * "reel in" the bottom-most rope block in the chain (currently, destroys it)
 */
fun tryReelRopeIn(level: Level, pos: BlockPos, player: Player?): InteractionResult
{
    if (player != null && !player.abilities.mayBuild) return InteractionResult.PASS
    if (player != null && !player.abilities.instabuild)
    {
        val stack = ItemStack(ROPE_ITEM)
        val openSlot = player.inventory.getSlotWithRemainingSpace(stack)
        if (openSlot == -1) return InteractionResult.PASS // TODO("Add animation to show that inventory is full")
        player.inventory.add(openSlot, stack)
    }

    val (lastRopePos, _, _) = scanLinearWhile(level, pos, Direction.DOWN, ROPE_BLOCK).last()
    level.destroyBlock(lastRopePos, /* drops item? */ false, player)
    return InteractionResult.sidedSuccess(level.isClientSide)
}