package com.test.wingstest.feature.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.wingstest.R
import com.test.wingstest.core.data.model.TransactionDetail
import com.test.wingstest.core.utils.toCurrencyFormat
import com.test.wingstest.databinding.ItemListProductCoBinding

class CheckoutAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<TransactionDetail>() {
        override fun areItemsTheSame(
            oldItem: TransactionDetail,
            newItem: TransactionDetail
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: TransactionDetail,
            newItem: TransactionDetail
        ): Boolean =
            oldItem == newItem

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    inner class Item(private val parent: ViewGroup, private val binding: ItemListProductCoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TransactionDetail) {
            binding.apply {
                parent.context.let { ctx ->
                    /** set data to view **/
                    tvName.text = data.productName
                    tvQuantity.text = data.quantity.toString()
                    tvTotal.text = ctx.getString(
                        R.string.price_w_value,
                        data.price.toString().toCurrencyFormat()
                    )

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Item(
            parent,
            ItemListProductCoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is Item -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size



}
