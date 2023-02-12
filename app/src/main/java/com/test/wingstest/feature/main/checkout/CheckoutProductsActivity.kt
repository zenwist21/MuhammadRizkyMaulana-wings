package com.test.wingstest.feature.main.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.wingstest.R
import com.test.wingstest.core.utils.INTENT_DATA
import com.test.wingstest.core.utils.SUCCESS_CHECKOUT
import com.test.wingstest.core.utils.createAlertDialog
import com.test.wingstest.core.utils.toCurrencyFormat
import com.test.wingstest.databinding.ActivityCheckoutProductsBinding
import com.test.wingstest.feature.component.adapter.CheckoutAdapter
import com.test.wingstest.feature.main.home.ProductListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutProductsActivity : AppCompatActivity() {
    private var _binding: ActivityCheckoutProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CheckoutProductViewModel by viewModels()
    private var adapter: CheckoutAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutProductsBinding.inflate(layoutInflater)
        initController()
        setContentView(binding.root)
    }

    private fun initController() {
        setupToolbar()
        setupAdapter()
        initListener()
        observer()
    }

    private fun setupAdapter() {
        adapter = CheckoutAdapter()
        binding.rvList.adapter = adapter
    }

    private fun initListener() {
        binding.btnCheckOut.setOnClickListener {
            this.createAlertDialog {
                viewModel.completeTransaction()
            }
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                        showLoading(it.isLoading)
                        if (it.listProduct.isNotEmpty() && it.listTransaction.isNotEmpty()) {
                            showNoData(false)
                            adapter?.differ?.submitList(
                                viewModel.mapProductNameToDetail()
                            )
                            binding.tvTotal.text = if (it.transactionHeader?.total != null)getString(R.string.price_w_value,
                                it.transactionHeader.total.toString().toCurrencyFormat()) else ""
                        }else{
                            showNoData(true)
                        }
                        if (it.response != null) {
                            Intent(
                                this@CheckoutProductsActivity, ProductListActivity::class.java
                            ).also { intent ->
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                intent.putExtra(INTENT_DATA, SUCCESS_CHECKOUT)
                                startActivity(intent)
                                this@CheckoutProductsActivity.finish()
                            }
                            viewModel.clearResponse()
                        }
                    }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.iLoading.clMain.visibility = if (!state) View.GONE else View.VISIBLE
        binding.clMain.visibility = if (state) View.GONE else View.VISIBLE
    }
    private fun showNoData(state: Boolean) {
        binding.iNoData.llcError.visibility = if (!state) View.GONE else View.VISIBLE
        binding.clMain.visibility = if (state) View.GONE else View.VISIBLE
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
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}