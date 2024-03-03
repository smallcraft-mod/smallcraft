package io.github.svew.smallcraft.gui

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable

interface ScRenderable : Renderable
{
    fun render(graphics: ScGuiGraphics, mouseX: Int, mouseY: Int, deltaTime: Float)

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, deltaTime: Float)
        = render(ScGuiGraphics(graphics), mouseX, mouseY, deltaTime)
}