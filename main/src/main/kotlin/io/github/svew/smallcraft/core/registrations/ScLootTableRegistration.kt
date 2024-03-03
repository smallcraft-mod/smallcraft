package io.github.svew.smallcraft.core.registrations

import io.github.svew.smallcraft.core.ScBlockLootProvider
import net.minecraft.world.level.storage.loot.LootTable

interface ScLootTableRegistration
{
    /**
     * Loot table for this block
     * Set to null if the loot table exists in resources (json file)
     */
    fun addLootTable(reg: ScBlockLootProvider): LootTable.Builder?
}