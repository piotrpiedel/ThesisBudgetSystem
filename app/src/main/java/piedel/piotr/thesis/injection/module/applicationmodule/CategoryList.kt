package piedel.piotr.thesis.injection.module.applicationmodule

import piedel.piotr.thesis.data.model.category.Category

class CategoryList {
    /////////////////////
    private val uncategorized = Category(999, "Uncategorized", null)
    private val food = Category(1, "Food", null)
    private val entertainments = Category(2, "Entertainments", null)
    private val house = Category(3, "House", null)
    private val clothes = Category(4, "Clothes", null)
    private val electronics = Category(5, "Electronics", null)
    private val work = Category(6, "Work", null)

    private val arrayOfCategories = arrayOf(uncategorized, food, entertainments, house, clothes, electronics, work)

    fun getListOfParentCategories(): Array<Category> {
        return arrayOfCategories
    }

    /////////////////////
    private val foodMain = Category(7, "Food - Main ", 1)
    private val foodSchop = Category(8, " Food -Supermarket", 1)
    private val foodWork = Category(9, " Food - Work", 1)
    private val foodFast = Category(10, " Food - FastFoods", 1)
    private val foodCommon = Category(11, "Food - Common", 1)

    private val arrayOfFoodSubcategories = arrayOf(foodMain, foodSchop, foodWork, foodWork, foodFast, foodCommon)

    fun getListOfFoodSubcategories(): Array<Category> {
        return arrayOfFoodSubcategories
    }

    /////////////////////
    private val entertainmentsMain = Category(12, "Entertainments - Main", 2)
    private val entertainmentsShop = Category(13, "Entertainments - Shop", 2)
    private val entertainmentsWork = Category(14, "Entertainments - Work", 2)
    private val entertainmentsFast = Category(15, "Entertainments - Fast", 2)
    private val entertainmentsCommon = Category(16, "Entertainments - Common", 2)

    private val arrayOfEntertainmentsSubcategories = arrayOf(entertainmentsMain, entertainmentsShop, entertainmentsWork, entertainmentsFast, entertainmentsCommon)

    fun getListOfEntertainmentSubcategories(): Array<Category> {
        return arrayOfEntertainmentsSubcategories
    }

    /////////////////////
    private val houseMain = Category(17, "House - Main", 3)
    private val houseShop = Category(18, "House - Maintain", 3)
    private val houseFurniture = Category(19, "House - Furniture", 3)
    private val houseFixes = Category(20, "House - Fixes", 3)
    private val houseCommon = Category(21, "House - Common", 3)
    private val houseBills = Category(22, "House - Bills", 3)

    private val arrayOfHouseSubcategories = arrayOf(houseMain, houseShop, houseFurniture, houseFixes, houseCommon, houseBills)

    fun getListOfHouseSubcategories(): Array<Category> {
        return arrayOfHouseSubcategories
    }

    /////////////////////
    private val clothesMain = Category(23, "Clothes - Main", 4)
    private val clothesShop = Category(24, "Clothes - Shop", 4)
    private val clothesKids = Category(25, "Clothes - Kids", 4)
    private val clothesBoots = Category(26, "Clothes - Boots", 4)
    private val clothesHats = Category(27, "Clothes - Hats", 4)
    private val clothesCasual = Category(28, "Clothes - Casual", 4)

    private val arrayOfClothesSubcategories = arrayOf(clothesMain, clothesShop, clothesKids, clothesBoots, clothesHats, clothesCasual)

    fun getListOfClothesSubcategories(): Array<Category> {
        return arrayOfClothesSubcategories
    }

    /////////////////////
    private val electronicsMain = Category(29, "Electronic - Main", 5)
    private val electronicsShop = Category(30, "Electronic - Shop", 5)
    private val electronicsKids = Category(31, "Electronic - Kids", 5)
    private val electronicsChina = Category(32, "Electronic - China", 5)
    private val electronicsFun = Category(33, "Electronic - Fun", 5)
    private val electronicsSmall = Category(34, "Electronic - Small ", 5)

    private val arrayOfElectronicsSubcategories = arrayOf(electronicsMain, electronicsShop, electronicsKids, electronicsChina, electronicsFun, electronicsSmall)

    fun getListOfElectronicsSubcategories(): Array<Category> {
        return arrayOfElectronicsSubcategories
    }

    /////////////////////
    private val workMain = Category(35, "Work - Main", 6)
    private val workIncome = Category(36, "Work - Income", 6)
    private val workFood = Category(37, "Work - Food", 6)

    private val arrayOfWorkSubcategories = arrayOf(workMain, workIncome, workFood)

    fun getListOfWorkSubcategories(): Array<Category> {
        return arrayOfWorkSubcategories
    }

}