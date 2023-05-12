package com.sakuno.restaurantmanagesystem.model.order


import com.google.gson.annotations.SerializedName

data class OrderDiy(
    @SerializedName("index")
    var index: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("values")
    var values: List<OrderDiyValue>
)