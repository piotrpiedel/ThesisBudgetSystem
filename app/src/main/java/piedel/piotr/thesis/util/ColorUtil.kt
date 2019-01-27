package piedel.piotr.thesis.util

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import piedel.piotr.thesis.R
import java.util.*

fun getRandomColor(): Int {
    val rnd = Random()
    return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
}

fun getColorsFromTemplate(): ArrayList<Int> {
    val colors = arrayListOf<Int>()

    for (c in ColorTemplate.VORDIPLOM_COLORS)
        colors.add(c)

    for (c in ColorTemplate.JOYFUL_COLORS)
        colors.add(c)

    for (c in ColorTemplate.COLORFUL_COLORS)
        colors.add(c)

    for (c in ColorTemplate.LIBERTY_COLORS)
        colors.add(c)

    for (c in ColorTemplate.PASTEL_COLORS)
        colors.add(c)

    colors.add(ColorTemplate.getHoloBlue())
    return colors
}

fun setColorDataSetBarChart(set: BarDataSet, context: Context) {
    set.setColors(intArrayOf(R.color.YellowGreen, R.color.Green, R.color.Blue, R.color.BlueViolet, R.color.AliceBlue, R.color.DarkOrchid, R.color.DarkOrange, R.color.Khaki), context)
}