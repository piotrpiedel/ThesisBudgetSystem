package piedel.piotr.thesis.ui.fragment.category.categorylist

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.data.model.category.categoryparent.CategoryParent

class CategoryExpandableGroup(parentCategoryChild: CategoryParent, items: List<CategoryChild>) : ExpandableGroup<CategoryChild>(parentCategoryChild.category_title_parent, items)