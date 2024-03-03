package io.github.svew.smallcraft.gui

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner
import net.minecraft.client.renderer.MultiBufferSource.BufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.FormattedCharSequence
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import net.minecraftforge.client.extensions.IForgeGuiGraphics
import java.util.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
@JvmInline
value class ScGuiGraphics(private val self: GuiGraphics) : IForgeGuiGraphics
{
    fun drawManaged(runnable: Runnable) = self.drawManaged(runnable)
    fun guiWidth(): Int = self.guiWidth()
    fun guiHeight(): Int = self.guiHeight()
    fun pose(): PoseStack = self.pose()
    fun bufferSource(): BufferSource = self.bufferSource()
    fun flush() = self.flush()

    fun hLine(xStart: Int, xEnd: Int, y: Int, color: Int)
        = self.hLine(xStart, xEnd, y, color)

    fun hLine(renderType: RenderType, xStart: Int, xEnd: Int, y: Int, color: Int)
        = self.hLine(renderType, xStart, xEnd, y, color)

    fun vLine(x: Int, yStart: Int, yEnd: Int, color: Int)
        = self.vLine(x, yStart, yEnd, color)

    fun vLine(renderType: RenderType, x: Int, yStart: Int, yEnd: Int, color: Int)
        = self.vLine(renderType, x, yStart, yEnd, color)

    fun enableScissor(p_281479_: Int, p_282788_: Int, p_282924_: Int, p_282826_: Int)
        = self.enableScissor(p_281479_, p_282788_, p_282924_, p_282826_)

    fun disableScissor() = self.disableScissor()

    fun setColor(p_281272_: Float, p_281734_: Float, p_282022_: Float, p_281752_: Float)
        = self.setColor(p_281272_, p_281734_, p_282022_, p_281752_)

    fun fill(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int, color: Int)
        = this.fill(xStart, yStart, xEnd, yEnd, 0, color)

    fun fill(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int, z: Int, color: Int)
        = this.fill(RenderType.gui(), xStart, yStart, xEnd, yEnd, z, color)

    fun fill(
        renderType: RenderType,
        xStart: Int,
        yStart: Int,
        xEnd: Int,
        yEnd: Int,
        color: Int
    ) = this.fill(renderType, xStart, yStart, xEnd, yEnd, 0, color)

    fun fill(
        renderType: RenderType,
        xStart: Int,
        yStart: Int,
        xEnd: Int,
        yEnd: Int,
        z: Int,
        color: Int
    ) = self.fill(renderType, xStart, yStart, xEnd, yEnd, z, color)

    fun fillGradient(
        p_283290_: Int,
        p_283278_: Int,
        p_282670_: Int,
        p_281698_: Int,
        p_283374_: Int,
        p_283076_: Int
    ) = self.fillGradient(p_283290_, p_283278_, p_282670_, p_281698_, p_283374_, p_283076_)

    fun fillGradient(
        p_282702_: Int,
        p_282331_: Int,
        p_281415_: Int,
        p_283118_: Int,
        p_282419_: Int,
        p_281954_: Int,
        p_282607_: Int
    )
    {
        self.fillGradient(p_282702_, p_282331_, p_281415_, p_283118_, p_282419_, p_281954_, p_282607_)
    }

    fun fillGradient(
        p_286522_: RenderType,
        p_286535_: Int,
        p_286839_: Int,
        p_286242_: Int,
        p_286856_: Int,
        p_286809_: Int,
        p_286833_: Int,
        p_286706_: Int
    )
    {
        self.fillGradient(p_286522_, p_286535_, p_286839_, p_286242_, p_286856_, p_286809_, p_286833_, p_286706_)
    }

    fun drawCenteredString(p_282122_: Font, p_282898_: String, p_281490_: Int, p_282853_: Int, p_281258_: Int)
    {
        self.drawCenteredString(p_282122_, p_282898_, p_281490_, p_282853_, p_281258_)
    }

    fun drawCenteredString(
        p_282901_: Font,
        p_282456_: Component,
        p_283083_: Int,
        p_282276_: Int,
        p_281457_: Int
    )
    {
        self.drawCenteredString(p_282901_, p_282456_, p_283083_, p_282276_, p_281457_)
    }

    fun drawCenteredString(
        p_282592_: Font,
        p_281854_: FormattedCharSequence,
        p_281573_: Int,
        p_283511_: Int,
        p_282577_: Int
    )
    {
        self.drawCenteredString(p_282592_, p_281854_, p_281573_, p_283511_, p_282577_)
    }

    fun drawString(p_282003_: Font, p_281403_: String?, p_282714_: Int, p_282041_: Int, p_281908_: Int): Int
    {
        return self.drawString(p_282003_, p_281403_, p_282714_, p_282041_, p_281908_)
    }

    fun drawString(
        p_283343_: Font,
        p_281896_: String?,
        p_283569_: Int,
        p_283418_: Int,
        p_281560_: Int,
        p_282130_: Boolean
    ): Int
    {
        return self.drawString(p_283343_, p_281896_, p_283569_, p_283418_, p_281560_, p_282130_)
    }

    fun drawString(
        p_283343_: Font,
        p_281896_: String?,
        p_283569_: Float,
        p_283418_: Float,
        p_281560_: Int,
        p_282130_: Boolean
    ): Int
    {
        return self.drawString(p_283343_, p_281896_, p_283569_, p_283418_, p_281560_, p_282130_)
    }

    fun drawString(
        p_283019_: Font,
        p_283376_: FormattedCharSequence,
        p_283379_: Int,
        p_283346_: Int,
        p_282119_: Int
    ): Int
    {
        return self.drawString(p_283019_, p_283376_, p_283379_, p_283346_, p_282119_)
    }

    fun drawString(
        p_282636_: Font,
        p_281596_: FormattedCharSequence,
        p_281586_: Int,
        p_282816_: Int,
        p_281743_: Int,
        p_282394_: Boolean
    ): Int = self.drawString(p_282636_, p_281596_, p_281586_, p_282816_, p_281743_, p_282394_)

    fun drawString(
        p_282636_: Font,
        p_281596_: FormattedCharSequence,
        p_281586_: Float,
        p_282816_: Float,
        p_281743_: Int,
        p_282394_: Boolean
    ): Int = self.drawString(p_282636_, p_281596_, p_281586_, p_282816_, p_281743_, p_282394_)

    fun drawString(p_281653_: Font, p_283140_: Component, p_283102_: Int, p_282347_: Int, p_281429_: Int): Int
    {
        return self.drawString(p_281653_, p_283140_, p_283102_, p_282347_, p_281429_)
    }

    fun drawString(
        p_281547_: Font,
        p_282131_: Component,
        p_282857_: Int,
        p_281250_: Int,
        p_282195_: Int,
        p_282791_: Boolean
    ): Int = self.drawString(p_281547_, p_282131_, p_282857_, p_281250_, p_282195_, p_282791_)

    fun drawWordWrap(
        p_281494_: Font,
        p_283463_: FormattedText,
        p_282183_: Int,
        p_283250_: Int,
        p_282564_: Int,
        p_282629_: Int
    ) = self.drawWordWrap(p_281494_, p_283463_, p_282183_, p_283250_, p_282564_, p_282629_)









    fun blit(
        x: Int,
        y: Int,
        z: Int,
        width: Int,
        height: Int,
        sprite: TextureAtlasSprite
    ): Unit = TODO() // this.innerBlit(sprite.atlasLocation(), x, x + width, y, y + height, z, sprite.u0, sprite.u1, sprite.v0, sprite.v1)

    fun blit(
        x: Int,
        y: Int,
        z: Int,
        width: Int,
        height: Int,
        sprite: TextureAtlasSprite,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ): Unit = TODO() // this.innerBlit(sprite.atlasLocation(), x + 0, x + width, y + 0, y + height, z + 0, sprite.u0, sprite.u1, sprite.v0, sprite.v1, red, green, blue, alpha)

    fun blit(
        resource: ResourceLocation?,
        x: Int,
        y: Int,
        rectX: Int,
        rectY: Int,
        width: Int,
        height: Int
    ) = this.blit(resource, x, y, 0, rectX.toFloat(), rectY.toFloat(), width, height,
            256,
            256
        )

    fun blit(
        resource: ResourceLocation?,
        x: Int,
        y: Int,
        z: Int,
        rectX: Float,
        rectY: Float,
        width: Int,
        height: Int,
        p_282832_: Int,
        p_281851_: Int
    ) = this.blit(
            resource, x, x + width, y, y + height, z,
            width,
            height,
            rectX,
            rectY,
            p_282832_ + 0,
            p_281851_ + 0
        )

    fun blit(
        resource: ResourceLocation?,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        rectX: Float,
        rectY: Float,
        rectWidth: Int,
        rectHeight: Int,
        p_282481_: Int,
        p_281887_: Int
    ) = this.blit(resource, x, x + width, y, y + height, 0, rectWidth, rectHeight, rectX, rectY,
            p_282481_ + 0,
            p_281887_ + 0
        )

    fun blit(
        resource: ResourceLocation?,
        x: Int,
        y: Int,
        rectX: Float,
        rectY: Float,
        width: Int,
        height: Int,
        p_282596_: Int,
        p_281699_: Int
    ) = this.blit(resource, x, y, width, height, rectX, rectY, width, height,
            p_282596_ + 0,
            p_281699_ + 0
        )

    private fun blit(
        resource: ResourceLocation?,
        xStart: Int,
        xEnd: Int,
        yStart: Int,
        yEnd: Int,
        z: Int,
        rectWidth: Int,
        rectHeight: Int,
        rectX: Float,
        rectY: Float,
        p_282315_: Int,
        p_281436_: Int
    )
    {
        TODO()/*
        this.innerBlit(
            resource,
            xStart,
            xEnd,
            yStart,
            yEnd,
            z,
            (rectX + 0.0f) / p_282315_.toFloat(),
            (rectX + rectWidth.toFloat()) / p_282315_.toFloat(),
            (rectY + 0.0f) / p_281436_.toFloat(),
            (rectY + rectHeight.toFloat()) / p_281436_.toFloat()
        )*/
    }













    fun renderOutline(p_281496_: Int, p_282076_: Int, p_281334_: Int, p_283576_: Int, p_283618_: Int)
    {
        self.renderOutline(p_281496_, p_282076_, p_281334_, p_283576_, p_283618_)
    }

    fun blitNineSliced(
        p_282546_: ResourceLocation,
        p_282275_: Int,
        p_281581_: Int,
        p_283274_: Int,
        p_281626_: Int,
        p_283005_: Int,
        p_282047_: Int,
        p_282125_: Int,
        p_283423_: Int,
        p_281424_: Int
    )
    {
        self.blitNineSliced(
            p_282546_,
            p_282275_,
            p_281581_,
            p_283274_,
            p_281626_,
            p_283005_,
            p_282047_,
            p_282125_,
            p_283423_,
            p_281424_
        )
    }

    fun blitNineSliced(
        p_282543_: ResourceLocation,
        p_281513_: Int,
        p_281865_: Int,
        p_282482_: Int,
        p_282661_: Int,
        p_282068_: Int,
        p_281294_: Int,
        p_281681_: Int,
        p_281957_: Int,
        p_282300_: Int,
        p_282769_: Int
    )
    {
        self.blitNineSliced(
            p_282543_,
            p_281513_,
            p_281865_,
            p_282482_,
            p_282661_,
            p_282068_,
            p_281294_,
            p_281681_,
            p_281957_,
            p_282300_,
            p_282769_
        )
    }

    fun blitNineSliced(
        p_282712_: ResourceLocation,
        p_283509_: Int,
        p_283259_: Int,
        p_283273_: Int,
        p_282043_: Int,
        p_281430_: Int,
        p_281412_: Int,
        p_282566_: Int,
        p_281971_: Int,
        p_282879_: Int,
        p_281529_: Int,
        p_281924_: Int,
        p_281407_: Int
    )
    {
        self.blitNineSliced(
            p_282712_,
            p_283509_,
            p_283259_,
            p_283273_,
            p_282043_,
            p_281430_,
            p_281412_,
            p_282566_,
            p_281971_,
            p_282879_,
            p_281529_,
            p_281924_,
            p_281407_
        )
    }

    fun blitRepeating(
        p_283059_: ResourceLocation,
        p_283575_: Int,
        p_283192_: Int,
        p_281790_: Int,
        p_283642_: Int,
        p_282691_: Int,
        p_281912_: Int,
        p_281728_: Int,
        p_282324_: Int
    )
    {
        self.blitRepeating(
            p_283059_,
            p_283575_,
            p_283192_,
            p_281790_,
            p_283642_,
            p_282691_,
            p_281912_,
            p_281728_,
            p_282324_
        )
    }

    fun blitRepeating(
        p_283059_: ResourceLocation,
        p_283575_: Int,
        p_283192_: Int,
        p_281790_: Int,
        p_283642_: Int,
        p_282691_: Int,
        p_281912_: Int,
        p_281728_: Int,
        p_282324_: Int,
        textureWidth: Int,
        textureHeight: Int
    )
    {
        self.blitRepeating(
            p_283059_,
            p_283575_,
            p_283192_,
            p_281790_,
            p_283642_,
            p_282691_,
            p_281912_,
            p_281728_,
            p_282324_,
            textureWidth,
            textureHeight
        )
    }

    fun renderItem(p_281978_: ItemStack, p_282647_: Int, p_281944_: Int)
    {
        self.renderItem(p_281978_, p_282647_, p_281944_)
    }

    fun renderItem(p_282262_: ItemStack, p_283221_: Int, p_283496_: Int, p_283435_: Int)
    {
        self.renderItem(p_282262_, p_283221_, p_283496_, p_283435_)
    }

    fun renderItem(p_282786_: ItemStack, p_282502_: Int, p_282976_: Int, p_281592_: Int, p_282314_: Int)
    {
        self.renderItem(p_282786_, p_282502_, p_282976_, p_281592_, p_282314_)
    }

    fun renderItem(
        p_282154_: LivingEntity,
        p_282777_: ItemStack,
        p_282110_: Int,
        p_281371_: Int,
        p_283572_: Int
    )
    {
        self.renderItem(p_282154_, p_282777_, p_282110_, p_281371_, p_283572_)
    }

    fun renderFakeItem(p_281946_: ItemStack, p_283299_: Int, p_283674_: Int)
    {
        self.renderFakeItem(p_281946_, p_283299_, p_283674_)
    }

    fun renderItemDecorations(p_281721_: Font, p_281514_: ItemStack, p_282056_: Int, p_282683_: Int)
    {
        self.renderItemDecorations(p_281721_, p_281514_, p_282056_, p_282683_)
    }

    fun renderItemDecorations(
        p_282005_: Font,
        p_283349_: ItemStack,
        p_282641_: Int,
        p_282146_: Int,
        p_282803_: String?
    )
    {
        self.renderItemDecorations(p_282005_, p_283349_, p_282641_, p_282146_, p_282803_)
    }

    fun renderTooltip(p_282308_: Font, p_282781_: ItemStack, p_282687_: Int, p_282292_: Int)
    {
        self.renderTooltip(p_282308_, p_282781_, p_282687_, p_282292_)
    }

    fun renderTooltip(
        font: Font,
        textComponents: MutableList<Component>,
        tooltipComponent: Optional<TooltipComponent>,
        stack: ItemStack,
        mouseX: Int,
        mouseY: Int
    )
    {
        self.renderTooltip(font, textComponents, tooltipComponent, stack, mouseX, mouseY)
    }

    fun renderTooltip(
        p_283128_: Font,
        p_282716_: MutableList<Component>,
        p_281682_: Optional<TooltipComponent>,
        p_283678_: Int,
        p_281696_: Int
    )
    {
        self.renderTooltip(p_283128_, p_282716_, p_281682_, p_283678_, p_281696_)
    }

    fun renderTooltip(p_282269_: Font, p_282572_: Component, p_282044_: Int, p_282545_: Int)
    {
        self.renderTooltip(p_282269_, p_282572_, p_282044_, p_282545_)
    }

    fun renderTooltip(
        p_282192_: Font,
        p_282297_: MutableList<out FormattedCharSequence>,
        p_281680_: Int,
        p_283325_: Int
    )
    {
        self.renderTooltip(p_282192_, p_282297_, p_281680_, p_283325_)
    }

    fun renderTooltip(
        p_281627_: Font,
        p_283313_: MutableList<FormattedCharSequence>,
        p_283571_: ClientTooltipPositioner,
        p_282367_: Int,
        p_282806_: Int
    )
    {
        self.renderTooltip(p_281627_, p_283313_, p_283571_, p_282367_, p_282806_)
    }

    fun renderComponentTooltip(
        p_282739_: Font,
        p_281832_: MutableList<Component>,
        p_282191_: Int,
        p_282446_: Int
    )
    {
        self.renderComponentTooltip(p_282739_, p_281832_, p_282191_, p_282446_)
    }

    fun renderComponentTooltip(
        font: Font,
        tooltips: MutableList<out FormattedText>,
        mouseX: Int,
        mouseY: Int,
        stack: ItemStack
    )
    {
        self.renderComponentTooltip(font, tooltips, mouseX, mouseY, stack)
    }

    fun renderComponentHoverEffect(p_282584_: Font, p_282156_: Style?, p_283623_: Int, p_282114_: Int)
    {
        self.renderComponentHoverEffect(p_282584_, p_282156_, p_283623_, p_282114_)
    }

}