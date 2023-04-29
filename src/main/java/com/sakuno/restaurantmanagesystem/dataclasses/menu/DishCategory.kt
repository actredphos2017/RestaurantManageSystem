package com.sakuno.restaurantmanagesystem.dataclasses.menu


import com.google.gson.annotations.SerializedName

data class DishCategory(
    @SerializedName("name")
    var categoryName: String,
    @SerializedName("dishes")
    var dishes: ArrayList<Dish>
) {

    fun addDish(
        basePrice: Double,
        commit: String,
        diyOption: ArrayList<ChoiceList> = arrayListOf(),
        name: String,
        picUrl: String
    ): Dish = Dish(
        basePrice = basePrice,
        commit = commit,
        diyOption = diyOption,
        name = name,
        picUrl = picUrl
    ).also { dishes.add(it) }


    fun removeDish(name: String): Boolean =
        dishes.removeIf { it.name == name }

    fun removeDish(index: Int): Boolean =
        if (index !in 0 until dishes.size) false
        else {
            dishes.removeAt(index)
            true
        }
}