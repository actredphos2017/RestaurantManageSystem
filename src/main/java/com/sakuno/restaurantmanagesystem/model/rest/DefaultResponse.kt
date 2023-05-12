package com.sakuno.restaurantmanagesystem.model.rest

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class DefaultResponse(
    @SerializedName("ok")
    val result: Boolean,
    @SerializedName("reason")
    val reason: String
) {
    companion object {
        fun success() = DefaultResponse(true, "")
        fun failed(reason: String) = DefaultResponse(false, reason)
    }

    fun toJson() = Gson().toJson(this)!!
}