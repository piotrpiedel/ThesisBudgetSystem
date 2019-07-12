package piedel.piotr.thesis.ui.activity.imagepicker

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.karumi.dexter.Dexter
import com.yalantis.ucrop.UCrop
import piedel.piotr.thesis.data.buildier.ImagePickerActivityOptions
import piedel.piotr.thesis.injection.scopes.ConfigPersistent
import piedel.piotr.thesis.ui.activity.imagepicker.ImagePickerContract.ImagePickerView
import piedel.piotr.thesis.ui.activity.imagepicker.ImagePickerContract.PresenterContract
import piedel.piotr.thesis.ui.base.BasePresenter
import piedel.piotr.thesis.util.listener.CameraAndStoragePermissionListener
import piedel.piotr.thesis.util.saveBitmapReturnOnlyPathToFile
import piedel.piotr.thesis.util.suffixAppendToFileNameBeforeExtension
import piedel.piotr.thesis.util.toGrayScaleUsingRenderScript
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

@ConfigPersistent
class ImagePickerPresenter @Inject constructor() : BasePresenter<ImagePickerView>(), PresenterContract<ImagePickerView> {

    override fun getOriginalToGrayScaleToModifiedBitmap(sourcePathToFile: String, context: Context): Uri {
        val originalPickedImageFile = File(sourcePathToFile)
        val modifiedFileName = appendSuffixCropToImage(originalPickedImageFile.name)
        val bitmapFromFileInGrayScale: Bitmap = decodeOriginalBitmapToScaledAndModifyToGrayScale(originalPickedImageFile, context)
        return Uri.fromFile(File(saveBitmapGrayScaleAsNewFile(modifiedFileName, bitmapFromFileInGrayScale)))
    }

    //TODO: refactor this split in two methods or find out other solution for scaling down bitmap
    private fun decodeOriginalBitmapToScaledAndModifyToGrayScale(originalPickedImageFile: File, context: Context): Bitmap {
        return bitmapDecodeAndGrayScaleOrOriginal(originalPickedImageFile, context)
    }

    private fun saveBitmapGrayScaleAsNewFile(modifiedFileName: String, bitmapFromFileInGrayScale: Bitmap): String {
        return saveBitmapReturnOnlyPathToFile(modifiedFileName, bitmapFromFileInGrayScale)
    }

    override fun openPickerBasedOnGivenPermissions(imagePickerActivity: ImagePickerActivity) {
        Dexter.withActivity(imagePickerActivity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(CameraAndStoragePermissionListener(view))
                .check()
    }

    override fun bitmapDecodeAndGrayScaleOrOriginal(originalPickedImageFile: File, context: Context): Bitmap {
        val bitmapOptions = BitmapFactory.Options().apply {
            inSampleSize = 2
        }
        val bitmapOriginal = BitmapFactory.decodeStream(FileInputStream(originalPickedImageFile))
        if (isBitmapResolutionLargerThanFHD(bitmapOriginal)) {
            bitmapOriginal.recycle() // release original bitmap immediately
            return BitmapFactory
                    .decodeStream(FileInputStream(originalPickedImageFile), null, bitmapOptions) as Bitmap
//                    ?.toGrayScaleUsingRenderScript(context)
//                    ?.autoBrightnessUsingRenderScript(context)
//                    ?.thresholdAdaptiveUsingRenderScript(context) as Bitmap
//                    ?.binarizeBitmapUsingRenderScriptTempFunctionName(context) as Bitmap
            //should split in two methods
            // and should in one time grayscale&& brightness in one renderscript
            // this approach should be faster than separate functions
        } else return bitmapOriginal.toGrayScaleUsingRenderScript(context)
    }

    private fun isBitmapResolutionLargerThanFHD(bitmapOriginal: Bitmap) =
            bitmapOriginal.height > 1920 && bitmapOriginal.width > 1080 || bitmapOriginal.width > 1920 && bitmapOriginal.height > 1080

    private fun appendSuffixCropToImage(originalNameFromFile: String): String {
        return originalNameFromFile.suffixAppendToFileNameBeforeExtension("_crop")
    }

    override fun setOptionsFromPassedIntentOrDefault(imagePickerOptions: ImagePickerActivityOptions): UCrop.Options {
        val options = UCrop.Options().apply {
            setCompressionQuality(imagePickerOptions.imageCompression)
        }
        if (imagePickerOptions.lockAspectRatio)
            options.withAspectRatio(imagePickerOptions.aspectRatio_X, imagePickerOptions.aspectRatio_Y)

        if (imagePickerOptions.setBitmapMaxWidthHeight)
            options.withMaxResultSize(imagePickerOptions.bitmapMaxWidth, imagePickerOptions.bitmapMaxHeight)
        return options
    }
}