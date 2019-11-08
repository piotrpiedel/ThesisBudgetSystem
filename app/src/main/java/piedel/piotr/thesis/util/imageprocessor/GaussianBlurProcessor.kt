package piedel.piotr.thesis.util.imageprocessor

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

class GaussianBlurProcessor(renderScriptInstance: RenderScript) : SuperProcessor(renderScriptInstance) {
    override fun process(inputBitmap: Bitmap): Bitmap {
        val outputBitmap = Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
        val inputAllocation = Allocation.createFromBitmap(renderScriptInstance, inputBitmap)
        val outputAllocation = Allocation.createFromBitmap(renderScriptInstance, outputBitmap)
        val gaussianBlurScript = ScriptIntrinsicBlur
                .create(renderScriptInstance, Element.U8_4(renderScriptInstance))
        gaussianBlurScript.setRadius(5.0f);
        gaussianBlurScript.setInput(inputAllocation);
        gaussianBlurScript.forEach(outputAllocation);
        outputAllocation.copyTo(outputBitmap);
        return outputBitmap
    }
}