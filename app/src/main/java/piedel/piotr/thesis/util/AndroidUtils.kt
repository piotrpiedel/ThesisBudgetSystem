package piedel.piotr.thesis.util

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.CircularProgressDrawable
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

fun ImageView.loadImageFromUrl(url: String) {
    Glide.with(context)
            .load(url)
            .into(this)
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun getCircularProgressDrawable(context: Context): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    return circularProgressDrawable
}

fun showToast(context: Context, textToToast: String) {
    Toast.makeText(context, textToToast, Toast.LENGTH_SHORT).show()
}