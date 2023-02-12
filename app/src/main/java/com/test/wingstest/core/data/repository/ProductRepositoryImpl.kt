package com.test.wingstest.core.data.repository

import android.util.Log
import com.test.wingstest.core.data.local.dao.DbDao
import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.data.model.TransactionDetail
import com.test.wingstest.core.data.model.TransactionHeader
import com.test.wingstest.core.domain.localRepo.ProductRepository
import com.test.wingstest.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: DbDao
) : ProductRepository {

    //    override suspend fun getAllUser(): Flow<MutableList<UserInformation>> = dao.getAllUser()
    override suspend fun getAllProduct(): Flow<MutableList<ProductModel>> = dao.getAllProduct()
    override suspend fun addData(data: MutableList<ProductModel>) {
        withContext(Dispatchers.IO) {
            dao.addDataProducts(data)
        }
    }

    override suspend fun addTransactionHeader(data: TransactionHeader) {
        withContext(Dispatchers.IO) {
            dao.addTransactionHeader(data)
        }
    }

    override suspend fun addTransactionDetail(data: TransactionDetail) {
        withContext(Dispatchers.IO) {
            dao.addTransactionDetail(data)
        }
    }

    override suspend fun getTransactionHeader(username: String): Flow<TransactionHeader?> =
        dao.getTransactionHeader(username)

    override suspend fun getAllTransactionDetail(): Flow<MutableList<TransactionDetail>>{
        return flow {
            emit(dao.getAllTransactionDetail())
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTransactionDetail(productCode: String): Flow<TransactionDetail?> =
        dao.getTransactionDetail(productCode)

    override suspend fun saveUpdateDetailTransaction(detail: TransactionDetail) {
        withContext(Dispatchers.IO) {
            try {
                dao.updateTransactionDetail(detail)
            } catch (e: Exception) {
                Log.e("TAG", "error: $e")
            }
        }
    }

    override suspend fun clearTransactionDetail(documentNumber: String) {
        withContext(Dispatchers.IO){
            dao.deleteTransactionDetail(documentNumber)
        }
    }

    override suspend fun clearTransactionHead(username: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            dao.deleteTransactionHeader(username)
            delay(1000)
            emit(Resource.Success(data = true))
        }.flowOn(Dispatchers.IO )
    }


    override suspend fun updateHeader(data: TransactionHeader) {
        withContext(Dispatchers.IO){ dao.updateTransactionHeader(data) }
    }

    override suspend fun clearTransaction(username: String, documentNumber: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            dao.deleteTransactionDetail(documentNumber)
            delay(200)
            dao.deleteTransactionHeader(username)
            delay(1000)
            emit(Resource.Success(data = true))
        }.flowOn(Dispatchers.IO )
    }

}

