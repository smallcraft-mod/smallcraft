package io.github.svew.smallcraft.core

import net.minecraft.world.item.Item

abstract class ScItem(val registration: ScItemRegistration<*>)
    : Item(registration.properties)