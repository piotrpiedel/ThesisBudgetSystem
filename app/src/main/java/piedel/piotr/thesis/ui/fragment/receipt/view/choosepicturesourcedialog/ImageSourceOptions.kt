package piedel.piotr.thesis.ui.fragment.receipt.view.choosepicturesourcedialog

enum class ImageSourceOptions {
    CAMERA, INTERNAL_MEMORY, NOT_RECOGNIZED_SOURCE // add external memory ??
}

fun imageSourceOptionsConverter(passedString: String?): ImageSourceOptions {
    if (passedString == "CAMERA") return ImageSourceOptions.CAMERA
    else if (passedString == "INTERNAL_MEMORY") return ImageSourceOptions.INTERNAL_MEMORY
    return ImageSourceOptions.NOT_RECOGNIZED_SOURCE
}
