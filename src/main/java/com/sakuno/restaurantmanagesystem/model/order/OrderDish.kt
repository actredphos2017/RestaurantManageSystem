package com.sakuno.restaurantmanagesystem.model.order


import com.google.gson.annotations.SerializedName

data class OrderDish(
        @SerializedName("amount")
        var amount: Int,
        @SerializedName("c_index")
        var cIndex: Int,
        @SerializedName("c_name")
        var cName: String,
        @SerializedName("d_index")
        var dIndex: Int,
        @SerializedName("d_name")
        var dName: String,
        @SerializedName("diy")
        var diy: List<OrderDiy>
) {
    fun getDiyString(): String {
        val stringBuilder = StringBuilder()

        for ((diyIndex, diyItem) in diy.withIndex()) {

            if(diyIndex == 0) stringBuilder.append("   ")
            stringBuilder.append("${diyItem.name}：")

            for ((optionIndex, optionItem) in diyItem.values.withIndex()) {
                if (optionIndex != 0) stringBuilder.append(", ")
                stringBuilder.append(optionItem.name)
            }
        }

        return stringBuilder.toString()
    }
}