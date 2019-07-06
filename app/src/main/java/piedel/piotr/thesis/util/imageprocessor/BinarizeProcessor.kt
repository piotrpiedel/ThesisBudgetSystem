package piedel.piotr.thesis.util.imageprocessor

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript


class BinarizeProcessor(renderscriptInstance: RenderScript) : SuperProcessor(renderscriptInstance) {
    override fun process(inputBitmap: Bitmap): Bitmap {
        val outputBitmap = Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
        val inputAllocation = Allocation.createFromBitmap(renderScriptInstance, inputBitmap)
        val outputAllocation = Allocation.createFromBitmap(renderScriptInstance, outputBitmap)
        val mBinarizeScript = ScriptC_binarize(renderScriptInstance)

        mBinarizeScript.forEach_root(inputAllocation, outputAllocation)
        outputAllocation.copyTo(outputBitmap)

        return outputBitmap
    }
}