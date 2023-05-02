package com.sakuno.restaurantmanagesystem.dataclasses.rests


import com.google.gson.annotations.SerializedName

data class ReorderCategory(
    @SerializedName("category")
    var category: List<Int>
)