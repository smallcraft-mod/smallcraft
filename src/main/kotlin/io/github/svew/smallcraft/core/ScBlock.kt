package io.github.svew.smallcraft.core

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.Property
import net.minecraftforge.registries.DeferredRegister

abstract class ScBlock(name: String, props: Properties)
    : Block(props)
{
    constructor(registration: IScBlockRegistration) : this(registration.name, registration.properties)

    // Helps with type resolution for default values
    data class Item<T : Comparable<T>>(val property: Property<T>, val defaultValue: T)
    {
        fun setDefaultState(state: BlockState) {
            state.setValue(property, defaultValue)
        }
    }

    // Could do a map, but don't want to double-store properties as keys and in values, plus it's safe
    // to assume we won't duplicate it ourselves
    private val defaultValueList = mutableListOf<Item<*>>()

    init {
        val blockState = this.stateDefinition.any()
        for (item in defaultValueList) {
            item.setDefaultState(blockState)
        }
        this.registerDefaultState(blockState)
    }

    protected fun <T : Comparable<T>> defineState(property: Property<T>, defaultValue: T): Property<T> {
        defaultValueList.add(Item(property, defaultValue))
        return property
    }

    final override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        for (item in defaultValueList) {
            builder.add(item.property)
        }
    }
}