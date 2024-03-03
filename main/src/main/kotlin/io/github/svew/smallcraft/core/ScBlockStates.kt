package io.github.svew.smallcraft.core

import io.github.svew.smallcraft.core.registrations.Model
import io.github.svew.smallcraft.util.Product2
import io.github.svew.smallcraft.util.Product3
import io.github.svew.smallcraft.util.Product4
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.PropertyDispatch
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import kotlin.streams.asSequence

private data class StateInfo<T : Comparable<T>>(val prop: Property<T>, val defaultValue: T)
{
    fun setDefaultState(state: BlockState) {
        state.setValue(prop, defaultValue)
    }
}

class VariantBuilder

sealed class ScBlockStates private constructor()
{
    private val stateInfoSet = mutableSetOf<StateInfo<*>>()

    val all: List<Property<*>> get() = stateInfoSet.map({ info -> info.prop })

    fun applyDefaults(state: BlockState)
    {
        for (info in stateInfoSet)
        {
            info.setDefaultState(state)
        }
    }

    protected fun <T : Comparable<T>> create(prop: Property<T>, defaultValue: T): Property<T>
    {
        stateInfoSet.add(StateInfo(prop, defaultValue))
        return prop
    }

    class Empty : ScBlockStates()

    abstract class C1<T1> : ScBlockStates()
            where T1 : Comparable<T1>
    {
        private lateinit var prop1: Property<T1>

        protected fun create1(prop: Property<T1>, defaultValue: T1): Property<T1>
        {
            prop1 = create(prop, defaultValue)
            return prop1
        }

        fun variantBuilder(block: Block, init: VariantBuilder.(T1) -> List<Model>): BlockStateGenerator
        {
            val builder = VariantBuilder()
            val gen = MultiVariantGenerator.multiVariant(block)
            val propertyDispatch = PropertyDispatch.property(prop1)
            gen.with(propertyDispatch)

            for (value in prop1.allValues.asSequence())
            {
                val models = builder.init(value.value)
                propertyDispatch.select(value.value, models.map { it -> it.asVariant() })
            }
            return gen
        }
    }

    abstract class C2<T1, T2> : ScBlockStates()
            where T1 : Comparable<T1>,
                  T2 : Comparable<T2>
    {
        private lateinit var prop1: Property<T1>
        private lateinit var prop2: Property<T2>

        protected fun create1(prop: Property<T1>, defaultValue: T1): Property<T1> { prop1 = create(prop, defaultValue); return prop1; }
        protected fun create2(prop: Property<T2>, defaultValue: T2): Property<T2> { prop2 = create(prop, defaultValue); return prop2; }

        fun variantBuilder(block: Block, init: VariantBuilder.(T1, T2) -> List<Model>): BlockStateGenerator
        {
            val builder = VariantBuilder()
            val gen = MultiVariantGenerator.multiVariant(block)
            val propertyDispatch = PropertyDispatch.properties(prop1, prop2)
            gen.with(propertyDispatch)

            for (values in Product2(
                prop1.allValues.asSequence(),
                prop2.allValues.asSequence()))
            {
                val models = builder.init(values.a.value, values.b.value)
                propertyDispatch.select(values.a.value, values.b.value, models.map { it -> it.asVariant() })
            }
            return gen
        }
    }

    abstract class C3<T1, T2, T3> : ScBlockStates()
            where T1 : Comparable<T1>,
                  T2 : Comparable<T2>,
                  T3 : Comparable<T3>
    {
        private lateinit var prop1: Property<T1>
        private lateinit var prop2: Property<T2>
        private lateinit var prop3: Property<T3>

        protected fun create1(prop: Property<T1>, defaultValue: T1) { prop1 = create(prop, defaultValue) }
        protected fun create2(prop: Property<T2>, defaultValue: T2) { prop2 = create(prop, defaultValue) }
        protected fun create3(prop: Property<T3>, defaultValue: T3) { prop3 = create(prop, defaultValue) }

        fun variantBuilder(block: Block, init: VariantBuilder.(T1, T2, T3) -> List<Model>): BlockStateGenerator
        {
            val builder = VariantBuilder()
            val gen = MultiVariantGenerator.multiVariant(block)
            val propertyDispatch = PropertyDispatch.properties(prop1, prop2, prop3)
            gen.with(propertyDispatch)

            for (values in Product3(
                prop1.allValues.asSequence(),
                prop2.allValues.asSequence(),
                prop3.allValues.asSequence()))
            {
                val models = builder.init(values.a.value, values.b.value, values.c.value)
                propertyDispatch.select(values.a.value, values.b.value, values.c.value, models.map { it -> it.asVariant() })
            }
            return gen
        }
    }

    abstract class C4<T1, T2, T3, T4> : ScBlockStates()
            where T1 : Comparable<T1>,
                  T2 : Comparable<T2>,
                  T3 : Comparable<T3>,
                  T4 : Comparable<T4>
    {
        private lateinit var prop1: Property<T1>
        private lateinit var prop2: Property<T2>
        private lateinit var prop3: Property<T3>
        private lateinit var prop4: Property<T4>

        protected fun create1(prop: Property<T1>, defaultValue: T1) { prop1 = create(prop, defaultValue) }
        protected fun create2(prop: Property<T2>, defaultValue: T2) { prop2 = create(prop, defaultValue) }
        protected fun create3(prop: Property<T3>, defaultValue: T3) { prop3 = create(prop, defaultValue) }
        protected fun create4(prop: Property<T4>, defaultValue: T4) { prop4 = create(prop, defaultValue) }

        fun variantBuilder(block: Block, init: VariantBuilder.(T1, T2, T3, T4) -> List<Model>): BlockStateGenerator
        {
            val builder = VariantBuilder()
            val gen = MultiVariantGenerator.multiVariant(block)
            val propertyDispatch = PropertyDispatch.properties(prop1, prop2, prop3, prop4)
            gen.with(propertyDispatch)

            for (values in Product4(
                prop1.allValues.asSequence(),
                prop2.allValues.asSequence(),
                prop3.allValues.asSequence(),
                prop4.allValues.asSequence()))
            {
                val models = builder.init(values.a.value, values.b.value, values.c.value, values.d.value)
                propertyDispatch.select(values.a.value, values.b.value, values.c.value, values.d.value, models.map { it -> it.asVariant() })
            }
            return gen
        }
    }
}
