package com.sakuno.restaurantmanagesystem.model.rest

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class OrderPushResponse(
    @SerializedName("ok")
    val ok: Boolean,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("money")
    val money: Double?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("fail_list")
    val failedList: List<Int>?
) {

    fun toJson() = Gson().toJson(this)!!

    companion object {
        fun failed(reason: String) = OrderPushResponse(false, reason, null, null, null)

        fun success(money: Double, id: String, failedList: List<Int>) = OrderPushResponse(true, null, money, id, failedList)
    }
}