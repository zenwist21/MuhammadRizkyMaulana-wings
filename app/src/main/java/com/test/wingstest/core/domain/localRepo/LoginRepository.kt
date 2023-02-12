package com.test.wingstest.core.domain.localRepo

import com.test.wingstest.core.data.model.UserInformation
import com.test.wingstest.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(username: String, password: String): Flow<Resource<UserInformation>>
    suspend fun getAllUser(): Flow<MutableList<UserInformation>>
    suspend fun addUser(data: UserInformation)
}