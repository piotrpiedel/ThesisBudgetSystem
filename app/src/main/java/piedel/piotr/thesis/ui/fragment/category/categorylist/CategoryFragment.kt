package piedel.piotr.thesis.ui.fragment.category.categorylist

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import butterknife.BindView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.ui.base.BaseFragment
import javax.inject.Inject

class CategoryFragment : BaseFragment(), CategoryView {

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var categoryPresenter: CategoryPresenter

    @BindView(R.id.categories_list_view)
    lateinit var categoriesListView: ExpandableListView

    override val layout: Int
        get() = R.layout.fragment_categories_list

    override val toolbarTitle: String
        get() = FRAGMENT_TITLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        categoryPresenter.attachView(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        categoryPresenter.loadParentCategories()
        //TODO: setOnChildClickListener??? ale co dokładnie ma sie dziać?xD
    }

    private fun setAdapter() {
        categoriesListView.setAdapter(categoryAdapter)
    }

    override fun updateList(listCategories: List<Category>, listChildHashMap: MutableMap<Int, MutableList<Category>>) {
        categoryAdapter.updateList(listCategories, listChildHashMap);
    }

    companion object {
        const val FRAGMENT_TAG = "operationsFragment"
        const val FRAGMENT_TITLE = " Categories "
    }


}