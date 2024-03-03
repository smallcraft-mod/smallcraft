package io.github.svew.smallcraft.gui

data class Point(val x: Int, val y: Int)

data class Padding(val up: Int, val right: Int, val down: Int, val left: Int)
{
    constructor(all: Int) : this(all, all, all, all)
    constructor() : this(0)
}

enum class AlignmentX { CENTER, LEFT, RIGHT }
enum class AlignmentY { CENTER, UP, DOWN }

data class Alignment(val x: AlignmentX, val y: AlignmentY)
{
    constructor(x: String, y: String) : this(
        when (x) {
            "center" -> AlignmentX.CENTER
            "left" -> AlignmentX.LEFT
            "right" -> AlignmentX.RIGHT
            else -> throw IllegalArgumentException("Unrecognized argument ${x}, allowed values include: center, left, right")
        }, when (y) {
            "center" -> AlignmentY.CENTER
            "up" -> AlignmentY.UP
            "down" -> AlignmentY.DOWN
            else -> throw IllegalArgumentException("Unrecognized argument ${y}, allowed values include: center, up, down")
        })
    constructor() : this(AlignmentX.CENTER, AlignmentY.CENTER)
}

sealed class Style
{
    abstract fun render(graphics: ScGuiGraphics, xStart: Int, yStart: Int, xEnd: Int, yEnd: Int)
}

class NoStyle(
    val color: Color = Color.CLEAR) : Style()
{
    override fun render(graphics: ScGuiGraphics, xStart: Int, yStart: Int, xEnd: Int, yEnd: Int)
    {
        graphics.fill(xStart, yStart, xEnd, yEnd, color.packedValue)
    }
}

class ContainerStyle() : Style()
{
    object Colors
    {
        val FILL = Color(198,198,198)
        val OUTER_BORDER = Color.BLACK
        val LIGHT_BORDER = Color.WHITE
        val DARK_BORDER = Color(55,55,55)
        val TRANS_BORDER = Color.avg(LIGHT_BORDER, DARK_BORDER)
    }

    override fun render(graphics: ScGuiGraphics, xStart: Int, yStart: Int, xEnd: Int, yEnd: Int)
    {
        fun dot(x: Int, y: Int, color: Color)
        {
            graphics.fill(x, y, x + 1, y + 1, color.packedValue)
        }
        fun rect(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int, color: Color)
        {
            graphics.fill(xStart, yStart, xEnd, yEnd, color.packedValue)
        }

        rect(xStart, yStart, xEnd, yEnd, Colors.FILL)

        // Shadows
        rect(xStart - 1, yStart - 2, xEnd, yStart, Colors.LIGHT_BORDER)
        rect(xStart - 2, yStart, xStart, yEnd, Colors.LIGHT_BORDER)
        dot(xStart, yStart, Colors.LIGHT_BORDER)

        rect(xStart, yEnd, xEnd, yEnd + 2, Colors.DARK_BORDER)
        rect(xEnd, yStart, xEnd + 2, yEnd, Colors.DARK_BORDER)
        dot(xEnd - 1, yEnd - 1, Colors.DARK_BORDER)

        dot(xEnd, yStart - 1, Colors.TRANS_BORDER)
        dot(xStart - 1, yEnd, Colors.TRANS_BORDER)

        // Outer border
        rect(xStart - 1, yStart - 3, xEnd, yStart - 2, Colors.OUTER_BORDER)
        dot(xEnd, yStart - 2, Colors.OUTER_BORDER)
        dot(xEnd + 1, yStart - 1, Colors.OUTER_BORDER)

        rect(xEnd + 2, yStart, xEnd + 3, yEnd + 1, Colors.OUTER_BORDER)
        dot(xEnd + 1, yEnd + 1, Colors.OUTER_BORDER)

        //rect(xStart, yEnd + 2, xEnd + 4)
    }
}
/*
class InnerContainerStyle(
    val pushDown: Boolean,
    val bevel: Int,
    val color: Color) : Style()
{
    companion object
    {
        val LIGHT = Color(198,198,198)
        val DARK = Color(139,139,139)
    }
}

class Offset(val x: Int, val y: Int)
{
    constructor() : this(0, 0)
}

class SealedFrame(
    var width: Int,
    var height: Int,
    var float: Alignment = Alignment(), /* How this frame should align itself with its parent */
    var offset: Offset = Offset(), /* Offset of this element from its anchor point in the parent frame */
    var padding: Padding = Padding() /* Padding outside of frame, influenced by style */)

data class Margin(val up: Int, val right: Int, val down: Int, val left: Int)
{
    constructor(all: Int) : this(all, all, all, all)
}

class Frame(
    )*/