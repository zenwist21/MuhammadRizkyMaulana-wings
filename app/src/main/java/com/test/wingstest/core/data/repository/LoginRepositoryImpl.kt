package com.test.wingstest.core.data.repository

import android.util.Log
import com.test.wingstest.core.data.local.dao.DbDao
import com.test.wingstest.core.data.model.UserInformation
import com.test.wingstest.core.domain.localRepo.LoginRepository
import com.test.wingstest.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val dao: DbDao
) : LoginRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Flow<Resource<UserInformation>> {
        return flow {
            emit(Resource.Loading())
            delay(1000)
            emit(Resource.Success(dao.getUserLogin(username, password) ))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllUser(): Flow<MutableList<UserInformation>> = dao.getAllUser()

    override suspend fun addUser(data: UserInformation) {
        withContext(Dispatchers.IO) {
            dao.addUserInformation(data)
        }
    }
}

