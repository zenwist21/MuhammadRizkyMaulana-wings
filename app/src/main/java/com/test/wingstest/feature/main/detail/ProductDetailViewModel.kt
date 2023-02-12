package com.test.wingstest.feature.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.wingstest.core.data.local.sessionManager.PreferencesManager
import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.data.model.TransactionDetail
import com.test.wingstest.core.data.model.TransactionHeader
import com.test.wingstest.core.domain.localRepo.ProductRepository
import com.test.wingstest.core.utils.SUCCESS
import com.test.wingstest.core.utils.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductUiState(
    val productModel: ProductModel? = null,
    val response: Int? = null,
    val transactionHeader: TransactionHeader? = null,
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepo: ProductRepository, private val session: PreferencesManager
) : ViewModel() {
    private var _state = MutableStateFlow(ProductUiState())
    val state get() = _state.asStateFlow()

    fun updateState(
        productModel: ProductModel?= null,
        response: Int?= null,
        transactionHeader: TransactionHeader?= null,
    ){
        _state.update {
            it.copy(
                productModel = productModel ?: it.productModel,
                response = response ?: it.response,
                transactionHeader = transactionHeader ?: it.transactionHeader,
            )
        }
    }

    private fun addToTransactionHeader() {
        viewModelScope.launch {
            productRepo.addTransactionHeader(
                TransactionHeader(
                    documentCode = "TRX",
                    documentNumber = "001",
                    total = 0,
                    date = getCurrentTime(),
                    user = session.getUserKey()?.user ?: ""
                )
            )
            checkIfTransactionExist()
        }
    }

    private fun addDataToDetail(data: ProductModel) {
        viewModelScope.launch {
            val tempDetail = TransactionDetail(
                documentCode = state.value.transactionHeader?.documentCode,
                documentNumber = state.value.transactionHeader?.documentNumber,
                price = data.price ?: 0L,
                productCode = data.productCode,
                quantity = 1,
                unit = data.unit,
                subTotal =data.price?.minus(data.discount.times(data.price).toLong()),
                currency = data.currency
            )
            productRepo.addTransactionDetail(tempDetail)
            delay(200)
            _state.update {
                it.copy(response = SUCCESS)
            }
        }
    }
    private fun saveUpdateData(data: ProductModel, transactionDetail: TransactionDetail) {
        viewModelScope.launch {
            val tempDetail = transactionDetail.copy(
                quantity = transactionDetail.quantity?.plus(1),
                subTotal = data.price?.minus(data.discount.times(data.price).toLong())?.plus(transactionDetail.subTotal ?: 0)
            )
            productRepo.saveUpdateDetailTransaction(tempDetail)
            delay(200)
            _state.update {
                it.copy(response = SUCCESS)
            }
        }
    }

    private fun checkIfDetailExist(data: ProductModel) {
        viewModelScope.launch {
            productRepo.getTransactionDetail(data.productCode ?: "")  .take(1).collect{
                if (it == null) {
                    addDataToDetail(data)
                } else {
                    saveUpdateData(data, it)
                }
            }
        }
    }

    fun clearResponse() = _state.update {
        it.copy(response = null)
    }

    fun checkIfTransactionExist() {
        viewModelScope.launch {
            productRepo.getTransactionHeader(session.getUserKey()?.user ?: "")
                .take(1)
                .collect { res ->
                if (res == null) {
                    addToTransactionHeader()
                } else {
                    _state.update {
                        it.copy(transactionHeader = res)
                    }
                    checkIfDetailExist(state.value.productModel ?: ProductModel())
                }
            }
        }
    }

}