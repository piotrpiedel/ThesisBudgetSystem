package piedel.piotr.thesis.ui.fragment.category.categoryselectlablelist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.ui.base.BaseFragment
import piedel.piotr.thesis.ui.fragment.category.categorylist.CategoryExpandableGroup
import piedel.piotr.thesis.util.hideKeyboard
import javax.inject.Inject


class CategorySelectableListFragment : BaseFragment(), CategorySelectableListContract.CategorySelectListView, CategorySelectableListAdapter.CategorySelectListAdapterOnClickHandler {


    private lateinit var categorySelectableListAdapter: CategorySelectableListAdapter

    @Inject
    lateinit var categorySelectableListPresenter: CategorySelectableListPresenter

    @BindView(R.id.categories_list_view)
    lateinit var categoriesListView: RecyclerView

    override val layout: Int
        get() = R.layout.fragment_categories_list

    override val toolbarTitle: String
        get() = context?.getString(R.string.categories).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        categorySelectableListPresenter.attachView(this)
        getMainActivity().hideKeyboard()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutManager()
        initFragment()

    }

    private fun initFragment() {
        categorySelectableListPresenter.loadCategories()
    }

    override fun onChildClick(childCategoryChild: CategoryChild) {
        targetFragment?.onActivityResult(
                targetRequestCode,
                Activity.RESULT_OK,
                Intent().putExtra(FRAGMENT_INTENT_CATEGORY, childCategoryChild)
        )
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    private fun setLayoutManager() {
        categoriesListView.layoutManager = LinearLayoutManager(context)
    }

    override fun updateList(listCategories: MutableList<CategoryExpandableGroup>) {
        categorySelectableListAdapter = CategorySelectableListAdapter(listCategories)
        categorySelectableListAdapter.setOnChildClickListener(this)
        categoriesListView.adapter = categorySelectableListAdapter
    }

    companion object {
        const val FRAGMENT_TAG = "categorySelectListFragment"
        const val FRAGMENT_INTENT_CATEGORY = "categoryValue"
    }
}