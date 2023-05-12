package com.sakuno.restaurantmanagesystem.model.menu


import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("pd")
    var priceDifference: Double,
    @SerializedName("value")
    var value: String,
    @SerializedName("commit")
    var commit: String
)