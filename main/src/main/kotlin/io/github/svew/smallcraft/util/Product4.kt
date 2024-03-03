package io.github.svew.smallcraft.util

class Product4Iterator<A, B, C, D>(
        private val source: Product4<A, B, C, D>,
        private var aIter: Iterator<A>,
        private var bIter: Iterator<B>,
        private var cIter: Iterator<C>,
        private val dIter: Iterator<D>,
        private var bCurr: B,
        private var cCurr: C,
        private var dCurr: D)
    : Iterator<Tuple4<A, B, C, D>>
{
    override fun hasNext(): Boolean
    {
        if (aIter.hasNext()) return true
        if (bIter.hasNext()) return true
        if (cIter.hasNext()) return true
        if (dIter.hasNext()) return true
        return false
    }

    override fun next(): Tuple4<A, B, C, D>
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
            if (!cIter.hasNext()) {
                cIter = source.c.iterator()
            }
            return cIter.next()
        }
        fun incrementD(): D {
            dCurr = dIter.next()
            return dCurr
        }

        return when {
            aIter.hasNext() -> Tuple4(incrementA(), bCurr,        cCurr,        dCurr)
            bIter.hasNext() -> Tuple4(incrementA(), incrementB(), cCurr,        dCurr)
            cIter.hasNext() -> Tuple4(incrementA(), incrementB(), incrementC(), dCurr)
            dIter.hasNext() -> Tuple4(incrementA(), incrementB(), incrementC(), incrementD())
            else -> throw NoSuchElementException()
        }
    }
}

class Product4<A, B, C, D>(val a: Sequence<A>, val b: Sequence<B>, val c: Sequence<C>, val d: Sequence<D>)
    : Sequence<Tuple4<A, B, C, D>>
{
    override fun iterator() : Iterator<Tuple4<A, B, C, D>>
    {
        val aIter = a.iterator()
        if (!aIter.hasNext()) return EmptyIterator()
        val bIter = b.iterator()
        if (!bIter.hasNext()) return EmptyIterator()
        val cIter = c.iterator()
        if (!cIter.hasNext()) return EmptyIterator()
        val dIter = d.iterator()
        if (!dIter.hasNext()) return EmptyIterator()

        return Product4Iterator(this, aIter, bIter, cIter, dIter, bIter.next(), cIter.next(), dIter.next())
    }
}

infix fun <A, B, C, D> Product3<A, B, C>.product(other: Sequence<D>): Product4<A, B, C, D>
{
    return Product4(this.a, this.b, this.c, other)
}