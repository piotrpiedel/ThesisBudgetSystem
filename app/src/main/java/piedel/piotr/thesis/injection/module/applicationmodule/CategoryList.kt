package piedel.piotr.thesis.injection.module.applicationmodule

import piedel.piotr.thesis.data.model.category.Category

class CategoryList {

    private val food = Category(1, "Food", null)
    private val entertainments = Category(2, "Entertainments", null)
    private val house = Category(3, "House", null)
    private val clothes = Category(4, "Clothes", null)
    private val electronics = Category(5, "Electronics", null)
    private val work = Category(6, "Work", null)

    private val arrayOfCategories = arrayOf(food, entertainments, house, clothes, electronics, work)

    fun getListOfParentCategories(): Array<Category> {
        return arrayOfCategories
    }

    private val foodMain = Category("Main", 1)
    private val foodSchop = Category("Supermarket", 1)
    private val foodWork = Category("Work", 1)
    private val foodFast = Category("FastFoods", 1)
    private val foodCommon = Category("Common", 1)

    private val arrayOfFoodSubcategories = arrayOf(foodMain, foodSchop, foodWork, foodWork, foodFast, foodCommon)

    fun getListOfFoodSubcategories(): Array<Category> {
        return arrayOfFoodSubcategories
    }

    private val entertainmentsMain = Category("entertainments Main", 2)
    private val entertainmentsSchop = Category("entertainments Schop", 2)
    private val entertainmentsWork = Category("entertainments Work", 2)
    private val entertainmentsFast = Category("entertainments Fast", 2)
    private val entertainmentsCommon = Category("entertainments Common", 2)

    private val arrayOfEntertainmentsSubcategories = arrayOf(entertainmentsMain, entertainmentsSchop, entertainmentsWork, entertainmentsFast, entertainmentsCommon)

    fun getListOfEntertainmentSubcategories(): Array<Category> {
        return arrayOfEntertainmentsSubcategories
    }

}