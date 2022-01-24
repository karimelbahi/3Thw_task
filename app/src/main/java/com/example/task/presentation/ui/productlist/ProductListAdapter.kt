package com.example.task.presentation.ui.productlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.app.MyApplication
import com.example.task.data.database.model.Product
import com.example.task.databinding.ProductListItemBinding
import com.example.task.presentation.utils.convertLongToStrDate
import com.example.task.presentation.utils.currentTime
import com.example.task.presentation.utils.diffDaysBetweenTwoTimes

class ProductListAdapter :
    ListAdapter<Product, ProductListAdapter.ViewHolder>(CountryListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {

                val leftDays = diffDaysBetweenTwoTimes(product.expiredDate, currentTime())
                productsNameTv.text = product.name
                productsCodeTv.text = product.code
                productTypeTv.text = product.type
                expireDateTv.text = product.expiredDate.convertLongToStrDate()
                remainingDaysTv.text =
                    MyApplication.applicationContext().getString(R.string.left_days_num, leftDays)
            }
        }
    }


    class CountryListDiffCallback : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

}