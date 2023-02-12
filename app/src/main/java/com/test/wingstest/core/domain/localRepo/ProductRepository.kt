package com.test.wingstest.core.domain.localRepo

import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.data.model.TransactionDetail
import com.test.wingstest.core.data.model.TransactionHeader
import com.test.wingstest.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProduct(): Flow<MutableList<ProductModel>>
    suspend fun addData(data: MutableList<ProductModel>)
    suspend fun addTransactionHeader(data: TransactionHeader)
    suspend fun addTransactionDetail(data: TransactionDetail)
    suspend fun getTransactionHeader(username: String): Flow<TransactionHeader?>
    suspend fun getAllTransactionDetail(): Flow<MutableList<TransactionDetail>>
    suspend fun getTransactionDetail(productCode: String): Flow<TransactionDetail?>
    suspend fun saveUpdateDetailTransaction(detail: TransactionDetail)
    suspend fun clearTransactionDetail(documentNumber: String)
    suspend fun clearTransactionHead(username: String) : Flow<Resource<Boolean>>
    suspend fun updateHeader(data: TransactionHeader)
    suspend fun clearTransaction(username: String, documentNumber: String): Flow<Resource<Boolean>>
}