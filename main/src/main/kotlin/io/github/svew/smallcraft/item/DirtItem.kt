package io.github.svew.smallcraft.item

import io.github.svew.smallcraft.core.registrations.ScItemRegistration
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraftforge.client.model.generators.ItemModelProvider

object DirtItemRegistration : ScItemRegistration<Item>()
{
    override val id = "dirt"
    override val name = "Dirt"
    override val new = { Item(this.properties) }
    override val properties: Item.Properties = Item.Properties()
    override val creativeModeTabs = setOf(CreativeModeTabs.INGREDIENTS)
    override fun addModel(provider: ItemModelProvider) = provider.basicItem(obj)!!
}
