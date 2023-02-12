package com.test.wingstest.core.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.wingstest.core.utils.tbLogin
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = tbLogin)
data class UserInformation(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 1,
    @ColumnInfo(name = "user")
    val user: String? = null,
    @ColumnInfo(name = "password")
    val password: String? = null,
    val loginAt:String = "",
    val duration: Int = 0
) : Parcelable
