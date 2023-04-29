package com.sakuno.restaurantmanagesystem.utils

import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class SakunoUtils {
    fun getDateString(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
    }

    fun toMD5Code(vararg items: String): String {
        var spliceString = ""
        items.forEach { spliceString += "${it}|" }
        return MessageDigest
            .getInstance("MD5")
            .apply { update(spliceString.toByte()) }
            .digest()
            .toString()
            .substring(0..15)
    }
}