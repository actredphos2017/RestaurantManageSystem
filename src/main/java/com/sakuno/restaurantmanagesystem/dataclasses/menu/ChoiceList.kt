package com.sakuno.restaurantmanagesystem.dataclasses.menu

import com.google.gson.annotations.SerializedName

data class ChoiceList(
    @SerializedName("name")
    var name: String,
    @SerializedName("sc")
    var singleChoice: Boolean,
    @SerializedName("values")
    var values: ArrayList<Choice>
) {
    fun addChoice(
        value: String,
        priceDifference: Double = 0.0,
        commit: String = ""
    ): Boolean =
        if (values.indexOfFirst { it.value == value } >= 0)
            false
        else {
            values.add(Choice(priceDifference, value, commit))
            true
        }

    fun removeChoice(index: Int): Boolean =
        if (index !in 0 until values.size)
            false
        else {
            values.removeAt(index)
            true
        }


    fun removeChoice(name: String): Boolean = values.removeIf { it.value == name }


}