package com.test.wingstest.feature.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.wingstest.core.data.local.sessionManager.PreferencesManager
import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.data.model.TransactionDetail
import com.test.wingstest.core.data.model.TransactionHeader
import com.test.wingstest.core.domain.localRepo.ProductRepository
import com.test.wingstest.core.utils.SUCCESS
import com.test.wingstest.core.utils.dummyProduct
import com.test.wingstest.core.utils.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductUiState(
    val listProduct: List<ProductModel> = emptyList(),
    val listTransaction: List<TransactionDetail> = emptyList(),
    val transactionDetail: TransactionDetail = TransactionDetail(),
    val response: Int? = null,
    val transactionHeader: TransactionHeader? = null,
    val stopRequest: Boolean = true,
    val productModel: ProductModel? = null
)

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepo: ProductRepository, private val session: PreferencesManager
) : ViewModel() {
    private var _state = MutableStateFlow(ProductUiState())
    val state get() = _state.asStateFlow()

    init {
        getAllProduct()
    }

    fun enabledRequest(data: ProductModel) =
        _state.update { it.copy(stopRequest = false, productModel = data) }

    fun saveIntentData(data: Int) {
        if (data != 0) {
            _state.update {
                it.copy(
                    response = data
                )
            }
        }
    }


    private fun storeDummyProduct() {
        viewModelScope.launch {
            productRepo.addData(dummyProduct())
            getAllProduct()
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
                subTotal = data.price?.minus(data.discount.times(data.price).toLong()),
                currency = data.currency
            )
            productRepo.addTransactionDetail(tempDetail)
            delay(200)
            getAllTransaction()
        }
    }

    private fun saveUpdateData(data: ProductModel, transactionDetail: TransactionDetail) {
        viewModelScope.launch {
            val tempDetail = transactionDetail.copy(
                quantity = transactionDetail.quantity?.plus(1),
                subTotal = data.price?.minus(data.discount.times(data.price).toLong())
                    ?.plus(transactionDetail.subTotal ?: 0)
            )
            productRepo.saveUpdateDetailTransaction(tempDetail)
            delay(200)
            getAllTransaction()
        }
    }

    private fun checkIfDetailExist(data: ProductModel) {
        viewModelScope.launch {
                productRepo.getTransactionDetail(data.productCode ?: "")
                    .take(1).collect {
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

    private fun updateHeader(
        transactionHeader: TransactionHeader,
        detailData: List<TransactionDetail>
    ) {
        viewModelScope.launch {
            val data = transactionHeader.copy(
                total = detailData.sumOf { it.subTotal ?: 0 }
            )
            productRepo.updateHeader(data)
            cancel()
        }
    }

    fun checkIfTransactionExist() {
        viewModelScope.launch {
            productRepo.getTransactionHeader(session.getUserKey()?.user ?: "").take(1).collect { res ->
                if (res == null) {
                    addToTransactionHeader()
                } else {
                    _state.update {
                        it.copy(transactionHeader = res)
                    }
                    if (!state.value.stopRequest){
                        if (state.value.productModel != null) {
                            checkIfDetailExist(state.value.productModel!!)
                        }
                    }

                }
            }
        }
    }

    private fun getAllTransaction() {
        viewModelScope.launch {
            productRepo.getAllTransactionDetail()
                .take(1)
                .distinctUntilChanged { old, new -> old!=new  }.collect {
                    val detailData =
                        it.filter { data -> state.value.transactionHeader?.documentNumber == data.documentNumber }
                    updateHeader(state.value.transactionHeader ?: TransactionHeader(), detailData)
                    _state.update {state->
                        state.copy(response = SUCCESS, stopRequest = true)
                    }
                }
        }
    }

    private fun getAllProduct() {
        viewModelScope.launch {
            productRepo.getAllProduct().collect { data ->
                if (data.isEmpty()) {
                    storeDummyProduct()
                } else {
                    _state.update {
                        it.copy(listProduct = data.ifEmpty { emptyList() })
                    }
                }
            }
        }
    }

}