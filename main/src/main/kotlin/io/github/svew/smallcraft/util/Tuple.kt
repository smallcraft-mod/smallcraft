package io.github.svew.smallcraft.util

interface ITuple
{
    val size: Int
}

data class Tuple1<A>(val a: A) : ITuple
{
    override val size get() = 1
}

data class Tuple2<A, B>(val a: A, val b: B) : ITuple
{
    override val size get() = 2
}

data class Tuple3<A, B, C>(val a: A, val b: B, val c: C) : ITuple
{
    override val size get() = 3
}

data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D) : ITuple
{
    override val size get() = 4
}
