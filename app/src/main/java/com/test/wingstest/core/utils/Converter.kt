package com.test.wingstest.core.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.test.wingstest.core.data.model.ProductModel

class Converter {
    @TypeConverter
    fun listToJson(value: List<ProductModel>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<ProductModel>::class.java).toList()
}