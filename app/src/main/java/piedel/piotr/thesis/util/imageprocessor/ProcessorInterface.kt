package piedel.piotr.thesis.util.imageprocessor

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.RSRuntimeException
import android.renderscript.RenderScript
import piedel.piotr.thesis.exception.ProcessorException
import piedel.piotr.thesis.util.imageprocessor.ProcessorInterface.ProcessorType.*
import timber.log.Timber

//  use singleton pattern to ensure there is only one processorInterface and renderScriptInstance
class ProcessorInterface private constructor(context: Context) {
    private val renderScriptInstance: RenderScript? = RenderScript.create(context)

    companion object {
        private var processorInterface: ProcessorInterface? = null
        fun getProcessorInterfaceInstance(context: Context): ProcessorInterface {
            if (processorInterface == null) processorInterface = ProcessorInterface(context)
            return processorInterface as ProcessorInterface
        }
    }

    @Throws(ProcessorException::class)
    fun processBitmapWithImageProcessor(inputBitmap: Bitmap, processorType: ProcessorType): Bitmap {
        if (renderScriptInstance == null) throw ProcessorException(ProcessorException.NOT_INITIALIZED)
        val outputBitmap: Bitmap?
        val imageProcessor: SuperProcessor
        when (processorType) {
            BINARIZE -> imageProcessor = BinarizeProcessor(renderScriptInstance)
            GRAYSCALE -> imageProcessor = GrayScaleProcessor(renderScriptInstance)
            BRIGHTNESS -> imageProcessor = BrightnessProcessor(renderScriptInstance)
            THRESHOLD_ADAPTIVE -> imageProcessor = ThresholdAdaptive(renderScriptInstance)
            else -> throw ProcessorException(ProcessorException.UNKNOWN_PROCESSOR_TYPE)
        }

        try {
            outputBitmap = imageProcessor.process(inputBitmap)
        } catch (e: RSRuntimeException) {
            Timber.e(e)
            e.printStackTrace()
            throw ProcessorException(ProcessorException.RUNTIME_EXCEPTION)
        }
        return outputBitmap
    }

    enum class ProcessorType {
        BINARIZE, GRAYSCALE, BRIGHTNESS, THRESHOLD_ADAPTIVE
        //, SOBEL
    }
}