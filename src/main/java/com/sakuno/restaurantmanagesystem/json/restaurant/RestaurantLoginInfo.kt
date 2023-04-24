package com.sakuno.restaurantmanagesystem.json.restaurant


import com.google.gson.annotations.SerializedName

data class RestaurantLoginInfo(
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)