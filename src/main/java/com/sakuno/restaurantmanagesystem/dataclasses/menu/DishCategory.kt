package com.sakuno.restaurantmanagesystem.dataclasses.menu


import com.google.gson.annotations.SerializedName

data class DishCategory(
    @SerializedName("category_name")
    var categoryName: String,
    @SerializedName("dishes")
    var dishes: ArrayList<Dish>
) {

    fun addDish(
        basePrice: Double,
        commit: String,
        diyOption: ArrayList<ChoiceList>,
        name: String,
        picUrl: String
    ) {
        dishes.add(
            Dish(
                basePrice = basePrice,
                commit = commit,
                diyOption = diyOption,
                name = name,
                picUrl = picUrl
            )
        )
    }

    fun removeDish(name: String): Boolean =
        dishes.removeIf { it.name == name }

    fun removeDish(index: Int): Boolean =
        if (index !in 0 until dishes.size) false
        else {
            dishes.removeAt(index)
            true
        }
}