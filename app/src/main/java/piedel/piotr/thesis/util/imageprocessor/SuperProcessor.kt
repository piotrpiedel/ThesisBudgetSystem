package piedel.piotr.thesis.util.imageprocessor

import android.graphics.Bitmap
import android.renderscript.RSRuntimeException
import android.renderscript.RenderScript


abstract class SuperProcessor(renderscript: RenderScript) {
    protected var renderScriptInstance: RenderScript = renderscript

    @Throws(RSRuntimeException::class)
    abstract fun process(inputBitmap: Bitmap): Bitmap
}