package com.akhnaton.atrSupply.ui.nav.home.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrSupply.data.model.review.ReviewModel
import com.akhnaton.atrSupply.databinding.LayoutReviewDetailsBinding

class ReviewDetailsAdapter(
    private val onClick: (product: ReviewModel, position: Int) -> Unit,
) :
    RecyclerView.Adapter<ReviewDetailsAdapter.ViewHolder>() {

    private var productsList = ArrayList<ReviewModel>()

    fun setData(product: List<ReviewModel>) {
        productsList = product as ArrayList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutReviewDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReviewModel, position: Int) {
            binding.txtName.text = item.USER_NAME
            binding.txtReview.text = item.COMMENT
            binding.ratingBar.rating = item.RATE.toFloat()

            itemView.setOnClickListener {
                onClick(item, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutReviewDetailsBinding.inflate(
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