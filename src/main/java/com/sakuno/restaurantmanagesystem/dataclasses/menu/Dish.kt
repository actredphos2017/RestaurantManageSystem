package com.sakuno.restaurantmanagesystem.dataclasses.menu


import com.google.gson.annotations.SerializedName

data class Dish(
    @SerializedName("base_price")
    var basePrice: Double,
    @SerializedName("commit")
    var commit: String,
    @SerializedName("diy_option")
    var diyOption: ArrayList<ChoiceList>,
    @SerializedName("name")
    var name: String,
    @SerializedName("pic_url")
    var picUrl: String
)