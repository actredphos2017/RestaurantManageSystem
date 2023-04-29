package com.sakuno.restaurantmanagesystem.dataclasses.menu


import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("price_difference")
    var priceDifference: Double,
    @SerializedName("value")
    var value: String,
    @SerializedName("commit")
    var commit: String
)