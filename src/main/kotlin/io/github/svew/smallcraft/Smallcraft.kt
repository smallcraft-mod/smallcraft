
package io.github.svew.smallcraft
import io.github.svew.smallcraft.block.LadderBlockRegistration
import io.github.svew.smallcraft.block.RopeBlockRegistration
import io.github.svew.smallcraft.core.*
import io.github.svew.smallcraft.item.LadderItemRegistration
import io.github.svew.smallcraft.item.RopeItemRegistration
import io.github.svew.smallcraft.language.EnglishProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
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


@Mod(Smallcraft.MOD_ID)
object Smallcraft
{
    const val MOD_ID = "smallcraft"

    val LOGGER = LogManager.getLogger(MOD_ID)!!

    val REGISTRY_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID)!!
    val REGISTRY_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID)!!
    val REGISTRY_VANILLA_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "minecraft")!!
    val REGISTRY_VANILLA_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft")!!

    val ALL = mutableSetOf<ScRegistration<*>>()

    val ROPE_BLOCK by REGISTRY_BLOCKS.registerSc(RopeBlockRegistration)
    val ROPE_ITEM by REGISTRY_ITEMS.registerSc(RopeItemRegistration)

    val LADDER_BLOCK by REGISTRY_VANILLA_BLOCKS.registerSc(LadderBlockRegistration)
    val LADDER_ITEM by REGISTRY_VANILLA_ITEMS.registerSc(LadderItemRegistration)

    init
    {
        REGISTRY_BLOCKS.register(MOD_BUS)
        REGISTRY_ITEMS.register(MOD_BUS)
        REGISTRY_VANILLA_BLOCKS.register(MOD_BUS)
        REGISTRY_VANILLA_ITEMS.register(MOD_BUS)

        val creativeTabMap by lazy(
        {
            val map: MutableMap<ResourceKey<CreativeModeTab>, MutableList<ItemLike>> = mutableMapOf()
            for (itemReg in ALL.filterIsInstance<ScCreativeModeTabsRegistration<*>>())
            {
                for (creativeTab in itemReg.creativeModeTabs)
                {
                    val list = map.getOrPut(creativeTab, { mutableListOf() })
                    when (itemReg)
                    {
                        is ScItemRegistration<*> -> list.add(itemReg.obj)
                        is ScBlockItemRegistration<*> -> list.add(itemReg.obj)
                        else -> throw Exception("Found a creative tab registration that wasn't an itemlike?")
                    }
                }
            }
            map
        })

        MOD_BUS.addListener(fun(event: BuildCreativeModeTabContentsEvent)
        {
            val itemsToAdd = creativeTabMap.getOrDefault(event.tabKey, listOf())
            itemsToAdd.forEach({ item -> event.accept(item) })
        })

        MOD_BUS.register(object {
            @SubscribeEvent
            fun gatherData(event: GatherDataEvent) {
                val generator = event.generator
                val output = generator.packOutput
                val lookupProvider = event.lookupProvider
                val helper = event.existingFileHelper

                generator.addProvider(event.includeServer(), object : BlockTagsProvider(output, lookupProvider, MOD_ID, helper)
                {
                    override fun addTags(provider: HolderLookup.Provider)
                    {
                        for (reg in ALL.filterIsInstance<ScBlockRegistration<*>>())
                        {
                            for (blockTag in reg.tags)
                            {
                                tag(blockTag).add(reg.obj)
                            }
                        }
                    }
                })

                generator.addProvider(event.includeServer(), LootTableProvider(output, HashSet(), listOf(
                    LootTableProvider.SubProviderEntry({ ScLootTableRegistry(ALL.filterIsInstance<ScBlockRegistration<*>>()) }, LootContextParamSets.BLOCK)
                )))

                generator.addProvider(event.includeClient(), EnglishProvider(output))
            }
        })
    }


    private fun <T, V : T> DeferredRegister<T>.registerSc(registration: ScRegistration<V>): ObjectHolderDelegate<V>
    {
        ALL.add(registration)
        val regObj = this.registerObject(registration.id, registration.new)
        registration.setObj(regObj)
        return regObj
    }
}