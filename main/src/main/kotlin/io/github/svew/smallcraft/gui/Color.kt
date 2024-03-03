package io.github.svew.smallcraft.gui

data class Color(val r: Int, val g: Int, val b: Int, val a: Int = 0)
{
    val packedValue: Int get() = (a shl 24) or (r shl 16) or (g shl 8) or b

    init
    {
        require(r in 0..255
             && g in 0..255
             && b in 0..255
             && a in 0..255) { "Could not create color ${this}, found color values out of range 0 <= x <= 255" }
    }

    companion object
    {
        val WHITE = Color(255, 255, 255)
        val BLACK = Color(0, 0, 0)
        val CLEAR = Color(0, 0, 0, 255)

        fun avg(c1: Color, c2: Color): Color
        {
            return Color((c1.r + c2.r) / 2, (c1.g + c2.g) / 2, (c1.b + c2.b) / 2, (c1.a + c2.a) / 2)
        }
    }
}