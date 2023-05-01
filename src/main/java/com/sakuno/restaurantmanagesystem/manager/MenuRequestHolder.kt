package com.sakuno.restaurantmanagesystem.manager

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.sakuno.restaurantmanagesystem.dataclasses.menu.Dish
import com.sakuno.restaurantmanagesystem.utils.AuthCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.PrintStream


@Component
class MenuRequestHolder {

    @Autowired
    lateinit var manager: RestaurantManager

    private val taskMap = mapOf<String, (String, String, String, PrintStream) -> Boolean>(
        "move_up_dish" to ::moveUpDish,
        "delete_dish" to ::deleteDish,
        "add_dish" to ::addDish
    )

    fun holder(taskName: String, id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {
        return taskMap.getOrDefault(taskName) { _, _, _, eo ->
            eo.println("没有找到指定的请求任务")
            false
        }(id, authCode, data, errorOs)
    }

     fun toIntList(limitSize: Int, separator: String, data: String, errorOs: PrintStream): List<Int>? {
        val coordinate = data.split(separator)
        if (coordinate.size != limitSize) {
            errorOs.println("请求数据格式错误")
            return null
        }

        val intList = ArrayList<Int>()
        return try {
            coordinate.forEach { it.toIntOrNull()?.apply { intList.add(this) } ?: throw Exception() }
            intList.toList()
        } catch (_: Exception) {
            errorOs.println("请求数据格式错误")
            null
        }
    }


    private fun addDish(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {

        val cutPoint = data.indexOfFirst { it == '|' }

        if (cutPoint < 0) {
            errorOs.println("请求数据格式错误1")
            return false
        }

        val categoryIndex = data.substring(0 until cutPoint).toIntOrNull()

        if (categoryIndex == null) {
            errorOs.println("请求数据格式错误")
            return false
        }

        val newDish: Dish

        try {
            newDish = Gson().fromJson(data.substring(cutPoint + 1), Dish::class.java)
        } catch (_: JsonSyntaxException) {
            errorOs.println("请求数据格式错误")
            return false
        }

        val targetMenu = manager.getMenu(id, errorOs)

        if (targetMenu == null) {
            errorOs.println("ID对应的菜单存在！")
            return false
        }

        try {
            targetMenu.getCategory(categoryIndex)!!.dishes.add(newDish)
        } catch (e: Exception) {
            errorOs.println("目标分类索引不存在！")
            return false
        }

        return manager.setMenu(id, AuthCode(authCode), targetMenu, errorOs)
    }

    private fun moveUpDish(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {
        val intList = toIntList(2, ",", data, errorOs) ?: return false

        val targetMenu = manager.getMenu(id, errorOs)

        if (targetMenu == null) {
            errorOs.println("ID对应的菜单存在！")
            return false
        }

        val upSetResult = targetMenu.getCategory(intList[0])?.reinsertDish(intList[1], 0)

        return if (upSetResult == null) {
            errorOs.println("请求数据值越界")
            false
        } else if (!upSetResult) {
            errorOs.println("请求数据值越界")
            false
        } else manager.setMenu(id, AuthCode(authCode), targetMenu, errorOs)
    }

    private fun deleteDish(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {
        val intList = toIntList(2, ",", data, errorOs) ?: return false

        val targetMenu = manager.getMenu(id, errorOs)

        if (targetMenu == null) {
            errorOs.println("ID对应的菜单存在！")
            return false
        }

        val removeResult = targetMenu.getCategory(intList[0])?.removeDish(intList[1])

        return if (removeResult == null) {
            errorOs.println("请求数据值越界")
            false
        } else if (!removeResult) {
            errorOs.println("请求数据值越界")
            false
        } else manager.setMenu(id, AuthCode(authCode), targetMenu, errorOs)
    }

}