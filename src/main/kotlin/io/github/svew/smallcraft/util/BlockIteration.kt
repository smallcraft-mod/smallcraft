package io.github.svew.smallcraft.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

interface IScanIteration
{
    /**
     * The current position of iteration.
     * First iteration gives the position of the given starting block
     */
    val currentPos: BlockPos
    /**
     * The current block state of the iteration.
     * First iteration gives the state of the given starting block
     */
    val state: BlockState
    /**
     * The number of blocks iterated so far.
     * First iteration starts at 0, representing the starting block.
     */
    val count: Int

    operator fun component1(): BlockPos = currentPos
    operator fun component2(): BlockState = state
    operator fun component3(): Int = count
}

class ScanIteration(
    private val level: LevelReader,
    override val currentPos: BlockPos,
    override val count: Int) : IScanIteration
{
    override val state: BlockState
        get() = level.getBlockState(currentPos)
}

/**
 * Returns iterator that linearly scans block positions in the given direction
 */
fun scanLinear(level: LevelReader, startingPos: BlockPos, direction: Direction) = object : Iterable<IScanIteration>
{
    override fun iterator() = object : Iterator<IScanIteration>
    {
        var nextIter: ScanIteration? = ScanIteration(level, startingPos, 0)

        override fun hasNext() = nextIter != null

        override fun next(): IScanIteration
        {
            val thisIter = nextIter ?: throw NoSuchElementException()
            val nextCandidate = ScanIteration(level, thisIter.currentPos.relative(direction), thisIter.count + 1)
            nextIter = null
            if (direction == Direction.UP   && nextCandidate.currentPos.y > level.maxBuildHeight) return thisIter
            if (direction == Direction.DOWN && nextCandidate.currentPos.y < level.minBuildHeight) return thisIter
            if (nextCandidate.count > 5000) throw Exception("Scanning has iterated over 5000 blocks in a row, it seems unlikely this is correct")
            nextIter = nextCandidate
            return thisIter
        }
    }
}

fun scanLinearWhile(
    level: LevelAccessor,
    startingPos: BlockPos,
    direction: Direction,
    predicate: (iter: IScanIteration) -> Boolean): Sequence<IScanIteration>
{
    return scanLinear(level, startingPos, direction)
        .asSequence()
        .takeWhile(predicate)
}

fun scanLinearWhile(
    level: LevelAccessor,
    startingPos: BlockPos,
    direction: Direction,
    blockPredicate: Block): Sequence<IScanIteration>
{
    return scanLinearWhile(level, startingPos, direction, { iter -> level.getBlockState(iter.currentPos).`is`(blockPredicate) })
}