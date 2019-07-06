package piedel.piotr.thesis.util.imageprocessor

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript

class BrightnessProcessor(renderScriptInstance: RenderScript) : SuperProcessor(renderScriptInstance) {
    override fun process(inputBitmap: Bitmap): Bitmap {
        val outputBitmap = Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
        val inputAllocation = Allocation.createFromBitmap(renderScriptInstance, inputBitmap)
        val outputAllocation = Allocation.createFromBitmap(renderScriptInstance, outputBitmap)
        val brightnessScript = ScriptC_brightnessauto(renderScriptInstance)

        //for each root fun which is working for every bit
        // TODO: check how exactly this work
        brightnessScript.forEach_root(inputAllocation, outputAllocation)
        outputAllocation.copyTo(outputBitmap)

        return outputBitmap
    }
}