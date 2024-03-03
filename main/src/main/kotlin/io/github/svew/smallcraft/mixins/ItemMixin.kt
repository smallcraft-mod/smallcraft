package io.github.svew.smallcraft.mixins

import net.minecraft.world.item.Item
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import kotlin.math.min


@Mixin(Item::class)
abstract class ItemMixin
{
    @Inject(method = [ "getMaxStackSize" ], at = [ At("RETURN") ], cancellable = true)
    private fun injected(cir: CallbackInfoReturnable<Int>)
    {
        cir.returnValue = min(cir.returnValue, 16)
    }
}

