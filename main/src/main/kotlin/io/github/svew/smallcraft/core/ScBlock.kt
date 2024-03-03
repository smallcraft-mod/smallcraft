package io.github.svew.smallcraft.core

import io.github.svew.smallcraft.core.registrations.ScBlockRegistration
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

abstract class ScBlock(registration: ScBlockRegistration<*>, states: ScBlockStates = ScBlockStates.Empty())
    : Block(inject(registration, states))
{
    val registration get() = (this.soundType as ScSoundTypeInjector).registration
    val states get() = (this.soundType as ScSoundTypeInjector).states

    init
    {
        val blockState = this.stateDefinition.any()
        states.applyDefaults(blockState)
        this.registerDefaultState(blockState)
    }

    final override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>)
    {
        for (state in states.all)
        {
            builder.add(state)
        }
    }

    /**
     * Called when block is getting destroyed
     */
    override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState)
    {
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult
    {
        return InteractionResult.PASS
    }

    companion object
    {
        fun box(
            xStart: Double,
            yStart: Double,
            zStart: Double,
            xEnd: Double,
            yEnd: Double,
            zEnd: Double
        ): VoxelShape
        {
            return Shapes.box(
                xStart / 16.0,
                yStart / 16.0,
                zStart / 16.0,
                xEnd / 16.0,
                yEnd / 16.0,
                zEnd / 16.0
            )
        }
    }

}

// Terrible, horrible, dreadful sneaky injection!
// We just do this so we can store IScBlockRegistration inside of Block, and
// access it during Block's construction when it calls createBlockStateDefinition
@Suppress("DEPRECATION")
private class ScSoundTypeInjector(
    st: SoundType,
    val registration: ScBlockRegistration<*>,
    val states: ScBlockStates
)
    : SoundType(st.volume, st.pitch, st.breakSound, st.stepSound, st.placeSound, st.hitSound, st.fallSound)

private fun inject(registration: ScBlockRegistration<*>, states: ScBlockStates): BlockBehaviour.Properties
{
    val field = registration.properties.javaClass.getDeclaredField("soundType")
    field.isAccessible = true
    val soundType: SoundType = field.get(registration.properties) as SoundType // GOTCHA!
    val injector = ScSoundTypeInjector(soundType, registration, states)
    registration.properties.sound(injector)
    return registration.properties
}