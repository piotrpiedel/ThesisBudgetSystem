package piedel.piotr.thesis.util.imageprocessor

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript


class GrayScaleProcessor(renderScriptInstance: RenderScript) : SuperProcessor(renderScriptInstance) {
    override fun process(inputBitmap: Bitmap): Bitmap {
        val outputBitmap = Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
        val inputAllocation = Allocation.createFromBitmap(renderScriptInstance, inputBitmap)
        val outputAllocation = Allocation.createFromBitmap(renderScriptInstance, outputBitmap)
        val mGrayScaleScript = ScriptC_grayscale(renderScriptInstance)

        //for each root fun which is working for every bit
        // TODO: check how exactly this work
        mGrayScaleScript.forEach_root(inputAllocation, outputAllocation)
        outputAllocation.copyTo(outputBitmap)

        return outputBitmap
    }
}