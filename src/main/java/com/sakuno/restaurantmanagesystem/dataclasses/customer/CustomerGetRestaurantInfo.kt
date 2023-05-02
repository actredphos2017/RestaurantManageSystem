package com.sakuno.restaurantmanagesystem.dataclasses.customer

import com.google.gson.annotations.SerializedName
import com.sakuno.restaurantmanagesystem.dataclasses.restaurant.RestaurantShowData

data class CustomerGetRestaurantInfo(
    @SerializedName("ok")
    val ok: Boolean,
    @SerializedName("info")
    val restaurantInfo: RestaurantShowData?,
    @SerializedName("reason")
    val reason: String
) {
    companion object {
        fun success(restaurantInfo: RestaurantShowData) = CustomerGetRestaurantInfo(true, restaurantInfo, "")

        fun failed(reason: String) = CustomerGetRestaurantInfo(false, null, reason)
    }
}