package io.github.svew.smallcraft.core

import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider
import java.util.function.BiConsumer
import java.util.function.Function

interface IScLootTableRegistration {
    fun registerLootTable(registry: ScLootTableRegistry)
}

class ScLootTableRegistry(
    private val registrations: List<IScLootTableRegistration>,
    explosionResistant: Set<Item>,
    flagSet: FeatureFlagSet,
    map: Map<ResourceLocation, LootTable.Builder>)
    : BlockLootSubProvider(explosionResistant, flagSet, map)
{
    override fun generate() {
        for (registration in registrations) {
            registration.registerLootTable(this)
        }
    }

    public override fun generate(p_249322_: BiConsumer<ResourceLocation, LootTable.Builder>) {
        super.generate(p_249322_)
    }

    public override fun <T : FunctionUserBuilder<T>?> applyExplosionDecay(
        p_248695_: ItemLike,
        p_248548_: FunctionUserBuilder<T>
    ): T {
        return super.applyExplosionDecay(p_248695_, p_248548_)
    }

    public override fun <T : ConditionUserBuilder<T>?> applyExplosionCondition(
        p_249717_: ItemLike,
        p_248851_: ConditionUserBuilder<T>
    ): T {
        return super.applyExplosionCondition(p_249717_, p_248851_)
    }

    public override fun createSingleItemTable(p_251912_: ItemLike): LootTable.Builder {
        return super.createSingleItemTable(p_251912_)
    }

    public override fun createSingleItemTable(p_251584_: ItemLike, p_249865_: NumberProvider): LootTable.Builder {
        return super.createSingleItemTable(p_251584_, p_249865_)
    }

    public override fun createSingleItemTableWithSilkTouch(p_249305_: Block, p_251905_: ItemLike): LootTable.Builder {
        return super.createSingleItemTableWithSilkTouch(p_249305_, p_251905_)
    }

    public override fun createSingleItemTableWithSilkTouch(
        p_251449_: Block,
        p_248558_: ItemLike,
        p_250047_: NumberProvider
    ): LootTable.Builder {
        return super.createSingleItemTableWithSilkTouch(p_251449_, p_248558_, p_250047_)
    }

    public override fun createPotFlowerItemTable(p_249395_: ItemLike): LootTable.Builder {
        return super.createPotFlowerItemTable(p_249395_)
    }

    public override fun createSlabItemTable(p_251313_: Block): LootTable.Builder {
        return super.createSlabItemTable(p_251313_)
    }

    public override fun <T> createSinglePropConditionTable(
        p_252154_: Block,
        p_250272_: Property<T>,
        p_250292_: T
    ): LootTable.Builder
        where T : Comparable<T>, T : StringRepresentable?
    {
        return super.createSinglePropConditionTable(p_252154_, p_250272_, p_250292_)
    }

    public override fun createNameableBlockEntityTable(p_252291_: Block): LootTable.Builder {
        return super.createNameableBlockEntityTable(p_252291_)
    }

    public override fun createShulkerBoxDrop(p_252164_: Block): LootTable.Builder {
        return super.createShulkerBoxDrop(p_252164_)
    }

    public override fun createCopperOreDrops(p_251306_: Block): LootTable.Builder {
        return super.createCopperOreDrops(p_251306_)
    }

    public override fun createLapisOreDrops(p_251511_: Block): LootTable.Builder {
        return super.createLapisOreDrops(p_251511_)
    }

    public override fun createRedstoneOreDrops(p_251906_: Block): LootTable.Builder {
        return super.createRedstoneOreDrops(p_251906_)
    }

    public override fun createBannerDrop(p_249810_: Block): LootTable.Builder {
        return super.createBannerDrop(p_249810_)
    }

    public override fun createOreDrop(p_250450_: Block, p_249745_: Item): LootTable.Builder {
        return super.createOreDrop(p_250450_, p_249745_)
    }

    public override fun createMushroomBlockDrop(p_249959_: Block, p_249315_: ItemLike): LootTable.Builder {
        return super.createMushroomBlockDrop(p_249959_, p_249315_)
    }

    public override fun createGrassDrops(p_252139_: Block): LootTable.Builder {
        return super.createGrassDrops(p_252139_)
    }

    public override fun createStemDrops(p_250957_: Block, p_249098_: Item): LootTable.Builder {
        return super.createStemDrops(p_250957_, p_249098_)
    }

    public override fun createAttachedStemDrops(p_249778_: Block, p_250678_: Item): LootTable.Builder {
        return super.createAttachedStemDrops(p_249778_, p_250678_)
    }

    public override fun createMultifaceBlockDrops(p_249088_: Block, p_251535_: LootItemCondition.Builder): LootTable.Builder {
        return super.createMultifaceBlockDrops(p_249088_, p_251535_)
    }

    public override fun createLeavesDrops(p_250088_: Block, p_250731_: Block, vararg p_248949_: Float): LootTable.Builder {
        return super.createLeavesDrops(p_250088_, p_250731_, *p_248949_)
    }

    public override fun createOakLeavesDrops(p_249535_: Block, p_251505_: Block, vararg p_250753_: Float): LootTable.Builder {
        return super.createOakLeavesDrops(p_249535_, p_251505_, *p_250753_)
    }

    public override fun createMangroveLeavesDrops(p_251103_: Block): LootTable.Builder {
        return super.createMangroveLeavesDrops(p_251103_)
    }

    public override fun createCropDrops(
        p_249457_: Block,
        p_248599_: Item,
        p_251915_: Item,
        p_252202_: LootItemCondition.Builder
    ): LootTable.Builder {
        return super.createCropDrops(p_249457_, p_248599_, p_251915_, p_252202_)
    }

    public override fun createDoublePlantWithSeedDrops(p_248590_: Block, p_248735_: Block): LootTable.Builder {
        return super.createDoublePlantWithSeedDrops(p_248590_, p_248735_)
    }

    public override fun createCandleDrops(p_250896_: Block): LootTable.Builder {
        return super.createCandleDrops(p_250896_)
    }

    public override fun createPetalsDrops(p_273240_: Block): LootTable.Builder {
        return super.createPetalsDrops(p_273240_)
    }

    public override fun getKnownBlocks(): MutableIterable<Block> {
        return super.getKnownBlocks()
    }

    public override fun addNetherVinesDropTable(p_252269_: Block, p_250696_: Block) {
        super.addNetherVinesDropTable(p_252269_, p_250696_)
    }

    public override fun createDoorTable(p_252166_: Block): LootTable.Builder {
        return super.createDoorTable(p_252166_)
    }

    public override fun dropPottedContents(p_251064_: Block) {
        super.dropPottedContents(p_251064_)
    }

    public override fun otherWhenSilkTouch(p_249932_: Block, p_252053_: Block) {
        super.otherWhenSilkTouch(p_249932_, p_252053_)
    }

    public override fun dropOther(p_248885_: Block, p_251883_: ItemLike) {
        super.dropOther(p_248885_, p_251883_)
    }

    public override fun dropWhenSilkTouch(p_250855_: Block) {
        super.dropWhenSilkTouch(p_250855_)
    }

    public override fun dropSelf(p_249181_: Block) {
        super.dropSelf(p_249181_)
    }

    public override fun add(p_251966_: Block, p_251699_: Function<Block, LootTable.Builder>) {
        super.add(p_251966_, p_251699_)
    }

    public override fun add(p_250610_: Block, p_249817_: LootTable.Builder) {
        super.add(p_250610_, p_249817_)
    }
}
