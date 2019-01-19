package piedel.piotr.thesis.ui.fragment.chart.choosechart

import android.os.Bundle
import butterknife.OnClick
import piedel.piotr.thesis.R
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.chart.barchart.BarChartFragment
import piedel.piotr.thesis.ui.fragment.chart.piechart.PieCharFragment
import javax.inject.Inject

class ChooseChartFragment : BaseFragment(), ChooseChartView {

    @Inject
    lateinit var chooseChartPresenter: ChooseChartPresenter

    override val layout: Int
        get() = R.layout.fragment_choose_chart

    override val toolbarTitle: String
        get() = context?.getString(R.string.choose_chart).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        chooseChartPresenter.attachView(this)
    }

    @OnClick(R.id.button_bar_chart)
    fun onBarChartButtonClicked() {
        getMainActivity().replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
                BarChartFragment(),
                BarChartFragment.FRAGMENT_TAG)
    }

    @OnClick(R.id.button_pie_chart)
    fun onPieChartButtonClicked() {
        getMainActivity().replaceFragmentWithBackStack(R.id.fragment_container_activity_main,
                PieCharFragment(),
                PieCharFragment.FRAGMENT_TAG)
    }

    companion object {
        const val FRAGMENT_TAG: String = "ChooseChart"
    }

}