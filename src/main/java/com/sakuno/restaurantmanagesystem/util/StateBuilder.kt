package com.sakuno.restaurantmanagesystem.util

import java.lang.Exception

class StateBuilder {

    companion object {
        fun select() = Select()

        fun insert() = Insert()

        fun delete() = Delete()

        fun update() = Update()

        fun toVarchar(string: String): String = "\'$string\'"
    }

    class Select {
        private var tableName: String? = null
        private var columns: List<String>? = null
        private var conditions: ArrayList<Pair<String, Any>> = ArrayList()

        fun from(table: String): Select {
            tableName = table
            return this
        }

        fun forColumns(vararg column: String): Select {
            columns = column.toList()
            return this
        }

        fun withCondition(column: String, value: Any): Select {
            conditions.add(Pair(column, value))
            return this
        }

        fun build(): String = if (tableName == null) {
            throw Exception("Select: 缺少参数")
        } else StringBuilder().apply {
            append("select ")
            if (columns == null)
                append("*")
            else for ((index, value) in columns!!.withIndex())
                if (index == 0)
                    append(value)
                else
                    append(", $value")
            append(" from $tableName")
            if (conditions.isNotEmpty()) {
                append(" where ")
                for ((index, value) in conditions.withIndex()) {
                    if (value.second is String) {
                        if (index == 0)
                            append("${value.first} = ${toVarchar(value.second as String)}")
                        else
                            append(" and ${value.first} = ${toVarchar(value.second as String)}")
                    } else {
                        if (index == 0)
                            append("${value.first} = ${value.second}")
                        else
                            append(" and ${value.first} = ${value.second}")
                    }
                }
            }
        }.toString()

    }

    class Insert {
        private var tableName: String? = null
        private var columns: List<String>? = null
        private var values: List<Any>? = null

        fun into(table: String): Insert {
            tableName = table
            return this
        }

        fun forColumns(vararg column: String): Insert {
            columns = column.toList()
            return this
        }

        fun withItems(vararg item: Any): Insert {
            values = item.toList()
            return this
        }

        fun build(): String =
                if (tableName == null || columns == null || values == null) {
                    throw Exception("Insert: 缺少参数")
                } else if (columns!!.size != values!!.size) {
                    throw Exception("Insert: 参数数与值不匹配")
                } else
                    StringBuilder().apply {
                        append("insert into $tableName(")
                        for ((index, value) in columns!!.withIndex())
                            if (index == 0)
                                append(value)
                            else
                                append(", $value")
                        append(") values (")
                        for ((index, value) in values!!.withIndex()) {
                            if (value is String) {
                                if (index == 0)
                                    append(toVarchar(value))
                                else
                                    append(", ${toVarchar(value)}")
                            } else {
                                if (index == 0)
                                    append(value)
                                else
                                    append(", $value")
                            }
                        }
                        append(")")
                    }.toString()
    }

    class Delete {
        private var tableName: String? = null
        private var conditions: ArrayList<Pair<String, Any>> = ArrayList()

        fun from(table: String): Delete {
            tableName = table
            return this
        }

        fun withCondition(column: String, value: Any): Delete {
            conditions.add(Pair(column, value))
            return this
        }

        fun build(): String = if (tableName == null) {
            throw Exception("Delete: 缺少参数")
        } else StringBuilder().apply {
            append("delete from $tableName where ")
            if (conditions.isNotEmpty())
                for ((index, value) in conditions.withIndex()) {
                    if (value.second is String) {
                        if (index == 0)
                            append("${value.first} = ${toVarchar(value.second as String)}")
                        else
                            append(" and ${value.first} = ${toVarchar(value.second as String)}")
                    } else {
                        if (index == 0)
                            append("${value.first} = ${value.second}")
                        else
                            append(" and ${value.first} = ${value.second}")
                    }
                }
            else {
                append("true")
            }
        }.toString()
    }

    class Update {
        private var tableName: String? = null
        private var updateColumns: ArrayList<Pair<String, Any>> = ArrayList()
        private var conditions: ArrayList<Pair<String, Any>> = ArrayList()

        fun fromTable(table: String): Update {
            tableName = table
            return this
        }

        fun change(column: String, value: Any): Update {
            updateColumns.add(Pair(column, value))
            return this
        }

        fun withCondition(column: String, value: Any): Update {
            conditions.add(Pair(column, value))
            return this
        }

        fun build(): String = if (tableName == null || updateColumns.isEmpty()) {
            throw Exception("Update: 缺少参数")
        } else StringBuilder().apply {
            append("update $tableName set ")
            for ((index, value) in updateColumns.withIndex()) {
                if (value.second is String) {
                    if (index == 0)
                        append("${value.first} = ${toVarchar(value.second as String)}")
                    else
                        append(", ${value.first} = ${toVarchar(value.second as String)}")
                } else {
                    if (index == 0)
                        append("${value.first} = ${value.second}")
                    else
                        append(", ${value.first} = ${value.second}")
                }
            }
            if (conditions.isNotEmpty()) {
                append(" where ")
                for ((index, value) in conditions.withIndex()) {
                    if (value.second is String) {
                        if (index == 0)
                            append("${value.first} = ${toVarchar(value.second as String)}")
                        else
                            append(" and ${value.first} = ${toVarchar(value.second as String)}")
                    } else {
                        if (index == 0)
                            append("${value.first} = ${value.second}")
                        else
                            append(" and ${value.first} = ${value.second}")
                    }
                }
            }
        }.toString()
    }
}