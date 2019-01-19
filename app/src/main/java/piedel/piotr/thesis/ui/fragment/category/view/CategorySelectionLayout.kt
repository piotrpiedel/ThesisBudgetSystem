package piedel.piotr.thesis.ui.fragment.category.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.github.pavlospt.roundedletterview.RoundedLetterView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.util.getRandomColor

class CategorySelectionLayout : RelativeLayout {

    @BindView(R.id.round_letter_view_category_selection)
    lateinit var roundedLetterView: RoundedLetterView

    @BindView(R.id.category_title_add_view_fragment)
    lateinit var categoryTitle: TextView


    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, R.style.OperationInputStyle) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, R.style.OperationInputStyle, defStyleRes) {
        initView()
    }

    private fun initView() {
        inflate(context, R.layout.operation_input_category_selection_view, this)
        ButterKnife.bind(this)
    }

    fun setView(categoryChild: CategoryChild) {
        categoryTitle.visibility = View.VISIBLE
        roundedLetterView.visibility = View.VISIBLE
        categoryTitle.setTypeface(null, Typeface.BOLD)
        categoryTitle.text = categoryChild.category_title
        roundedLetterView.titleText = categoryChild.category_title[0].toString()
        roundedLetterView.backgroundColor = getRandomColor()
    }
}