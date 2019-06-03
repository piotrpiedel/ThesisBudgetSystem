package piedel.piotr.thesis.util.glide

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import piedel.piotr.thesis.R
import piedel.piotr.thesis.util.getCircularProgressDrawable

fun glideLoadAsSmallBitmap(context: Context, picturePath: String?): RequestBuilder<Bitmap> {
    return returnBuilderAsBitmap(requestOptions(context)
            .override(200, 200),
            context, picturePath)
}

fun glideLoadAsBitmap(context: Context, picturePath: String?): RequestBuilder<Bitmap> {
    return returnBuilderAsBitmap(requestOptions(context), context, picturePath)
}

private fun requestOptions(context: Context): RequestOptions {
    return RequestOptions()
            .placeholder(getCircularProgressDrawable(context))
            .error(R.drawable.ic_outline_error_outline)
}

private fun returnBuilderAsBitmap(requestOptions: RequestOptions, context: Context, picturePath: String?): RequestBuilder<Bitmap> {
    return Glide.with(context)
            .asBitmap()
            .load(picturePath)
            .apply(requestOptions)
}
