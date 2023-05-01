package com.sakuno.restaurantmanagesystem.dataclasses.rests

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("ok")
    val result: Boolean,
    @SerializedName("reason")
    val reason: String
) {
    companion object {
        fun success() = TaskResponse(true, "")
        fun failed(reason: String) = TaskResponse(false, reason)
    }

    fun toJson() = Gson().toJson(this)!!
}