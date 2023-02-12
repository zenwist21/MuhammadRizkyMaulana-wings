package com.test.wingstest.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.wingstest.R
import com.test.wingstest.core.data.local.sessionManager.PreferencesManager
import com.test.wingstest.core.data.model.UserInformation
import com.test.wingstest.core.domain.localRepo.LoginRepository
import com.test.wingstest.core.utils.Resource
import com.test.wingstest.core.utils.SUCCESS
import com.test.wingstest.core.utils.dummyUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val userName: String? = null, val password: String? = null, val response: Int? = 0,
    val isLoading: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: LoginRepository,
    private val prefManager:PreferencesManager
) : ViewModel() {

    private var _state = MutableStateFlow(LoginUiState())
    val state get() = _state.asStateFlow()

    init {
        checkIfUserExist()
    }

    fun updateState(
        userName: String? = null,
        password: String? = null,
        response: Int? = null,
        isLoading: Boolean? = false
    ) {
        _state.update {
            it.copy(
                userName = userName ?: it.userName,
                password = password ?: it.password,
                response = response ?: it.response,
                isLoading = isLoading ?: it.isLoading
            )
        }
    }

    fun doLogin() {
        viewModelScope.launch {
            repo.login(state.value.userName ?: "", state.value.password ?: "").onEach {
                when (it) {
                    is Resource.Loading -> {
                        updateState(isLoading = true)
                    }
                    is Resource.Success -> {
                        updateState(isLoading = false, response = if ( it.data == null) R.string.wrong_credentials else SUCCESS)
                        if (it.data != null) prefManager.saveUserKey(it.data)
                    }
                }
            }.launchIn(this)
        }
    }

    private fun checkIfUserExist() {
        viewModelScope.launch {
            repo.getAllUser().onEach {
                if (!it.contains(dummyUser)) {
                    repo.addUser(dummyUser)
                }
            }.launchIn(this)
        }
    }


}