package com.sakuno.restaurantmanagesystem.model.restaurant


import com.google.gson.annotations.SerializedName

data class RestaurantFullData(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("commit")
    val commit: String,
    @SerializedName("auth_code")
    val authCode: String
)