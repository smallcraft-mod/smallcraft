package io.github.svew.smallcraft.util

class Product2Iterator<A, B>(
        private val source: Product2<A, B>,
        private var aIter: Iterator<A>,
        private val bIter: Iterator<B>,
        private var bCurr: B)
    : Iterator<Tuple2<A, B>>
{
    override fun hasNext(): Boolean
    {
        if (aIter.hasNext()) return true
        if (bIter.hasNext()) return true
        return false
    }

    override fun next(): Tuple2<A, B>
    {
        fun incrementA(): A {
            if (!aIter.hasNext()) {
                aIter = source.a.iterator()
            }
            return aIter.next()
        }
        fun incrementB(): B {
            bCurr = bIter.next()
            return bCurr
        }

        return when {
            aIter.hasNext() -> Tuple2(incrementA(), bCurr)
            bIter.hasNext() -> Tuple2(incrementA(), incrementB())
            else -> throw NoSuchElementException()
        }
    }
}

class Product2<A, B>(val a: Iterable<A>, val b: Iterable<B>)
    : Iterable<Tuple2<A, B>>
{
    override fun iterator() : Iterator<Tuple2<A, B>>
    {
        val aIter = a.iterator()
        if (!aIter.hasNext()) return EmptyIterator()
        val bIter = b.iterator()
        if (!bIter.hasNext()) return EmptyIterator()

        return Product2Iterator(this, aIter, bIter, bIter.next())
    }
}

infix fun <A, B> Iterable<A>.product(other: Iterable<B>): Iterable<Tuple2<A, B>>
{
    return Product2(this, other)
}