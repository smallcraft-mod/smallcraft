package io.github.svew.smallcraft.core.registrations

import com.google.gson.JsonElement
import net.minecraft.data.models.blockstates.*
import net.minecraft.data.models.model.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

abstract class ScBlockRegistration<TBlock : Block>
    : ScRegistration<TBlock>()
    , ScLootTableRegistration
    , ScNameRegistration<TBlock>
{
    /**
     * Block behaviours
     */
    abstract val properties: BlockBehaviour.Properties
    /**
     * Tags to be applied to the block
     */
    open val tags: Array<TagKey<Block>> = arrayOf()
    /**
     * For the given block and block states
     */
    abstract val blockStateModels: (TBlock) -> BlockStateGenerator;
}

class Model(
    val resource: String,
    val x: VariantProperties.Rotation,
    val y: VariantProperties.Rotation,
    val uvlock: Boolean,
    val weight: Int)
{
    constructor(
        resourceSuffix: String,
        x: Int = 0,
        y: Int = 0,
        uvlock: Boolean = false,
        weight: Int = 1)
    : this(resourceSuffix, readRotation(x), readRotation(y), uvlock, readWeight(weight))

    fun asVariant(): Variant
    {
        return TODO()
    }

    companion object
    {
        private fun readRotation(rotation: Int) = when (rotation)
        {
            0 -> VariantProperties.Rotation.R0
            90 -> VariantProperties.Rotation.R90
            180 -> VariantProperties.Rotation.R180
            270 -> VariantProperties.Rotation.R270
            else -> throw Exception("Given rotation ${rotation} is not allowed")
        }

        private fun readWeight(weight: Int) = when
        {
            weight <= 0 -> throw Exception("Negative or zero weights are not allowed")
            else -> weight
        }
    }
}


















class XYZ
{
    lateinit var blockStateOutput: Consumer<BlockStateGenerator>
    lateinit var modelOutput: BiConsumer<ResourceLocation, Supplier<JsonElement>>


    fun cubeBottomTop(p_125827_: Block?): TextureMapping
    {
        return TextureMapping()
            .put(TextureSlot.SIDE,   TextureMapping.getBlockTexture(p_125827_, "_side"))
            .put(TextureSlot.TOP,    TextureMapping.getBlockTexture(p_125827_, "_top"))
            .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(p_125827_, "_bottom"))
    }

    fun cube(p_125777_: ResourceLocation?): TextureMapping
    {
        return TextureMapping().put(TextureSlot.ALL, p_125777_)
    }

    private fun createGrassBlocks()
    {
        val resourcelocation = TextureMapping.getBlockTexture(Blocks.DIRT)
        val texturemapping = TextureMapping()
            .put(        TextureSlot.BOTTOM, resourcelocation)
            .copyForced( TextureSlot.BOTTOM, TextureSlot.PARTICLE)
            .put(        TextureSlot.TOP,    TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_top"))
            .put(        TextureSlot.SIDE,   TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_snow"))

        val variant = Variant.variant()
            .with(VariantProperties.MODEL, ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(
                Blocks.GRASS_BLOCK,
                "_snow",
                texturemapping,
                this.modelOutput))
        this.createGrassLikeBlock(Blocks.GRASS_BLOCK, ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK), variant)

        val resourcelocation1 = TexturedModel.CUBE_TOP_BOTTOM[Blocks.MYCELIUM]
            .updateTextures { p_176198_: TextureMapping -> p_176198_.put(TextureSlot.BOTTOM, resourcelocation) }
            .create(Blocks.MYCELIUM, this.modelOutput)
        this.createGrassLikeBlock(Blocks.MYCELIUM, resourcelocation1, variant)

        val resourcelocation2 = TexturedModel.CUBE_TOP_BOTTOM[Blocks.PODZOL]
            .updateTextures { p_176154_: TextureMapping -> p_176154_.put(TextureSlot.BOTTOM, resourcelocation) }
            .create(Blocks.PODZOL, this.modelOutput)
        this.createGrassLikeBlock(Blocks.PODZOL, resourcelocation2, variant)
    }

    private fun createGrassLikeBlock(p_124600_: Block, p_124601_: ResourceLocation, p_124602_: Variant)
    {
        val list = createRotatedVariants(p_124601_).asList()
        blockStateOutput.accept(MultiVariantGenerator
            .multiVariant(p_124600_)
            .with(PropertyDispatch.property(BlockStateProperties.SNOWY)
                .select(true, p_124602_)
                .select(false, list))
        )
    }

    private fun createRotatedVariants(p_124689_: ResourceLocation): Array<Variant>
    {
        return arrayOf(
            Variant.variant().with(VariantProperties.MODEL, p_124689_),
            Variant.variant().with(VariantProperties.MODEL, p_124689_).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
            Variant.variant().with(VariantProperties.MODEL, p_124689_).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180),
            Variant.variant().with(VariantProperties.MODEL, p_124689_).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
        )
    }
}