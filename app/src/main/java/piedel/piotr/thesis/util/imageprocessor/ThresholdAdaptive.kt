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
        thresholdAdaptiveScript._kernelSize = 15;
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
//imijContext.grayscale(bmp, bmpOut);
//imijContext.adaptiveThreshold(bmpOut, bmpOut, Integer.parseInt(prefs.getString(getString(R.string.pref_adaptiveThresholdRadius), "15")), 255);
//break;

//public void adaptiveThreshold(Bitmap bmp, Bitmap bmpOut, int blockSize, int maxValue) {
//    if (bmp.getConfig() != Bitmap.Config.ARGB_8888 || bmpOut.getConfig() != Bitmap.Config.ARGB_8888) {
//        throw new IllegalArgumentException("Input and output bitmaps must be 1 channel, 8-bit configuration");
//    }
//
//    alloc(bmp, bmpOut);
//
//    imijScript.set_kernelSize(blockSize);
//    imijScript.set_imgIn(mInAllocation);
//    imijScript.set_width(bmp.getWidth());
//
//    imijScript.set_height(bmp.getHeight());
//    imijScript.set_thresholdMaxValue(maxValue);
//    imijScript.forEach_adaptiveThreshold(mInAllocation, mOutAllocation);
//
//    copy(bmpOut);
//}

