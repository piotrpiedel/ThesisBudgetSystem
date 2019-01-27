package piedel.piotr.thesis.injection.module.applicationmodule

import android.app.Application
import android.content.Context
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.data.model.category.categoryparent.CategoryParent

class CategoryList(application: Application) {
    /////////////////////
    val context: Context = application.applicationContext
    private val uncategorized = CategoryParent(1, context.getString(R.string.uncategorized))
    private val food = CategoryParent(2, context.getString(R.string.food))
    private val entertainments = CategoryParent(3, context.getString(R.string.entertainments))
    private val house = CategoryParent(4, context.getString(R.string.house))
    private val clothes = CategoryParent(5, context.getString(R.string.clothes))
    private val electronics = CategoryParent(6, context.getString(R.string.electronics))
    private val work = CategoryParent(7, context.getString(R.string.work))

    private val arrayOfCategories = arrayOf(uncategorized, food, entertainments, house, clothes, electronics, work)

    fun getListOfParentCategories(): Array<CategoryParent> {
        return arrayOfCategories
    }

    /////////////////////
    private val foodMain = CategoryChild(7, context.getString(R.string.food_main), food.category_id_parent)
    private val foodShop = CategoryChild(8, context.getString(R.string.food_supermarket), food.category_id_parent)
    private val foodWork = CategoryChild(9, context.getString(R.string.food_work), food.category_id_parent)
    private val foodFast = CategoryChild(10, context.getString(R.string.food_fast_foods), food.category_id_parent)
    private val foodCommon = CategoryChild(11, context.getString(R.string.food_common), food.category_id_parent)

    private val arrayOfFoodSubcategories = arrayOf(foodMain, foodShop, foodWork, foodWork, foodFast, foodCommon)

    fun getListOfFoodSubcategories(): Array<CategoryChild> {
        return arrayOfFoodSubcategories
    }

    /////////////////////
    private val entertainmentsMain = CategoryChild(12, context.getString(R.string.entertainments_main), entertainments.category_id_parent)
    private val entertainmentsShop = CategoryChild(13, context.getString(R.string.entertainments_shop), entertainments.category_id_parent)
    private val entertainmentsWork = CategoryChild(14, context.getString(R.string.entertainments_work), entertainments.category_id_parent)
    private val entertainmentsFast = CategoryChild(15, context.getString(R.string.entertainments_fast), entertainments.category_id_parent)
    private val entertainmentsCommon = CategoryChild(16, context.getString(R.string.entertainments_common), entertainments.category_id_parent)

    private val arrayOfEntertainmentsSubcategories = arrayOf(entertainmentsMain, entertainmentsShop, entertainmentsWork, entertainmentsFast, entertainmentsCommon)

    fun getListOfEntertainmentSubcategories(): Array<CategoryChild> {
        return arrayOfEntertainmentsSubcategories
    }

    /////////////////////
    private val houseMain = CategoryChild(17, context.getString(R.string.house_main), house.category_id_parent)
    private val houseShop = CategoryChild(18, context.getString(R.string.house_maintain), house.category_id_parent)
    private val houseFurniture = CategoryChild(19, context.getString(R.string.house_furniture), house.category_id_parent)
    private val houseFixes = CategoryChild(20, context.getString(R.string.house_fixes), house.category_id_parent)
    private val houseCommon = CategoryChild(21, context.getString(R.string.house_common), house.category_id_parent)
    private val houseBills = CategoryChild(22, context.getString(R.string.house_bills), house.category_id_parent)

    private val arrayOfHouseSubcategories = arrayOf(houseMain, houseShop, houseFurniture, houseFixes, houseCommon, houseBills)

    fun getListOfHouseSubcategories(): Array<CategoryChild> {
        return arrayOfHouseSubcategories
    }

    /////////////////////
    private val clothesMain = CategoryChild(23, context.getString(R.string.clothes_main), clothes.category_id_parent)
    private val clothesShop = CategoryChild(24, context.getString(R.string.clothes_shop), clothes.category_id_parent)
    private val clothesKids = CategoryChild(25, context.getString(R.string.clothes_kids), clothes.category_id_parent)
    private val clothesBoots = CategoryChild(26, context.getString(R.string.clothes_boots), clothes.category_id_parent)
    private val clothesHats = CategoryChild(27, context.getString(R.string.clothes_hats), clothes.category_id_parent)
    private val clothesCasual = CategoryChild(28, context.getString(R.string.clothes_casul), clothes.category_id_parent)

    private val arrayOfClothesSubcategories = arrayOf(clothesMain, clothesShop, clothesKids, clothesBoots, clothesHats, clothesCasual)

    fun getListOfClothesSubcategories(): Array<CategoryChild> {
        return arrayOfClothesSubcategories
    }

    /////////////////////
    private val electronicsMain = CategoryChild(29, context.getString(R.string.electronic_main), electronics.category_id_parent)
    private val electronicsShop = CategoryChild(30, context.getString(R.string.electronic_shop), electronics.category_id_parent)
    private val electronicsKids = CategoryChild(31, context.getString(R.string.electronic_kids), electronics.category_id_parent)
    private val electronicsChina = CategoryChild(32, context.getString(R.string.electronic_china), electronics.category_id_parent)
    private val electronicsFun = CategoryChild(33, context.getString(R.string.electronic_fun), electronics.category_id_parent)
    private val electronicsSmall = CategoryChild(34, context.getString(R.string.electronic_small), electronics.category_id_parent)

    private val arrayOfElectronicsSubcategories = arrayOf(electronicsMain, electronicsShop, electronicsKids, electronicsChina, electronicsFun, electronicsSmall)

    fun getListOfElectronicsSubcategories(): Array<CategoryChild> {
        return arrayOfElectronicsSubcategories
    }

    /////////////////////
    private val workMain = CategoryChild(35, context.getString(R.string.work_main), work.category_id_parent)
    private val workIncome = CategoryChild(36, context.getString(R.string.work_income), work.category_id_parent)
    private val workFood = CategoryChild(37, context.getString(R.string.work_food), work.category_id_parent)

    private val arrayOfWorkSubcategories = arrayOf(workMain, workIncome, workFood)

    fun getListOfWorkSubcategories(): Array<CategoryChild> {
        return arrayOfWorkSubcategories
    }

}