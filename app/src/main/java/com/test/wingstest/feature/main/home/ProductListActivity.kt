package com.test.wingstest.feature.main.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.wingstest.R
import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.utils.INTENT_DATA
import com.test.wingstest.core.utils.SUCCESS
import com.test.wingstest.core.utils.makeToast
import com.test.wingstest.databinding.ActivityProductListBinding
import com.test.wingstest.feature.component.adapter.ProductAdapter
import com.test.wingstest.feature.main.checkout.CheckoutProductsActivity
import com.test.wingstest.feature.main.detail.DetailProductActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListActivity : AppCompatActivity() {
    private var _binding: ActivityProductListBinding? = null
    private val binding get() = _binding!!
    private var adapter: ProductAdapter? = null
    private val viewModel: ProductListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductListBinding.inflate(layoutInflater)
        initController()
        setContentView(binding.root)
    }

    private fun initController() {
        viewModel.saveIntentData(intent.getIntExtra(INTENT_DATA , 0))
        setupAdapter()
        initListener()
        observer()
    }


    private fun setupAdapter() {
        adapter = ProductAdapter()
        binding.rvList.adapter = adapter
    }

    private fun initListener() {
        adapter?.setOnClickListener {
            viewModel.enabledRequest(it as ProductModel)
            viewModel.checkIfTransactionExist()
        }
        adapter?.setOnDetailListener { data->
            Intent(this, DetailProductActivity::class.java).also {
                it.putExtra(INTENT_DATA ,data as ProductModel)
                startActivity(it)
            }
        }
        binding.btnCheckOut.setOnClickListener {
            Intent(this, CheckoutProductsActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    adapter?.differ?.submitList(it.listProduct)
                    if (it.response != null) {
                        this@ProductListActivity.makeToast(if (it.response == SUCCESS)getString(R.string.success_add) else getString(R.string.success_checkout))
                        viewModel.clearResponse()
                    }
                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}