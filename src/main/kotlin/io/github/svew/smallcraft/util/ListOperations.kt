package io.github.svew.smallcraft.util

data class Product2<A, B>(val a: A, val b: B)
data class Product3<A, B, C>(val a: A, val b: B, val c: C)
data class Product4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

fun <A, B> Iterable<A>.product(other: Iterable<B>): Iterable<Product2<A, B>>
{
    fold
}