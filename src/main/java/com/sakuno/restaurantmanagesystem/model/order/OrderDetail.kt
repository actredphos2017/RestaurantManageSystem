package com.sakuno.restaurantmanagesystem.model.order


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class OrderDetail(
    @SerializedName("c_id")
    var cId: String,
    @SerializedName("dishes")
    var dishes: List<OrderDish>,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("seat")
    var seat: String
) {
    fun toJson() = Gson().toJson(this)!!
}