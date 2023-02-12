package com.test.wingstest.feature.main.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.wingstest.core.data.local.sessionManager.PreferencesManager
import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.data.model.TransactionDetail
import com.test.wingstest.core.data.model.TransactionHeader
import com.test.wingstest.core.domain.localRepo.ProductRepository
import com.test.wingstest.core.utils.Resource
import com.test.wingstest.core.utils.SUCCESS_CHECKOUT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductUiState(
    val response: Int? = null,
    val transactionHeader: TransactionHeader? = null,
    val listProduct: List<ProductModel> = emptyList(),
    val listTransaction: List<TransactionDetail> = emptyList(),
    val isLoading:Boolean = false
)

@HiltViewModel
class CheckoutProductViewModel @Inject constructor(
    private val productRepo: ProductRepository, private val session: PreferencesManager
) : ViewModel() {
    private var _state = MutableStateFlow(ProductUiState())
    val state get() = _state.asStateFlow()

    init {
        execute()
    }

    private fun updateState(
        response: Int? = null,
        transactionHeader: TransactionHeader? = null,
        listTransaction: List<TransactionDetail>? = null,
        listProduct: List<ProductModel>? = null,
        isLoading: Boolean? = null
    ) {
        _state.update {
            it.copy(
                response = response ?: it.response,
                transactionHeader = transactionHeader ?: it.transactionHeader,
                listTransaction = listTransaction ?: it.listTransaction,
                listProduct = listProduct ?: it.listProduct,
                isLoading = isLoading ?:it.isLoading
            )
        }
    }

    fun mapProductNameToDetail(): List<TransactionDetail> {
        val list = mutableListOf<TransactionDetail>()
        list.addAll(
            state.value.listTransaction.map {
                it.copy(
                    productName = state.value.listProduct.find { productModel -> it.productCode == productModel.productCode }?.productName
                        ?: ""
                )
            }
        )
        return list
    }

    private fun getAllProduct() {
        viewModelScope.launch {
            productRepo.getAllProduct().distinctUntilChanged { old, new -> old.size != new.size }
                .onEach {
                    updateState(listProduct = it)
                }.launchIn(this)
        }
    }

    private fun getAllTransaction() {
        viewModelScope.launch {
            productRepo.getAllTransactionDetail()
                .distinctUntilChanged { old, new -> old.size != new.size }.onEach {
                    updateState(listTransaction = it)
                }.launchIn(this)
        }
    }

    private fun getHeader() {
        viewModelScope.launch {
            productRepo.getTransactionHeader(session.getUserKey()?.user ?: "")
                .distinctUntilChanged { old, new -> old != new && new != null }.collectLatest {
                    updateState(transactionHeader = it)
                }
        }
    }

      fun completeTransaction() {
        viewModelScope.launch {
            productRepo.clearTransaction(state.value.transactionHeader?.user ?: "", state.value.transactionHeader?.documentNumber?:"").onEach {
                when(it){
                    is Resource.Loading -> {
                        updateState(isLoading = true)
                    }
                    is Resource.Success -> {
                        updateState(response = SUCCESS_CHECKOUT)
                    }
                }
            }.launchIn(this)
        }
    }


    fun clearResponse(){
        updateState(response = null)
    }

    private fun execute() = viewModelScope.launch(Dispatchers.IO) {
        val header = async { getHeader() }
        val products = async { getAllProduct() }
        val transactions = async { getAllTransaction() }
        header.await()
        products.await()
        transactions.await()
    }


}