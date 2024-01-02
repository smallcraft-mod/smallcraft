package io.github.svew.smallcraft.language

import io.github.svew.smallcraft.Smallcraft
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.LanguageProvider

class EnglishProvider(output: PackOutput) : LanguageProvider(output, Smallcraft.MOD_ID, "en_us")
{
    override fun addTranslations()
    {
        this.addItem({ Smallcraft.ROPE_ITEM }, "Rope")
    }
}