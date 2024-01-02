package io.github.svew.smallcraft.util

interface EnumFlag

typealias EnumFlagValueType = Int

class EnumFlagTooLarge(enumName: String)
    : Exception("Enum ${enumName} is too large to fit within the size of the value type. ${EnumFlagValueType.SIZE_BITS} values are allowed at most.")

data class EnumFlagValue<T>(val value: EnumFlagValueType, private val clazz: Class<T>)
    : Comparable<EnumFlagValue<T>>
    where T : Enum<T>, T : EnumFlag
{
    init
    {
        if (clazz.enumConstants.size > EnumFlagValueType.SIZE_BITS)
        {
            throw EnumFlagTooLarge(clazz.name)
        }
    }

    fun toSet(): Set<T>
    {
        val collection = mutableSetOf<T>()
        addToCollection(collection)
        return collection
    }

    infix fun <T> EnumFlagValue<T>.with(other: T): EnumFlagValue<T>
            where T : Enum<T>, T : EnumFlag
    {
        return EnumFlagValue(this.value or other.rawFlagValue, this.clazz)
    }

    infix fun <T> EnumFlagValue<T>.with(other: EnumFlagValue<T>): EnumFlagValue<T>
            where T : Enum<T>, T : EnumFlag
    {
        return EnumFlagValue(this.value or other.value, this.clazz)
    }

    infix fun <T> EnumFlagValue<T>.without(other: T): EnumFlagValue<T>
            where T : Enum<T>, T : EnumFlag
    {
        return EnumFlagValue(this.value and other.rawFlagValue.inv(), this.clazz)
    }

    infix fun <T> EnumFlagValue<T>.without(other: EnumFlagValue<T>): EnumFlagValue<T>
            where T : Enum<T>, T : EnumFlag
    {
        return EnumFlagValue(this.value and other.value.inv(), this.clazz)
    }

    override fun compareTo(other: EnumFlagValue<T>): Int
    {
        return (this.value - other.value).toInt()
    }

    override fun toString(): String
    {
        val collection = mutableListOf<T>()
        addToCollection(collection)
        if (collection.size == 0) return ""
        return collection.joinToString(";")
    }

    private fun addToCollection(collection: MutableCollection<T>)
    {
        val enumValues = clazz.enumConstants

        var currValue = value
        var index = 0

        while (currValue > 0)
        {
            if ((currValue and 1) > 0)
            {
                collection.add(enumValues[index])
            }
            index += 1
            currValue = currValue ushr 1
        }
    }
}

inline val <reified T> T.flagValue: EnumFlagValue<T>
    where T : Enum<T>, T : EnumFlag
    get() = EnumFlagValue(this.rawFlagValue, T::class.java)

val <T> T.rawFlagValue: EnumFlagValueType
    where T : Enum<T>, T : EnumFlag
    get() = 1 shl this.ordinal

inline infix fun <reified T> T.with(other: T): EnumFlagValue<T>
    where T : Enum<T>, T : EnumFlag
{
    return EnumFlagValue(this.rawFlagValue or other.rawFlagValue, T::class.java)
}

inline fun <reified T> enumFlagAll(): EnumFlagValue<T>
    where T : Enum<T>, T : EnumFlag
{
    val clazz = T::class.java
    val unusedBitsCount = EnumFlagValueType.SIZE_BITS - clazz.enumConstants.size
    if (unusedBitsCount < 0) throw EnumFlagTooLarge(clazz.name)
    val bits = (0).inv() ushr unusedBitsCount
    return EnumFlagValue<T>(bits, clazz)
}

fun <T> enumFlagAll(clazz: Class<T>): EnumFlagValue<T>
    where T : Enum<T>, T : EnumFlag
{
    val unusedBitsCount = EnumFlagValueType.SIZE_BITS - clazz.enumConstants.size
    if (unusedBitsCount < 0) throw EnumFlagTooLarge(clazz.name)
    val bits = (0).inv() ushr unusedBitsCount
    return EnumFlagValue<T>(bits, clazz)
}

inline fun <reified T> enumFlagNone(): EnumFlagValue<T>
    where T : Enum<T>, T : EnumFlag
{
    return EnumFlagValue<T>(0, T::class.java)
}

fun <T> enumFlagNone(clazz: Class<T>): EnumFlagValue<T>
    where T : Enum<T>, T : EnumFlag
{
    return EnumFlagValue<T>(0, clazz)
}

