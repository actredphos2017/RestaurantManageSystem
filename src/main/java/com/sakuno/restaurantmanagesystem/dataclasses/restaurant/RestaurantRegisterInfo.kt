package com.sakuno.restaurantmanagesystem.dataclasses.restaurant


import com.google.gson.annotations.SerializedName

data class RestaurantRegisterInfo(
    @SerializedName("address")
    val address: String,
    @SerializedName("commit")
    val commit: String,
    @SerializedName("headPic")
    var headPic: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("managePassword")
    val managePassword: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String
) {
    fun check(): Boolean = id.isNotEmpty() && managePassword.isNotEmpty() && name.isNotEmpty()
}