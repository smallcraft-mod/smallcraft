package io.github.svew.smallcraft.core

import io.github.svew.smallcraft.core.registrations.ScBlockRegistration
import io.github.svew.smallcraft.registries.ModBlocks
import io.github.svew.smallcraft.registries.VanillaBlocks
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.util.StringRepresentable
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.block.state.properties.SlabType
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider

class ScBlockLootProvider(private val registrations: List<ScBlockRegistration<*>>)
    : BlockLootSubProviderWrapper(setOf(), FeatureFlags.REGISTRY.allFlags())
{
    override fun generate()
    {
        for (reg in registrations)
        {
            val builder = reg.addLootTable(this) ?: continue
            this.add(reg.obj, builder)
        }
    }

    override fun getKnownBlocks() = (ModBlocks.all + VanillaBlocks.all)
        .map { it.obj }
        .toMutableList()
}

abstract class BlockLootSubProviderWrapper(explosionResistantSet: Set<Item>, featureFlags: FeatureFlagSet)
    : BlockLootSubProvider(explosionResistantSet, featureFlags)
{
    public override fun createSingleItemTable(item: ItemLike): LootTable.Builder
    {
        return LootTable.lootTable()
            .withPool(this.applyExplosionCondition(item, LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(LootItem.lootTableItem(item))))
    }

    public override fun createSingleItemTable(item: ItemLike, count: NumberProvider): LootTable.Builder
    {
        return LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(this.applyExplosionDecay(item, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(count)))));
    }

    fun createSingleItemTable(item: ItemLike, count: Int): LootTable.Builder
    {
        return createSingleItemTable(item, ConstantValue.exactly(count.toFloat()))
    }


    public override fun createSingleItemTableWithSilkTouch(block: Block, item: ItemLike): LootTable.Builder
    {
        return BlockLootSubProvider.createSilkTouchDispatchTable(
            block,
            this.applyExplosionCondition(block, LootItem.lootTableItem(item))
        );
    }

    public override fun createSingleItemTableWithSilkTouch(
        block: Block,
        item: ItemLike,
        count: NumberProvider
    ): LootTable.Builder
    {
        return BlockLootSubProvider.createSilkTouchDispatchTable(
            block, this.applyExplosionDecay(
                block, LootItem.lootTableItem(item)
                    .apply(SetItemCountFunction.setCount(count))
            )
        )
    }

    public override fun createPotFlowerItemTable(item: ItemLike): LootTable.Builder
    {
        return LootTable.lootTable()
            .withPool(this.applyExplosionCondition(
                Blocks.FLOWER_POT, LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(Blocks.FLOWER_POT))))
            .withPool(this.applyExplosionCondition(item, LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(item))));
    }

    public override fun createSlabItemTable(block: Block): LootTable.Builder
    {
        return LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(this.applyExplosionDecay(block, LootItem.lootTableItem(block)
                    .apply(
                        SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                        .`when`(
                            LootItemBlockStatePropertyCondition
                            .hasBlockStateProperties(block)
                            .setProperties(
                                StatePropertiesPredicate.Builder.properties()
                                .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))))));
    }

    public override fun <T> createSinglePropConditionTable(
        block: Block,
        property: Property<T>,
        conditionValue: T
    ): LootTable.Builder where T : Comparable<T>, T : StringRepresentable?
    {
        return LootTable.lootTable()
            .withPool(this.applyExplosionCondition(block, LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(
                    LootItem.lootTableItem(block)
                    .`when`(
                        LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(block)
                        .setProperties(
                            StatePropertiesPredicate.Builder.properties()
                            .hasProperty(property, conditionValue))))));
    }

    public override fun createNameableBlockEntityTable(p_252291_: Block): LootTable.Builder
    {
        return super.createNameableBlockEntityTable(p_252291_)
    }

    public override fun createShulkerBoxDrop(p_252164_: Block): LootTable.Builder
    {
        return super.createShulkerBoxDrop(p_252164_)
    }

    public override fun createCopperOreDrops(p_251306_: Block): LootTable.Builder
    {
        return super.createCopperOreDrops(p_251306_)
    }

    public override fun createLapisOreDrops(p_251511_: Block): LootTable.Builder
    {
        return super.createLapisOreDrops(p_251511_)
    }

    public override fun createRedstoneOreDrops(p_251906_: Block): LootTable.Builder
    {
        return super.createRedstoneOreDrops(p_251906_)
    }

    public override fun createBannerDrop(p_249810_: Block): LootTable.Builder
    {
        return super.createBannerDrop(p_249810_)
    }

    public override fun createOreDrop(p_250450_: Block, p_249745_: Item): LootTable.Builder
    {
        return super.createOreDrop(p_250450_, p_249745_)
    }

    public override fun createMushroomBlockDrop(p_249959_: Block, p_249315_: ItemLike): LootTable.Builder
    {
        return super.createMushroomBlockDrop(p_249959_, p_249315_)
    }

    public override fun createGrassDrops(p_252139_: Block): LootTable.Builder
    {
        return super.createGrassDrops(p_252139_)
    }

    public override fun createStemDrops(p_250957_: Block, p_249098_: Item): LootTable.Builder
    {
        return super.createStemDrops(p_250957_, p_249098_)
    }

    public override fun createAttachedStemDrops(p_249778_: Block, p_250678_: Item): LootTable.Builder
    {
        return super.createAttachedStemDrops(p_249778_, p_250678_)
    }

    public override fun createMultifaceBlockDrops(p_249088_: Block, p_251535_: LootItemCondition.Builder): LootTable.Builder
    {
        return super.createMultifaceBlockDrops(p_249088_, p_251535_)
    }

    public override fun createLeavesDrops(p_250088_: Block, p_250731_: Block, vararg p_248949_: Float): LootTable.Builder
    {
        return super.createLeavesDrops(p_250088_, p_250731_, *p_248949_)
    }

    public override fun createOakLeavesDrops(p_249535_: Block, p_251505_: Block, vararg p_250753_: Float): LootTable.Builder
    {
        return super.createOakLeavesDrops(p_249535_, p_251505_, *p_250753_)
    }

    public override fun createMangroveLeavesDrops(p_251103_: Block): LootTable.Builder
    {
        return super.createMangroveLeavesDrops(p_251103_)
    }

    public override fun createCropDrops(
        p_249457_: Block,
        p_248599_: Item,
        p_251915_: Item,
        p_252202_: LootItemCondition.Builder
    ): LootTable.Builder
    {
        return super.createCropDrops(p_249457_, p_248599_, p_251915_, p_252202_)
    }

    public override fun createDoublePlantWithSeedDrops(p_248590_: Block, p_248735_: Block): LootTable.Builder
    {
        return super.createDoublePlantWithSeedDrops(p_248590_, p_248735_)
    }

    public override fun createCandleDrops(p_250896_: Block): LootTable.Builder
    {
        return super.createCandleDrops(p_250896_)
    }

    public override fun createPetalsDrops(p_273240_: Block): LootTable.Builder
    {
        return super.createPetalsDrops(p_273240_)
    }

    public override fun createDoorTable(p_252166_: Block): LootTable.Builder
    {
        return super.createDoorTable(p_252166_)
    }
}
