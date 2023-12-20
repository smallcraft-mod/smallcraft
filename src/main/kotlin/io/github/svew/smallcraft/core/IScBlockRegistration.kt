package io.github.svew.smallcraft.core

import net.minecraft.world.level.block.state.BlockBehaviour

interface IScBlockRegistration
{
    val name: String
    val properties: BlockBehaviour.Properties
}
