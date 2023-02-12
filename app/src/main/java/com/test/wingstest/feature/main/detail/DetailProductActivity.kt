package com.test.wingstest.feature.main.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.wingstest.R
import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.utils.INTENT_DATA
import com.test.wingstest.core.utils.makeToast
import com.test.wingstest.core.utils.toCurrencyFormat
import com.test.wingstest.databinding.ActivityDetailProductBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailProductActivity : AppCompatActivity() {
    private var _binding: ActivityDetailProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailProductBinding.inflate(layoutInflater)
        initController()
        setContentView(binding.root)
    }

    @Suppress("DEPRECATION")
    private fun initController() {
        setupToolbar()
        if (Build.VERSION.SDK_INT >= 33) {
            viewModel.updateState(
                productModel = intent.getParcelableExtra(
                    INTENT_DATA,
                    ProductModel::class.java
                )
            )
        } else {
            viewModel.updateState(productModel = intent.getParcelableExtra(INTENT_DATA))
        }
        initListener()
        observer()
    }

    private fun observer() {
       lifecycleScope.launch {
           repeatOnLifecycle(Lifecycle.State.STARTED) {
               viewModel.apply {
                   state.collectLatest {
                       if (it.productModel != null) setDataToView(it.productModel)
                       if (it.response != null) {
                           this@DetailProductActivity.makeToast(getString(R.string.success_add))
                           viewModel.clearResponse()
                       }
                   }
               }
           }
       }
    }

    private fun setDataToView(data: ProductModel) {
        binding.apply {
            data.apply {
                tvProductName.text = productName
                tvPrice.text = getString(R.string.price_w_value, price.toString().toCurrencyFormat())
                tvDimension.text = data.dimension
                tvPriceUnit.text = data.currency

            }
        }
    }

    private fun initListener() {
        binding.apply {
            btnBuy.setOnClickListener {
                viewModel.checkIfTransactionExist()
            }
        }
    }
    private fun setupToolbar(){
        supportActionBar!!.title = getString(R.string.detail_product)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}