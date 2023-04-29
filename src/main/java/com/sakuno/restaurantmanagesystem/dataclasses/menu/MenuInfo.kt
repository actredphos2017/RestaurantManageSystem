package com.sakuno.restaurantmanagesystem.dataclasses.menu


import com.google.gson.annotations.SerializedName

data class MenuInfo(
    @SerializedName("available")
    var available: Boolean,
    @SerializedName("categories")
    var categories: ArrayList<DishCategory>
) {
    fun createCategory(name: String, dishes: ArrayList<Dish>? = null): Boolean =
        if (categories.indexOfFirst { it.categoryName == name } > 0)
            false
        else {
            categories.add(DishCategory(name, dishes ?: arrayListOf()))
            true
        }

    fun deleteCategory(name: String) = categories.removeIf { it.categoryName == name }

    fun reinsertCategory(index: Int, toIndex: Int): Boolean {
        val size = categories.size
        return if (index !in 0 until size || toIndex !in 0 until size) false
        else if (index == toIndex) true
        else {
            val item = categories[index]
            categories.removeAt(index)
            categories.add(toIndex, item)
            true
        }
    }

    fun swapCategory(firstIndex: Int, secondIndex: Int): Boolean {
        val size = categories.size
        return if (firstIndex !in 0 until size || secondIndex !in 0 until size) false
        else if (firstIndex == secondIndex) true
        else {
            val temp = categories[firstIndex]
            categories[firstIndex] = categories[secondIndex]
            categories[secondIndex] = temp
            true
        }
    }

}