package io.github.svew.smallcraft.util

class Product3Iterator<A, B, C>(
        private val source: Product3<A, B, C>,
        private var aIter: Iterator<A>,
        private var bIter: Iterator<B>,
        private val cIter: Iterator<C>,
        private var bCurr: B,
        private var cCurr: C)
    : Iterator<Tuple3<A, B, C>>
{
    override fun hasNext(): Boolean
    {
        if (aIter.hasNext()) return true
        if (bIter.hasNext()) return true
        if (cIter.hasNext()) return true
        return false
    }

    override fun next(): Tuple3<A, B, C>
    {
        fun incrementA(): A {
            if (!aIter.hasNext()) {
                aIter = source.a.iterator()
            }
            return aIter.next()
        }
        fun incrementB(): B {
            if (!bIter.hasNext()) {
                bIter = source.b.iterator()
            }
            return bIter.next()
        }
        fun incrementC(): C {
            cCurr = cIter.next()
            return cCurr
        }

        return when {
            aIter.hasNext() -> Tuple3(incrementA(), bCurr,        cCurr)
            bIter.hasNext() -> Tuple3(incrementA(), incrementB(), cCurr)
            cIter.hasNext() -> Tuple3(incrementA(), incrementB(), incrementC())
            else -> throw NoSuchElementException()
        }
    }
}

class Product3<A, B, C>(val a: Iterable<A>, val b: Iterable<B>, val c: Iterable<C>)
    : Iterable<Tuple3<A, B, C>>
{
    override fun iterator() : Iterator<Tuple3<A, B, C>>
    {
        val aIter = a.iterator()
        if (!aIter.hasNext()) return EmptyIterator()
        val bIter = b.iterator()
        if (!bIter.hasNext()) return EmptyIterator()
        val cIter = c.iterator()
        if (!cIter.hasNext()) return EmptyIterator()

        return Product3Iterator(this, aIter, bIter, cIter, bIter.next(), cIter.next())
    }
}

infix fun <A, B, C> Product2<A, B>.product(other: Iterable<C>): Product3<A, B, C>
{
    return Product3(this.a, this.b, other)
}