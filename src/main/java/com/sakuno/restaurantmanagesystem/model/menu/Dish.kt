package com.sakuno.restaurantmanagesystem.model.menu


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Dish(
    @SerializedName("bp")
    var basePrice: Double,
    @SerializedName("commit")
    var commit: String,
    @SerializedName("diy")
    var diyOption: ArrayList<ChoiceList>,
    @SerializedName("name")
    var name: String,
    @SerializedName("pic")
    var picUrl: String?
) {

    fun toJson() = Gson().toJson(this)!!

    fun addDiyOption(name: String, singleChoice: Boolean, values: ArrayList<Choice> = arrayListOf()): ChoiceList =
        ChoiceList(name, singleChoice, values).also { diyOption.add(it) }



    fun removeDiyOption(name: String) = diyOption.removeIf { it.name == name }

    fun removeDiyOption(index: Int) = diyOption.removeAt(index)
}