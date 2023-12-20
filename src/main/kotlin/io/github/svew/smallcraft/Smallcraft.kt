
package io.github.svew.smallcraft
import io.github.svew.smallcraft.block.RopeBlock
import io.github.svew.smallcraft.block.RopeBlockRegistration
import io.github.svew.smallcraft.core.IScBlockRegistration
import io.github.svew.smallcraft.core.IScBlockTagsRegistration
import io.github.svew.smallcraft.core.IScLootTableRegistration
import io.github.svew.smallcraft.core.ScBlock
import io.github.svew.smallcraft.item.RopeItem
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraftforge.common.data.BlockTagsProvider
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

@Mod(Smallcraft.MODID)
object Smallcraft {

    const val MODID = "smallcraft"

    val LOGGER = LogManager.getLogger(MODID)!!

    val REGISTRY_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID)
    val REGISTRY_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID)

    val BLOCK_ROPE by registerBlock(RopeBlockRegistration, { RopeBlock })
    val ITEM_ROPE by REGISTRY_ITEMS.registerObject("rope", { RopeItem(BLOCK_ROPE) })

    private val lootTableRegistrations = mutableSetOf<IScLootTableRegistration>()
    private val blockTagsRegistrations = mutableSetOf<IScBlockTagsRegistration>()

    init
    {
        REGISTRY_BLOCKS.register(MOD_BUS)
        REGISTRY_ITEMS.register(MOD_BUS)
        
        MOD_BUS.addListener(fun(event: BuildCreativeModeTabContentsEvent?){
            when (event!!.tabKey) {
                CreativeModeTabs.BUILDING_BLOCKS -> event.accept(ITEM_ROPE)
            }
        })

        MOD_BUS.register(object {
            @SubscribeEvent
            fun gatherData(event: GatherDataEvent) {
                val generator = event.generator
                val output = generator.packOutput
                val lookupProvider = event.lookupProvider
                val helper = event.existingFileHelper

                generator.addProvider(event.includeServer(), object : BlockTagsProvider(output, lookupProvider, MODID, helper)
                {
                    override fun addTags(provider: HolderLookup.Provider)
                    {
                        for (registration in blockTagsRegistrations)
                        {
                            for (blockTag in registration.tags)
                            {

                            }
                        }
                    }
                })
                generator.addProvider(event.includeServer(), LootTableProvider(output, HashSet(), listOf(
                    LootTableProvider.SubProviderEntry({ object : BlockLootSubProvider(HashSet(), FeatureFlags.REGISTRY.allFlags()) {
                        val generatedLootTables: HashSet<Block> = HashSet()
                        override fun generate() {
                            dropSelf(BLOCK_ROPE)
                        }
                        override fun add(block: Block, builder: LootTable.Builder) {
                            generatedLootTables.add(block)
                            map[block.lootTable] = builder
                        }
                        override fun getKnownBlocks(): Iterable<Block> {
                            return generatedLootTables
                        }
                    }}, LootContextParamSets.BLOCK)
                )))
            }
        })

        MOD_BUS.register(object {

        })
    }

    private fun <T : Block> registerBlock(registration: IScBlockRegistration, supplier: () -> T): ObjectHolderDelegate<T>
    {
        REGISTRY_BLOCKS.registerObject(registration.name, supplier)
        if (registration is IScLootTableRegistration)
        {
            lootTableRegistrations.add(registration)
        }
        if (registration is IScBlockTagsRegistration)
        {
            blockTagsRegistrations.add(registration)
        }
    }
}