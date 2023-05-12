package com.sakuno.restaurantmanagesystem.model.order


import com.google.gson.annotations.SerializedName

data class OrderFullInfo(
        @SerializedName("detail")
        var orderDetail: OrderDetail,
        @SerializedName("dt")
        var dt: String,
        @SerializedName("money")
        var money: Double,
        @SerializedName("o_id")
        var oId: String,
        @SerializedName("r_id")
        var rId: String,
        @SerializedName("fail_list")
        var failedList: List<Int>
)