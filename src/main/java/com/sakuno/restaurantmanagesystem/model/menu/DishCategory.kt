package com.sakuno.restaurantmanagesystem.model.menu


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

    fun removeDish(index: Int) = dishes.removeAt(index)

    fun getDish(index: Int): Dish? = dishes.getOrNull(index)

    fun reinsertDish(index: Int, toIndex: Int): Boolean {
        val size = dishes.size
        return if (index !in 0 until size || toIndex !in 0 until size) false
        else if (index == toIndex) true
        else {
            val item = dishes[index]
            dishes.removeAt(index)
            dishes.add(toIndex, item)
            true
        }
    }
}