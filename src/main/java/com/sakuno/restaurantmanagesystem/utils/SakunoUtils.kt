package com.sakuno.restaurantmanagesystem.utils

import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SakunoUtils {

    fun getDateString(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))

    fun getMillString(): String = System.currentTimeMillis().toString()

    fun toMD5Code(vararg items: String): String {
        var spliceString = ""
        items.forEach { spliceString += "${it}#" }
        return MessageDigest
            .getInstance("MD5")
            .apply { update(spliceString.toByteArray()) }
            .digest()
            .toString()
    }

}