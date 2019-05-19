package piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog

//refactored - currently out of use in app
@Deprecated("Refactored - currently out of use in app")
enum class ImageSourceOptions {
    CAMERA, INTERNAL_MEMORY, NOT_RECOGNIZED_SOURCE
}


//refactored - currently out of use in app
@Deprecated("Refactored - currently out of use in app")
fun imageSourceOptionsConverter(passedString: String?): ImageSourceOptions {
    if (passedString == "CAMERA") return ImageSourceOptions.CAMERA
    else if (passedString == "INTERNAL_MEMORY") return ImageSourceOptions.INTERNAL_MEMORY
    return ImageSourceOptions.NOT_RECOGNIZED_SOURCE
}
