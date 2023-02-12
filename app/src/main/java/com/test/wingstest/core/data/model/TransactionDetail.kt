package com.test.wingstest.core.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.wingstest.core.utils.tbProduct
import com.test.wingstest.core.utils.tbTransactionDetail
import com.test.wingstest.core.utils.tbTransactionHead
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = tbTransactionDetail)
data class TransactionDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "documentCode")
    val documentCode: String? = null,
    @ColumnInfo(name = "documentNumber")
    val documentNumber: String? = null,
    @ColumnInfo(name = "productCode")
    val productCode: String? = null,
    @ColumnInfo(name = "price")
    val price: Long? = 0,
    @ColumnInfo(name = "quantity")
    val quantity: Long? = 0,
    @ColumnInfo(name = "unit")
    val unit: String? = null,
    @ColumnInfo(name = "subTotal")
    val subTotal: Long? = null,
    @ColumnInfo(name = "currency")
    val currency: String? = null,
    val productName:String? = null

) : Parcelable