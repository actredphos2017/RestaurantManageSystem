package com.sakuno.restaurantmanagesystem.dataclasses.customer


import com.google.gson.annotations.SerializedName

data class CustomerLoginInfo(
    @SerializedName("account")
    val account: String,
    @SerializedName("password")
    val password: String
)