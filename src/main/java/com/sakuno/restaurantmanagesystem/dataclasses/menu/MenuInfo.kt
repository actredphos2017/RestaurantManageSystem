package com.sakuno.restaurantmanagesystem.dataclasses.menu


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.PrintStream

data class MenuInfo(
    @SerializedName("available")
    var available: Boolean,
    @SerializedName("categories")
    var categories: ArrayList<DishCategory> = arrayListOf()
) {
    fun createCategory(name: String, dishes: ArrayList<Dish>? = null): DishCategory? =
        if (categories.indexOfFirst { it.categoryName == name } > 0)
            null
        else
            DishCategory(name, dishes ?: arrayListOf()).also { categories.add(it) }


    fun deleteCategory(name: String) = categories.removeIf { it.categoryName == name }

    fun reinsertCategory(index: Int, toIndex: Int): Boolean {
        val size = categories.size
        return if (index !in 0 until size || toIndex !in 0 until size) false
        else if (index == toIndex) true
        else {
            val item = categories[index]
            categories.removeAt(index)
            categories.add(toIndex, item)
            true
        }
    }

    fun swapCategory(firstIndex: Int, secondIndex: Int): Boolean {
        val size = categories.size
        return if (firstIndex !in 0 until size || secondIndex !in 0 until size) false
        else if (firstIndex == secondIndex) true
        else {
            val temp = categories[firstIndex]
            categories[firstIndex] = categories[secondIndex]
            categories[secondIndex] = temp
            true
        }
    }

    fun getCategory(index: Int): DishCategory? = categories.getOrNull(index)

    fun getCategory(name: String): DishCategory? = categories.find { it.categoryName == name }

    fun getJson() = Gson().toJson(this)!!

    companion object {
        val example = MenuInfo(true).apply {

            createCategory("菜品")?.apply {

                addDish(
                    name = "西红柿炒鸡蛋",
                    basePrice = 22.0,
                    commit = "以新鲜的西红柿和鸡蛋为主要材料，加上适量的盐和糖，炒出来的菜肴酸甜可口，营养丰富，可搭配多种主食享用。",
                    picUrl = "EXAMPLE/西红柿炒鸡蛋.jpg"
                ).apply {
                    addDiyOption(
                        name = "加料",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "加葱花",
                        )
                        addChoice(
                            value = "加虾仁",
                            priceDifference = 1.0
                        )
                    }
                    addDiyOption(
                        name = "做法",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "清蒸"
                        )
                        addChoice(
                            value = "蒜蓉"
                        )
                    }
                }

                addDish(
                    name = "红烧肉",
                    basePrice = 38.0,
                    commit = "选用优质的五花肉，佐以适量的酱油、糖和料酒，炖制至入味，香味扑鼻，肉质酥烂，是一道深受人们喜爱的传统美食。",
                    picUrl = "EXAMPLE/红烧肉.jpg"
                ).apply {
                    addDiyOption(
                        name = "加辣",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "微辣",
                        )
                        addChoice(
                            value = "中辣",
                        )
                        addChoice(
                            value = "重辣",
                        )
                    }
                    addDiyOption(
                        name = "份量",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "小份",
                            priceDifference = -5.0
                        )
                        addChoice(
                            value = "普通份",
                        )
                        addChoice(
                            value = "大份",
                            priceDifference = 5.0
                        )
                    }
                }

                addDish(
                    name = "宫保鸡丁",
                    basePrice = 28.0,
                    commit = "选用新鲜的鸡胸肉和花生米，佐以香辣的宫保酱汁，烹制出来的菜肴口感鲜美，味道香辣可口。",
                    picUrl = "EXAMPLE/宫保鸡丁.jpg"
                ).apply {
                    addDiyOption(
                        name = "辣度",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "微辣",
                        )
                        addChoice(
                            value = "中辣",
                        )
                        addChoice(
                            value = "重辣",
                        )
                    }
                    addDiyOption(
                        name = "配菜",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "青椒",
                            priceDifference = 0.0
                        )
                        addChoice(
                            value = "红椒",
                            priceDifference = 0.0
                        )
                        addChoice(
                            value = "土豆丝",
                            priceDifference = 0.0
                        )
                        addChoice(
                            value = "豆角",
                            priceDifference = 1.0
                        )
                    }
                }

                addDish(
                    name = "清蒸鲈鱼",
                    basePrice = 38.0,
                    commit = "鲈鱼肉质细嫩，味道鲜美，清蒸后搭配鲜美的调味汁，香气扑鼻，口感鲜美。",
                    picUrl = "EXAMPLE/清蒸鲈鱼.jpg"
                ).apply {
                    addDiyOption(
                        name = "配菜",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "西兰花",
                            priceDifference = 0.0
                        )
                        addChoice(
                            value = "胡萝卜",
                            priceDifference = 0.0
                        )
                        addChoice(
                            value = "青椒",
                            priceDifference = 0.0
                        )
                        addChoice(
                            value = "香菇",
                            priceDifference = 1.0
                        )
                    }
                    addDiyOption(
                        name = "口味",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "酸甜口味"
                        )
                        addChoice(
                            value = "酱油口味"
                        )
                    }
                }

                addDish(
                    name = "青椒炒肉丝",
                    basePrice = 26.0,
                    commit = "选用新鲜猪肉和青椒，搭配多种调料炒制而成，肉丝鲜嫩多汁，青椒口感清脆爽口，香气扑鼻，是一道下饭的好菜。",
                    picUrl = "EXAMPLE/青椒炒肉丝.jpg"
                ).apply {
                    addDiyOption(
                        name = "加料",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "加葱花"
                        )
                        addChoice(
                            value = "加姜丝"
                        )
                        addChoice(
                            value = "加蒜片"
                        )
                        addChoice(
                            value = "加豆腐干",
                            priceDifference = 2.0
                        )
                    }
                    addDiyOption(
                        name = "辣度",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "微辣"
                        )
                        addChoice(
                            value = "中辣"
                        )
                        addChoice(
                            value = "重辣",
                            priceDifference = 2.0
                        )
                    }
                }

                addDish(
                    name = "糖醋里脊",
                    basePrice = 32.0,
                    commit = "选用猪肉里脊，先用淀粉水抓匀，再油炸至金黄色，最后佐以糖、醋等调料，制成酸甜可口的糖醋里脊。",
                    picUrl = "EXAMPLE/糖醋里脊.jpg"
                ).apply {
                    addDiyOption(
                        name = "加菜",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "胡萝卜",
                            priceDifference = 2.0
                        )
                        addChoice(
                            value = "青椒",
                            priceDifference = 2.0
                        )
                        addChoice(
                            value = "洋葱",
                            priceDifference = 1.5
                        )
                    }
                    addDiyOption(
                        name = "口味",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "微辣"
                        )
                        addChoice(
                            value = "不辣"
                        )
                    }
                }

            }

            createCategory("主食")?.apply {
                addDish(
                    name = "经典意面",
                    basePrice = 38.0,
                    commit = "意面配上经典的意大利红酱，香气扑鼻，美味可口。",
                    picUrl = "EXAMPLE/经典意面.jpg"
                ).apply {
                    addDiyOption(
                        name = "面条类型",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "意大利面",
                        )
                        addChoice(
                            value = "蝴蝶面",
                            priceDifference = 2.0
                        )
                        addChoice(
                            value = "细麵",
                            priceDifference = 1.5
                        )
                    }
                    addDiyOption(
                        name = "配料",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "牛肉丸",
                            priceDifference = 5.0
                        )
                        addChoice(
                            value = "培根",
                            priceDifference = 6.0
                        )
                        addChoice(
                            value = "玉米",
                            priceDifference = 3.0
                        )
                        addChoice(
                            value = "黑橄榄",
                            priceDifference = 4.0
                        )
                    }
                }

                addDish(
                    name = "白米饭",
                    basePrice = 3.0,
                    commit = "香喷喷的白米饭，口感软糯，是搭配各种菜品的最佳主食选择。",
                    picUrl = "EXAMPLE/白米饭.jpg"
                ).apply {
                    addDiyOption(
                        name = "米种",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "东北大米"
                        )
                        addChoice(
                            value = "五常大米",
                        )
                        addChoice(
                            value = "江浙小香米",
                        )
                    }
                    addDiyOption(
                        name = "份量",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "小份"
                        )
                        addChoice(
                            value = "大份",
                            priceDifference = 3.0
                        )
                    }
                }
            }

            createCategory("甜点")?.apply {

                addDish(
                    name = "提拉米苏",
                    basePrice = 35.0,
                    commit = "来自意大利的经典甜点，以手指饼干、马斯卡彭芝士和咖啡为主要原料，口感丰富浓郁，甜而不腻，是下午茶和晚餐甜点的不二之选。",
                    picUrl = "EXAMPLE/提拉米苏.jpg"
                ).apply {
                    addDiyOption(
                        name = "大小",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "小份",
                            priceDifference = -5.0
                        )
                        addChoice(
                            value = "大份",
                            priceDifference = 5.0
                        )
                    }
                    addDiyOption(
                        name = "口味",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "经典"
                        )
                        addChoice(
                            value = "巧克力"
                        )
                    }
                }

                addDish(
                    name = "杏仁豆腐",
                    basePrice = 12.0,
                    commit = "杏仁豆腐是一道以杏仁、鲜奶和糖为主要材料的冷甜点，口感细腻，香甜可口，又不会太腻，非常适合夏天食用。",
                    picUrl = "EXAMPLE/杏仁豆腐.jpg"
                ).apply {
                    addDiyOption(
                        name = "口味",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "原味"
                        )
                        addChoice(
                            value = "加草莓酱",
                            priceDifference = 2.0
                        )
                    }
                    addDiyOption(
                        name = "份量",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "小份",
                            priceDifference = -2.0
                        )
                        addChoice(
                            value = "大份",
                            priceDifference = 2.0
                        )
                    }
                }

                addDish(
                    name = "芝麻糊",
                    basePrice = 12.0,
                    commit = "芝麻糊是一款浓郁香甜的甜点，采用优质芝麻磨制而成，口感香滑细腻，不但营养丰富，还能滋补身体。",
                    picUrl = "EXAMPLE/芝麻糊.jpg"
                ).apply {
                    addDiyOption(
                        name = "甜度",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "少甜",
                            priceDifference = -2.0
                        )
                        addChoice(
                            value = "正常甜"
                        )
                        addChoice(
                            value = "特别甜",
                            priceDifference = 2.0
                        )
                    }
                    addDiyOption(
                        name = "加料",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "糯米球",
                            priceDifference = 3.0
                        )
                        addChoice(
                            value = "红豆",
                            priceDifference = 2.0
                        )
                    }
                }

                addDish(
                    name = "水果沙拉",
                    basePrice = 28.0,
                    commit = "将新鲜的水果切成适当的大小，搭配新鲜的薄荷叶和柠檬汁，清新爽口，是夏天的必选甜点。",
                    picUrl = "EXAMPLE/水果沙拉.jpg"
                ).apply {
                    addDiyOption(
                        name = "选择水果",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "草莓",
                        )
                        addChoice(
                            value = "芒果",
                        )
                        addChoice(
                            value = "蓝莓",
                            priceDifference = 2.0
                        )
                        addChoice(
                            value = "猕猴桃",
                            priceDifference = 2.0
                        )
                    }
                    addDiyOption(
                        name = "加料",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "蜂蜜",
                        )
                        addChoice(
                            value = "奶油",
                            priceDifference = 3.0
                        )
                    }
                }
            }

            createCategory("汤羹")?.apply {

                addDish(
                    name = "玉米羹",
                    basePrice = 18.0,
                    commit = "将鲜嫩的玉米粒熬煮成浓浓的羹汤，口感清香绵滑，适合各个年龄层的食客品尝。",
                    picUrl = "EXAMPLE/玉米羹.jpg"
                ).apply {
                    addDiyOption(
                        name = "加料",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "鸡蛋",
                            priceDifference = 2.0
                        )
                        addChoice(
                            value = "火腿",
                            priceDifference = 3.0
                        )
                        addChoice(
                            value = "蟹棒",
                            priceDifference = 4.0
                        )
                    }
                    addDiyOption(
                        name = "口味",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "清淡"
                        )
                        addChoice(
                            value = "鲜香"
                        )
                        addChoice(
                            value = "浓郁"
                        )
                    }
                }

                addDish(
                    name = "南瓜汤羹",
                    basePrice = 26.0,
                    commit = "选用新鲜的南瓜为主料，加上少许鸡蛋、盐、胡椒粉等调味料制成，口感绵密，香气四溢。",
                    picUrl = "EXAMPLE/南瓜汤羹.jpg"
                ).apply {
                    addDiyOption(
                        name = "加料",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "肉丸",
                            priceDifference = 3.0
                        )
                        addChoice(
                            value = "豆腐",
                            priceDifference = 2.0
                        )
                        addChoice(
                            value = "鲜虾",
                            priceDifference = 5.0
                        )
                    }
                    addDiyOption(
                        name = "口味",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "原味"
                        )
                        addChoice(
                            value = "加辣",
                            priceDifference = 2.0
                        )
                    }
                }

            }

            createCategory("饮品")?.apply {

                addDish(
                    name = "奶茶",
                    basePrice = 18.0,
                    commit = "选用优质奶粉和茶叶，加入适量糖浆和珍珠，口感浓郁丰富，是经典的台式饮品。",
                    picUrl = "EXAMPLE/奶茶.jpg"
                ).apply {
                    addDiyOption(
                        name = "茶底",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "红茶"
                        )
                        addChoice(
                            value = "绿茶"
                        )
                        addChoice(
                            value = "奶绿",
                            priceDifference = 1.0
                        )
                    }
                    addDiyOption(
                        name = "甜度",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "无糖"
                        )
                        addChoice(
                            value = "少糖"
                        )
                        addChoice(
                            value = "正常糖",
                        )
                        addChoice(
                            value = "多糖",
                        )
                    }
                    addDiyOption(
                        name = "加料",
                        singleChoice = false
                    ).apply {
                        addChoice(
                            value = "珍珠(免费)"
                        )
                        addChoice(
                            value = "布丁",
                            priceDifference = 1.0
                        )
                        addChoice(
                            value = "仙草",
                            priceDifference = 1.0
                        )
                    }
                }

                addDish(
                    name = "可乐",
                    basePrice = 2.5,
                    commit = "以高质量的原料酿制而成，口感独特，留香回味。",
                    picUrl = "EXAMPLE/可乐.jpg"
                ).apply {
                    addDiyOption(
                        name = "包装",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "罐装",
                            priceDifference = 0.0
                        )
                        addChoice(
                            value = "瓶装",
                            priceDifference = 1.0
                        )
                    }
                }

                addDish(
                    name = "雪碧",
                    basePrice = 2.5,
                    commit = "以天然柠檬味和清凉气泡为主要特点，适合各个年龄段的消费者。",
                    picUrl = "EXAMPLE/雪碧.jpg"
                ).apply {
                    addDiyOption(
                        name = "包装",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "罐装",
                            priceDifference = 0.0
                        )
                        addChoice(
                            value = "瓶装",
                            priceDifference = 1.0
                        )
                    }
                }

                addDish(
                    name = "芬达",
                    basePrice = 2.5,
                    commit = "以多种水果口味为主要特点，清新可口，口感醇厚。",
                    picUrl = "EXAMPLE/芬达.jpg"
                ).apply {
                    addDiyOption(
                        name = "包装",
                        singleChoice = true
                    ).apply {
                        addChoice(
                            value = "罐装",
                            priceDifference = 0.0
                        )
                        addChoice(
                            value = "瓶装",
                            priceDifference = 1.0
                        )
                    }
                }

                addDish(
                    name = "王老吉",
                    basePrice = 3.0,
                    commit = "由多种中药材和天然植物精华熬制而成，口感清爽，带有微甜，有助于清热解毒、生津止渴。",
                    picUrl = "EXAMPLE/王老吉.jpg"
                )
            }

        }
    }

}