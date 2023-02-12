package com.test.wingstest.core.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.test.wingstest.core.utils.tbProduct
import com.test.wingstest.core.utils.tbTransactionHead
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = tbTransactionHead, indices = [
    Index(value = ["user"], unique = true)
])
data class TransactionHeader(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "documentCode")
    val documentCode: String? = null,
    @ColumnInfo(name = "documentNumber")
    val documentNumber: String = "001",
    @ColumnInfo(name = "user")
    val user: String? = null,
    @ColumnInfo(name = "total")
    val total: Long = 0,
    @ColumnInfo(name = "date")
    val date: String? = null,

) : Parcelable