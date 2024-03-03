package io.github.svew.smallcraft.core

import io.github.svew.smallcraft.core.registrations.ScBlockItemRegistration
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class ScBlockItem(val registration: ScBlockItemRegistration<*>)
    : BlockItem(registration.block, registration.properties)
{
    val block get() = registration.block

    override fun useOn(context: UseOnContext): InteractionResult
    {
        val interactionResult = this.place(BlockPlaceContext(context))
        if (interactionResult.consumesAction() || !this.isEdible) return interactionResult

        val interactionResult1 = this.use(context.level, context.player!!, context.hand).result
        if (interactionResult1 == InteractionResult.CONSUME) return InteractionResult.CONSUME_PARTIAL
        return interactionResult1
    }

    override fun place(context: BlockPlaceContext): InteractionResult
    {
        return super.place(context)
    }

    //Forge: Use more sensitive version {@link BlockItem#getPlaceSound(BlockState, IBlockReader, BlockPos, Entity) }
    @Suppress("OVERRIDE_DEPRECATION")
    override fun getPlaceSound(state: BlockState): SoundEvent
    {
        return getPlaceSound(state)
    }

    //Forge: Sensitive version of BlockItem#getPlaceSound
    override fun getPlaceSound(state: BlockState, world: Level, pos: BlockPos, entity: Player): SoundEvent
    {
        return super.getPlaceSound(state, world, pos, entity)
    }

    override fun updatePlacementContext(context: BlockPlaceContext): BlockPlaceContext?
    {
        return super.updatePlacementContext(context)
    }

    override fun updateCustomBlockEntityTag(pos: BlockPos, level: Level, player: Player?, itemStack: ItemStack, state: BlockState): Boolean
    {
        return super.updateCustomBlockEntityTag(pos, level, player, itemStack, state)
    }

    override fun getPlacementState(context: BlockPlaceContext): BlockState?
    {
        return super.getPlacementState(context)
    }

    override fun canPlace(context: BlockPlaceContext, state: BlockState): Boolean
    {
        return super.canPlace(context, state)
    }

    override fun placeBlock(context: BlockPlaceContext, state: BlockState): Boolean
    {
        return super.placeBlock(context, state)
    }

    override fun appendHoverText(itemStack: ItemStack, level: Level?, componentList: List<Component>, tooltipFlag: TooltipFlag)
    {
        return super.appendHoverText(itemStack, level, componentList, tooltipFlag)
    }

    override fun registerBlocks(blockItemMap: MutableMap<Block, Item>, item: Item)
    {
        super.registerBlocks(blockItemMap, item)
    }

    override fun removeFromBlockToItemMap(blockToItemMap: MutableMap<Block, Item>, itemIn: Item)
    {
        super.removeFromBlockToItemMap(blockToItemMap, itemIn)
    }
}
