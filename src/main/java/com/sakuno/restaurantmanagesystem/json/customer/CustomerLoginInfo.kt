package com.sakuno.restaurantmanagesystem.json.customer


import com.google.gson.annotations.SerializedName

data class CustomerLoginInfo(
    @SerializedName("account")
    val account: String,
    @SerializedName("password")
    val password: String
)