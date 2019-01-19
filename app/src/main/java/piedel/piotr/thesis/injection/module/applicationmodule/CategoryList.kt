package piedel.piotr.thesis.injection.module.applicationmodule

import android.app.Application
import android.content.Context
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild

class CategoryList(application: Application) {
    /////////////////////
    val context: Context = application.applicationContext
    private val uncategorized = CategoryChild(999, context.getString(R.string.uncategorized), null)
    private val food = CategoryChild(1, context.getString(R.string.food), null)
    private val entertainments = CategoryChild(2, context.getString(R.string.entertainments), null)
    private val house = CategoryChild(3, context.getString(R.string.house), null)
    private val clothes = CategoryChild(4, context.getString(R.string.clothes), null)
    private val electronics = CategoryChild(5, context.getString(R.string.electronics), null)
    private val work = CategoryChild(6, context.getString(R.string.work), null)

    private val arrayOfCategories = arrayOf(uncategorized, food, entertainments, house, clothes, electronics, work)

    fun getListOfParentCategories(): Array<CategoryChild> {
        return arrayOfCategories
    }

    /////////////////////
    private val foodMain = CategoryChild(7, context.getString(R.string.food_main), 1)
    private val foodSchop = CategoryChild(8, context.getString(R.string.food_supermarket), 1)
    private val foodWork = CategoryChild(9, context.getString(R.string.food_work), 1)
    private val foodFast = CategoryChild(10, context.getString(R.string.food_fast_foods), 1)
    private val foodCommon = CategoryChild(11, context.getString(R.string.food_common), 1)

    private val arrayOfFoodSubcategories = arrayOf(foodMain, foodSchop, foodWork, foodWork, foodFast, foodCommon)

    fun getListOfFoodSubcategories(): Array<CategoryChild> {
        return arrayOfFoodSubcategories
    }

    /////////////////////
    private val entertainmentsMain = CategoryChild(12, context.getString(R.string.entertainments_main), 2)
    private val entertainmentsShop = CategoryChild(13, context.getString(R.string.entertainments_shop), 2)
    private val entertainmentsWork = CategoryChild(14, context.getString(R.string.entertainments_work), 2)
    private val entertainmentsFast = CategoryChild(15, context.getString(R.string.entertainments_fast), 2)
    private val entertainmentsCommon = CategoryChild(16, context.getString(R.string.entertainments_common), 2)

    private val arrayOfEntertainmentsSubcategories = arrayOf(entertainmentsMain, entertainmentsShop, entertainmentsWork, entertainmentsFast, entertainmentsCommon)

    fun getListOfEntertainmentSubcategories(): Array<CategoryChild> {
        return arrayOfEntertainmentsSubcategories
    }

    /////////////////////
    private val houseMain = CategoryChild(17, context.getString(R.string.house_main), 3)
    private val houseShop = CategoryChild(18, context.getString(R.string.house_maintain), 3)
    private val houseFurniture = CategoryChild(19, context.getString(R.string.house_furniture), 3)
    private val houseFixes = CategoryChild(20, context.getString(R.string.house_fixes), 3)
    private val houseCommon = CategoryChild(21, context.getString(R.string.house_common), 3)
    private val houseBills = CategoryChild(22, context.getString(R.string.house_bills), 3)

    private val arrayOfHouseSubcategories = arrayOf(houseMain, houseShop, houseFurniture, houseFixes, houseCommon, houseBills)

    fun getListOfHouseSubcategories(): Array<CategoryChild> {
        return arrayOfHouseSubcategories
    }

    /////////////////////
    private val clothesMain = CategoryChild(23, context.getString(R.string.clothes_main), 4)
    private val clothesShop = CategoryChild(24, context.getString(R.string.clothes_shop), 4)
    private val clothesKids = CategoryChild(25, context.getString(R.string.clothes_kids), 4)
    private val clothesBoots = CategoryChild(26, context.getString(R.string.clothes_boots), 4)
    private val clothesHats = CategoryChild(27, context.getString(R.string.clothes_hats), 4)
    private val clothesCasual = CategoryChild(28, context.getString(R.string.clothes_casul), 4)

    private val arrayOfClothesSubcategories = arrayOf(clothesMain, clothesShop, clothesKids, clothesBoots, clothesHats, clothesCasual)

    fun getListOfClothesSubcategories(): Array<CategoryChild> {
        return arrayOfClothesSubcategories
    }

    /////////////////////
    private val electronicsMain = CategoryChild(29, context.getString(R.string.electronic_main), 5)
    private val electronicsShop = CategoryChild(30, context.getString(R.string.electronic_shop), 5)
    private val electronicsKids = CategoryChild(31, context.getString(R.string.electronic_kids), 5)
    private val electronicsChina = CategoryChild(32, context.getString(R.string.electronic_china), 5)
    private val electronicsFun = CategoryChild(33, context.getString(R.string.electronic_fun), 5)
    private val electronicsSmall = CategoryChild(34, context.getString(R.string.electronic_small), 5)

    private val arrayOfElectronicsSubcategories = arrayOf(electronicsMain, electronicsShop, electronicsKids, electronicsChina, electronicsFun, electronicsSmall)

    fun getListOfElectronicsSubcategories(): Array<CategoryChild> {
        return arrayOfElectronicsSubcategories
    }

    /////////////////////
    private val workMain = CategoryChild(35, context.getString(R.string.work_main), 6)
    private val workIncome = CategoryChild(36, context.getString(R.string.work_income), 6)
    private val workFood = CategoryChild(37, context.getString(R.string.work_food), 6)

    private val arrayOfWorkSubcategories = arrayOf(workMain, workIncome, workFood)

    fun getListOfWorkSubcategories(): Array<CategoryChild> {
        return arrayOfWorkSubcategories
    }

}