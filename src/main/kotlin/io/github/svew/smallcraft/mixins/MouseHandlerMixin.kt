package io.github.svew.smallcraft.mixins

import io.github.svew.smallcraft.Smallcraft
import net.minecraft.client.MouseHandler
import org.spongepowered.asm.mixin.*
import org.spongepowered.asm.mixin.injection.*
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.invoke.arg.Args

@Mixin(MouseHandler::class)
abstract class MouseHandlerMixin {

    private var lastX: Double? = null
    private var lastY: Double? = null

    @ModifyArg(method=["turnPlayer"], at=At("INVOKE", target="turn(DD)V"), index = 0)
    private fun onMoveX(x: Double, y: Double): Double {
        //Smallcraft.LOGGER.debug("(%.3f, %.3f)".format(x, y))
        return x
    }

    @ModifyArg(method=["turnPlayer"], at=At("INVOKE", target="turn(DD)V"), index = 1)
    private fun onMoveY(y: Double): Double {
        return y
    }

    @Inject(method=["onMove"], at=[At("HEAD")])
    private fun onMovePre(window: Long, x: Double, y: Double, ci: CallbackInfo) {
        //Smallcraft.LOGGER.debug("oh boy! $x, $y")
    }

    /*@ModifyArgs(method=["onMove"], at=At("INVOKE"))
    private fun onMove(args: Args) {
        val thisX: Double = args[1];
        val thisY: Double = args[2];
        args[1] = thisX - lastX
        args[2] = thisY - lastY
        lastX = thisX
        lastY = thisY
        Smallcraft.LOGGER.debug("oh boy! {}, {}", lastX, lastY)
    }*/

    /*@ModifyVariable(method=["onMove"], at=At("HEAD"), ordinal = 1)
    private fun onMoveX(x: Double): Double {
        val outX = x - lastX
        lastX = x
        Smallcraft.LOGGER.debug("i modify x! {}", outX)
        return outX
    }

    @ModifyVariable(method=["onMove"], at=At("HEAD"), ordinal = 1)
    private fun onMoveY(y: Double): Double {
        val outY = y - lastY
        lastY = y
        Smallcraft.LOGGER.debug("i modify y! {}", outY)
        return outY
    }*/
}