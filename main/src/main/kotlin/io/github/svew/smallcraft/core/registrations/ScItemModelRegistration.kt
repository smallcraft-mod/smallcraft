package io.github.svew.smallcraft.core.registrations

import net.minecraftforge.client.model.generators.ItemModelBuilder
import net.minecraftforge.client.model.generators.ItemModelProvider

interface ScItemModelRegistration
{
    /**
     * Provide the model of the item
     * If set to null, uses resource file instead
     */
    fun addModel(provider: ItemModelProvider): ItemModelBuilder?
}