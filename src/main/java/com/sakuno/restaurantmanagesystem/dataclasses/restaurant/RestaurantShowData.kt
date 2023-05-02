package com.sakuno.restaurantmanagesystem.dataclasses.restaurant

import com.google.gson.annotations.SerializedName
import com.sakuno.restaurantmanagesystem.dataclasses.menu.MenuInfo

data class RestaurantShowData(
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
    @SerializedName("menu")
    val menu: MenuInfo
)