
package io.github.svew.smallcraft
import io.github.svew.smallcraft.core.ScBlockLootProvider
import io.github.svew.smallcraft.core.ScCreativeModeTabsRegistration
import io.github.svew.smallcraft.core.ScRegistry
import io.github.svew.smallcraft.core.registrations.ScBlockRegistration
import io.github.svew.smallcraft.core.registrations.ScItemModelRegistration
import io.github.svew.smallcraft.core.registrations.ScNameRegistration
import io.github.svew.smallcraft.registries.ModBlocks
import io.github.svew.smallcraft.registries.ModItems
import io.github.svew.smallcraft.registries.VanillaBlocks
import io.github.svew.smallcraft.registries.VanillaItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.BlockTagsProvider
import net.minecraftforge.common.data.LanguageProvider
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Smallcraft.MOD_ID)
object Smallcraft
{
    const val MOD_ID = "smallcraft"

    @Suppress("unused")
    val LOGGER = LogManager.getLogger(MOD_ID)!!

    init
    {
        val modRegistries = listOf<ScRegistry<*>>(
            ModBlocks,
            ModItems,
        )
        val vanillaRegistry = listOf<ScRegistry<*>>(
            VanillaBlocks,
            VanillaItems,
        )
        val allRegistries = modRegistries + vanillaRegistry
        val allRegistrations by lazy { allRegistries.flatMap { it.all } }

        val creativeTabMap by lazy {
            val map: MutableMap<ResourceKey<CreativeModeTab>, MutableList<ItemLike>> = mutableMapOf()
            val allCreativeModeTabRegistrations = allRegistrations.filterIsInstance<ScCreativeModeTabsRegistration<*>>()

            for (itemReg in allCreativeModeTabRegistrations)
            {
                for (creativeTab in itemReg.creativeModeTabs)
                {
                    map.getOrPut(creativeTab, { mutableListOf() }).add(itemReg.obj)
                }
            }
            map
        }

        for (registry in allRegistries)
        {
            registry.registry.register(MOD_BUS) // pretty wordy...
        }

        MOD_BUS.addListener(fun(event: BuildCreativeModeTabContentsEvent)
        {
            creativeTabMap.getOrDefault(event.tabKey, listOf())
                .forEach({ item -> event.accept(item) })
        })

        MOD_BUS.register(object {
            @SubscribeEvent
            fun gatherData(event: GatherDataEvent) {
                val generator = event.generator
                val output = generator.packOutput
                val lookupProvider = event.lookupProvider
                val helper = event.existingFileHelper

                val allBlockRegistrations = allRegistrations.filterIsInstance<ScBlockRegistration<*>>()

                generator.addProvider(event.includeServer(), object : BlockTagsProvider(output, lookupProvider, MOD_ID, helper)
                {
                    override fun addTags(provider: HolderLookup.Provider)
                    {
                        for (reg in allBlockRegistrations)
                        {
                            for (blockTag in reg.tags)
                            {
                                tag(blockTag).add(reg.obj)
                            }
                        }
                    }
                })

                generator.addProvider(event.includeClient(), object : ItemModelProvider(output, MOD_ID, helper)
                {
                    override fun registerModels()
                    {
                        val allItemModelRegistrations = allRegistrations.filterIsInstance<ScItemModelRegistration>()
                        for (reg in allItemModelRegistrations)
                        {
                            reg.addModel(this)
                        }
                    }
                })

                generator.addProvider(event.includeServer(), LootTableProvider(output, HashSet(), listOf(
                    LootTableProvider.SubProviderEntry({ ScBlockLootProvider(allBlockRegistrations) }, LootContextParamSets.BLOCK)
                )))

                generator.addProvider(event.includeClient(), object : LanguageProvider(output, MOD_ID, "en_us")
                {
                    override fun addTranslations()
                    {
                        val allNameRegistrations = modRegistries.filterIsInstance<ScNameRegistration<*>>() // Only do modded name registrations
                        for (reg in allNameRegistrations)
                        {
                            val name = reg.name
                            when (val obj = reg.obj)
                            {
                                is BlockItem -> continue // Ignore block item, as block will register name for both
                                is Item -> this.addItem({ obj }, reg.name)
                                is Block -> this.addBlock({ obj }, reg.name)
                                else -> TODO("Haven't handled the case for type ${obj}")
                            }
                        }
                    }
                })
            }
        })
    }
}