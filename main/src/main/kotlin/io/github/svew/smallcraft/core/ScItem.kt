package io.github.svew.smallcraft.core

import io.github.svew.smallcraft.core.registrations.ScItemRegistration
import net.minecraft.world.item.Item

abstract class ScItem(val registration: ScItemRegistration<*>)
    : Item(registration.properties)