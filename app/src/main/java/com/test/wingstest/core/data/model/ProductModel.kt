package com.test.wingstest.core.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.wingstest.core.utils.tbProduct
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = tbProduct)
data class ProductModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "productCode")
    val productCode: String? = null,
    @ColumnInfo(name = "productName")
    val productName: String? = null,
    @ColumnInfo(name = "price")
    val price: Long? = 0,
    @ColumnInfo(name = "discount")
    val discount: Double = 0.0,
    @ColumnInfo(name = "dimension")
    val dimension: String? = null,
    @ColumnInfo(name = "currency")
    val currency: String? = null,
    @ColumnInfo(name = "unit")
    val unit: String? = null,
) : Parcelable