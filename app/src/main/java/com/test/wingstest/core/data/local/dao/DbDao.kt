package com.test.wingstest.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.data.model.TransactionDetail
import com.test.wingstest.core.data.model.TransactionHeader
import com.test.wingstest.core.data.model.UserInformation
import kotlinx.coroutines.flow.Flow

@Dao
interface DbDao {

    @Query("SELECT * from tbLogin WHERE user LIKE :username and password LIKE :password ")
    fun getUserLogin(username: String, password: String): UserInformation

    @Query("SELECT * from tbLogin")
    fun getAllUser(): Flow<MutableList<UserInformation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUserInformation(data: UserInformation)

    /* Product */
    @Query("SELECT * from tbProduct")
    fun getAllProduct(): Flow<MutableList<ProductModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDataProducts(data: MutableList<ProductModel>)

    @Insert
    fun addTransactionHeader(data: TransactionHeader)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransactionDetail(data: TransactionDetail)

    @Query("SELECT * from tbTransactionHead WHERE user LIKE :username")
    fun getTransactionHeader(username: String) : Flow<TransactionHeader?>

    @Query("SELECT * from tbTransactionDetail WHERE productCode LIKE :productCode")
    fun getTransactionDetail(productCode:String): Flow<TransactionDetail?>

    @Query("SELECT * from tbTransactionDetail")
    fun getAllTransactionDetail() : MutableList<TransactionDetail>

    @Update
    fun updateTransactionDetail(data: TransactionDetail)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTransactionHeader(data: TransactionHeader)

    @Query("DELETE FROM tbTransactionDetail WHERE documentNumber =:documentNumber")
    fun deleteTransactionDetail(documentNumber: String)

    @Query("DELETE FROM tbTransactionHead WHERE user =:username")
    fun deleteTransactionHeader(username: String)


}