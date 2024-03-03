package io.github.svew.smallcraft.core.registrations

import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate

interface IScRegistration<T>
{
    val obj: T
    val id: String
}

abstract class ScRegistration<T> : IScRegistration<T>
{
    override val obj: T get() = holder?.get() ?: throw IllegalStateException("Object hasn't been registered yet")

    abstract val new: () -> T

    private var holder: ObjectHolderDelegate<T>? = null

    fun setObj(holder: ObjectHolderDelegate<T>)
    {
        if (this.holder != null) throw IllegalStateException("Object has already been registered")
        this.holder = holder
    }
}

val USE_RESOURCE_FILE = null
