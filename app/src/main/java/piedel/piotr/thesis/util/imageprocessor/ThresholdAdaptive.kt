package piedel.piotr.thesis.util.imageprocessor

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript

class ThresholdAdaptive(renderScriptInstance: RenderScript) : SuperProcessor(renderScriptInstance) {
    override fun process(inputBitmap: Bitmap): Bitmap {
        val outputBitmap = Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
        val inputAllocation = Allocation.createFromBitmap(renderScriptInstance, inputBitmap)
        val outputAllocation = Allocation.createFromBitmap(renderScriptInstance, outputBitmap)
        val thresholdAdaptiveScript = ScriptC_thresholdadaptive(renderScriptInstance)
        thresholdAdaptiveScript._kernelSize = 30;
        thresholdAdaptiveScript._imgIn = inputAllocation;
        thresholdAdaptiveScript._width = inputBitmap.width.toLong();
        thresholdAdaptiveScript._height = inputBitmap.height.toLong();
        thresholdAdaptiveScript._thresholdMaxValue = 255.0f;

        //for each root fun which is working for every bit
        thresholdAdaptiveScript.forEach_root(inputAllocation, outputAllocation)
        outputAllocation.copyTo(outputBitmap)

        return outputBitmap
    }
}

