package piedel.piotr.thesis.util

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import piedel.piotr.thesis.R


@GlideModule
class MyAppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val requestOptions = RequestOptions().placeholder(getCircularProgressDrawable(context)).error(R.drawable.ic_outline_error_outline)
        builder.setDefaultRequestOptions(requestOptions)
    }

}

@GlideExtension
 class MyAppExtension private constructor() {
    companion object {
        @GlideOption
        fun miniThumb(options: RequestOptions) {
            options.fitCenter()
                    .override(200, 200)
        }
    }

}

