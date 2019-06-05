package piedel.piotr.thesis.ui.fragment.category.categorylist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.util.hideKeyboard
import javax.inject.Inject

class CategoryFragment : BaseFragment(), CategoryContract.CategoryView {

    private lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var categoryPresenter: CategoryPresenter

    @BindView(R.id.categories_list_view)
    lateinit var categoriesListView: RecyclerView

    override val layout: Int
        get() = R.layout.fragment_categories_list

    override val toolbarTitle: String
        get() = context?.getString(R.string.categories).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        categoryPresenter.attachView(this)
        getMainActivity().hideKeyboard()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutManager()
        initFragment()
    }

    private fun initFragment() {
        categoryPresenter.loadCategories()
    }

    private fun setLayoutManager() {
        categoriesListView.layoutManager = LinearLayoutManager(context)
    }

    override fun updateList(listCategories: MutableList<CategoryExpandableGroup>) {
        categoryAdapter = CategoryAdapter(listCategories)
        categoriesListView.adapter = categoryAdapter
    }

    companion object {
        const val FRAGMENT_TAG = "operationsFragment"
    }


}