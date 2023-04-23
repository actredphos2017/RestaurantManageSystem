package com.sakuno.restaurantmanagesystem.json.customer


import com.google.gson.annotations.SerializedName

data class CustomerRegisterInfo(
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("username")
    val username: String
) {

}