package io.github.svew.smallcraft.util

class EmptyIterator<T> : Iterator<T>
{
    override fun hasNext() = false

    override fun next() = throw IllegalStateException("Collection has no items")
}