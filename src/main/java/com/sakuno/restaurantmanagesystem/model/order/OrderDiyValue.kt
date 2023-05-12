package com.sakuno.restaurantmanagesystem.model.order


import com.google.gson.annotations.SerializedName

data class OrderDiyValue(
    @SerializedName("index")
    var index: Int,
    @SerializedName("name")
    var name: String
)