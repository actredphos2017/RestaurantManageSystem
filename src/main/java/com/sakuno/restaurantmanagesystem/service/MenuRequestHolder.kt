package com.sakuno.restaurantmanagesystem.service

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.sakuno.restaurantmanagesystem.model.menu.Dish
import com.sakuno.restaurantmanagesystem.model.menu.MenuInfo
import com.sakuno.restaurantmanagesystem.model.rest.ReorderCategory
import com.sakuno.restaurantmanagesystem.util.AuthCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.PrintStream


@Component
class MenuRequestHolder {

    @Autowired
    lateinit var restaurantManager: RestaurantManager

    @Autowired
    lateinit var pictureManager: PictureManager

    private val taskMap = mapOf<String, (String, String, String, PrintStream) -> Boolean>(
        "move_up_dish" to ::moveUpDish,
        "delete_dish" to ::deleteDish,
        "add_dish" to ::addDish,
        "delete_category" to ::deleteCategory,
        "add_category" to ::addCategory,
        "edit_dish" to ::editDish,
        "reorder_category" to ::reorderCategory,
        "edit_category" to ::editCategory
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
            errorOs.println("请求数据格式错误")
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

        val targetMenu = restaurantManager.getMenu(id, errorOs)

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

        return restaurantManager.setMenu(id, AuthCode(authCode), targetMenu, errorOs)
    }

    private fun moveUpDish(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {
        val intList = toIntList(2, ",", data, errorOs) ?: return false

        val targetMenu = restaurantManager.getMenu(id, errorOs)

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
        } else restaurantManager.setMenu(id, AuthCode(authCode), targetMenu, errorOs)
    }

    private fun deleteDish(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {
        val intList = toIntList(2, ",", data, errorOs) ?: return false

        val targetMenu = restaurantManager.getMenu(id, errorOs)

        if (targetMenu == null) {
            errorOs.println("ID对应的菜单存在！")
            return false
        }

        val removeResult = targetMenu.getCategory(intList[0])?.removeDish(intList[1])

        return if (removeResult == null) {
            errorOs.println("请求数据值越界")
            false
        } else {
            pictureManager.removeUploadedFile(removeResult.picUrl, errorOs)
            restaurantManager.setMenu(id, AuthCode(authCode), targetMenu, errorOs)
        }
    }

    private fun deleteCategory(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {
        val categoryIndex = data.toIntOrNull();
        if (categoryIndex == null) {
            errorOs.println("请求数据格式错误！")
            return false
        }

        val menu = restaurantManager.getMenu(id, errorOs) ?: return false

        val category = menu.getCategory(categoryIndex)
        if (category == null) {
            errorOs.println("目标菜单不存在！")
            return false
        }

        var allSuccessful = true

        category.dishes.forEach {
            if (!pictureManager.removeUploadedFile(it.picUrl, errorOs)) allSuccessful = false
        }

        if (!allSuccessful) errorOs.println("部分图像删除失败！")

        menu.deleteCategory(categoryIndex)

        return restaurantManager.setMenu(id, AuthCode(authCode), menu, errorOs)
    }

    private fun addCategory(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {
        val menu = restaurantManager.getMenu(id, errorOs)
        if (menu == null) {
            errorOs.println("菜单获取失败！")
            return false
        }
        menu.createCategory(data, null)
        return restaurantManager.setMenu(id, AuthCode(authCode), menu, errorOs)
    }

    private fun editDish(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {
        val cutPoint = data.indexOfFirst { it == '|' }
        if (cutPoint < 0) {
            errorOs.println("请求格式错误！")
            return false
        }

        val coordinateInfo = data.substring(0 until cutPoint)
        val dishInfo = data.substring(cutPoint + 1)

        println(dishInfo)

        val coordinate = toIntList(2, ",", coordinateInfo, errorOs)
        if (coordinate == null) {
            errorOs.println("请求格式错误！")
            return false
        }

        val dish: Dish

        try {
            dish = Gson().fromJson(dishInfo, Dish::class.java)
        } catch (_: JsonSyntaxException) {
            errorOs.println("请求格式错误！")
            return false
        }

        val menu = restaurantManager.getMenu(id, errorOs) ?: return false

        val oldDish = menu.getCategory(coordinate[0])?.getDish(coordinate[1])

        if (oldDish == null) {
            errorOs.println("目标菜品不存在！")
            return false
        }

        dish.picUrl = oldDish.picUrl

        menu.getCategory(coordinate[0])?.dishes?.set(coordinate[1], dish)

        return restaurantManager.setMenu(id, AuthCode(authCode), menu, errorOs)
    }

    private fun reorderCategory(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {
        val oldMenu = restaurantManager.getMenu(id, errorOs)
        if (oldMenu == null) {
            errorOs.println("目标菜单不存在！")
            return false
        }

        val newIndex: ReorderCategory

        try {
            newIndex = Gson().fromJson(data, ReorderCategory::class.java)
            if (newIndex.category.size != oldMenu.categories.size)
                throw Exception()
        } catch (_: Exception) {
            errorOs.println("请求数据格式错误！")
            return false
        }

        val newMenu = MenuInfo(true, arrayListOf())
        try {
            for (index in newIndex.category) {
                newMenu.categories.add(oldMenu.getCategory(index) ?: throw Exception())
            }
        } catch (_: Exception) {
            errorOs.println("未知错误！")
            return false
        }

        return restaurantManager.setMenu(id, AuthCode(authCode), newMenu, errorOs)
    }

    private fun editCategory(id: String, authCode: String, data: String, errorOs: PrintStream): Boolean {

        val cutPoint = data.indexOfFirst { it == '|' }
        if (cutPoint < 0) {
            errorOs.println("请求格式错误！")
            return false
        }

        val categoryIndex = data.substring(0 until cutPoint).toIntOrNull()
        if (categoryIndex == null) {
            errorOs.println("请求格式错误！")
            return false
        }

        val newName = data.substring(cutPoint + 1)

        val menu = restaurantManager.getMenu(id, errorOs)
        if (menu == null) {
            errorOs.println("目标菜单不存在！")
            return false
        }

        menu.getCategory(categoryIndex)?.categoryName = newName
        return restaurantManager.setMenu(id, AuthCode(authCode), menu, errorOs)
    }

}