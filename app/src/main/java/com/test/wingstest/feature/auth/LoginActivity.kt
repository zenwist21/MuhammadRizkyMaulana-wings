package com.test.wingstest.feature.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.wingstest.R
import com.test.wingstest.core.utils.SUCCESS
import com.test.wingstest.databinding.ActivityLoginBinding
import com.test.wingstest.feature.main.home.ProductListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        initController()
        setContentView(binding.root)
    }

    private fun initController() {
        initListener()
        observer()
    }

    private fun initListener() {
        binding.edtPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.edtPass.error = getString(R.string.must_be_filled)
                }
                viewModel.updateState(password = s.toString())
            }

        })
        binding.edtUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.edtUserName.error = getString(R.string.must_be_filled)
                }
                viewModel.updateState(userName = s.toString())
            }

        })
        binding.btnLogin.setOnClickListener {
            viewModel.doLogin()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.iLoading.clMain.visibility = if (!state) View.GONE else View.VISIBLE
        binding.llcMain.visibility = if (state) View.GONE else View.VISIBLE
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    Log.e("TAG", "observer: $it")
                    binding.btnLogin.isEnabled = !it.userName.isNullOrEmpty() && !it.password.isNullOrEmpty()
                    showLoading(it.isLoading)
                    if (it.response == SUCCESS) {
                        Intent(this@LoginActivity, ProductListActivity::class.java).also { intent ->
                            startActivity(intent)
                            finish()
                        }
                    }else{
                        binding.tvError.let { tv->
                            tv.visibility = if (it.response != 0) View.VISIBLE else View.GONE
                            tv.text = when(it.response){
                                0 -> {
                                    ""
                                }
                                SUCCESS ->{
                                    "$SUCCESS"
                                }
                                else ->{
                                    getString(it.response!!)
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}