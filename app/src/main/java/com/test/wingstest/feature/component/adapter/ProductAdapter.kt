package com.test.wingstest.feature.component.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.wingstest.R
import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.utils.toCurrencyFormat
import com.test.wingstest.databinding.ItemListProductBinding

class ProductAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onItemClick: ((Any?) -> Unit)? = null
    private var onDetailClick: ((Any?) -> Unit)? = null

    private val diffCallBack = object : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(
            oldItem: ProductModel,
            newItem: ProductModel
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ProductModel,
            newItem: ProductModel
        ): Boolean =
            oldItem == newItem

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    inner class Item(private val parent: ViewGroup, private val binding: ItemListProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductModel) {
            binding.apply {
                parent.context.let { ctx ->
                    /** set data to view **/
                    tvName.text = data.productName
                    tvDiscount.let {
                        it.visibility = if (data.discount > 0) View.VISIBLE else View.GONE
                        it.text = ctx.getString(R.string.price_w_value, data.price.toString().toCurrencyFormat())
                        it.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    }

                    tvPrice.text = ctx.getString(
                        R.string.price_w_value,
                        data.price?.minus(data.price.times(data.discount))?.toLong().toString().toCurrencyFormat()
                    )
                    btnBuy.setOnClickListener{
                        onItemClick?.invoke(data)
                    }
                    llcContent.setOnClickListener {
                        onDetailClick?.invoke(data)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Item(
            parent,
            ItemListProductBinding.inflate(
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
    fun setOnClickListener(listener: (Any?) -> Unit) {
        onItemClick = listener
    }
    fun setOnDetailListener(listener: (Any?) -> Unit){
        onDetailClick = listener
    }


}
