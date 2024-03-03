package io.github.svew.smallcraft.core

import io.github.svew.smallcraft.core.registrations.ScRegistration
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

abstract class ScRegistry<T>(registry: IForgeRegistry<T>, namespace: String)
{
    private val allRegistrations = mutableSetOf<ScRegistration<out T>>()

    val registry: DeferredRegister<T> = DeferredRegister.create(registry, namespace)
    val all: Set<ScRegistration<out T>> get() = allRegistrations

    protected fun <V> register(registration: ScRegistration<V>): ObjectHolderDelegate<V> where V : T
    {
        allRegistrations.add(registration)
        val regObj = registry.registerObject(registration.id, registration.new)
        registration.setObj(regObj)
        return regObj
    }
}