package com.sakuno.restaurantmanagesystem.model.rest


import com.google.gson.annotations.SerializedName

data class ReorderCategory(
    @SerializedName("category")
    var category: List<Int>
)