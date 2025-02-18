package com.akhnaton.atrapp.ui.nav.home.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.NotificationModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.databinding.LayoutNotificationsBinding
import com.akhnaton.atrapp.databinding.LayoutProductBinding
import com.akhnaton.atrapp.databinding.LayoutReviewBinding
import com.akhnaton.atrapp.databinding.LayoutReviewDetailsBinding
import com.akhnaton.atrapp.shared.Common

class NotificationAdapter(
    private val onClick: (product: NotificationModel, position: Int) -> Unit,
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private var productsList = ArrayList<NotificationModel>()

    fun setData(product: List<NotificationModel>) {
        productsList = product as ArrayList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationModel, position: Int) {
            binding.txtTitle.text = item.title
            binding.txtDescription.text = item.description
            binding.txtDateTime.text = item.dateTime
            binding.img.load(item.img)

            itemView.setOnClickListener {
                onClick(item, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutNotificationsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        productsList?.get(position)?.let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

}