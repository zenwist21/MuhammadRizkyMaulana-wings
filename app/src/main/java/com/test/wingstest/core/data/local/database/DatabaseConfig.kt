package com.test.wingstest.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.wingstest.core.data.local.dao.DbDao
import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.data.model.TransactionDetail
import com.test.wingstest.core.data.model.TransactionHeader
import com.test.wingstest.core.data.model.UserInformation
import com.test.wingstest.core.utils.Converter

@Database(
    entities = [UserInformation::class, ProductModel::class, TransactionHeader::class, TransactionDetail::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class DatabaseConfig : RoomDatabase() {
    abstract fun configDao(): DbDao
}