package com.akhnaton.atrapp.ui.nav.profile.order.history

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrapp.databinding.LayoutOrderBinding

class OrderHistoryAdapter :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    private lateinit var listener: OnProductClickListener

    private var mList = mutableListOf<OrderHistoryModel>()

    fun setData(
        order: List<OrderHistoryModel>,
        listener: OnProductClickListener
    ) {
        mList = order.toMutableList()
        this.listener = listener

        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: LayoutOrderBinding,
        private val listener: OnProductClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val statusTimelineHelper =
            OrderStatusTimelineHelper(
                binding.statusContainer
            )

        fun bind(item: OrderHistoryModel) {

            binding.data = item

            binding.tvOrderNo.text =
                item.ORDER_ID.orEmpty()

            // --------------------------------
            // STATUS TIMELINE
            // --------------------------------

            val steps = item.statusGroup.orEmpty()

            statusTimelineHelper.bind(steps)

            // --------------------------------
            // STATUS BADGE
            // --------------------------------

            bindStatusBadge(steps)

            // --------------------------------
            // VIEW ORDER BUTTON
            // --------------------------------

            binding.btnViewOrder.apply {

                isEnabled = true
                isClickable = true
                isFocusable = true

                setOnClickListener {

                    Log.d(
                        "VIEW_ORDER_DEBUG",
                        "BUTTON CLICKED: $item"
                    )

                    listener.onProductClick(item)
                }
            }
        }

        private fun bindStatusBadge(
            steps: List<com.akhnaton.atrapp.data.model.orderHistory.StatusGroup>
        ) {

            val currentStep =
                steps
                    .filter {
                        it.isCompleted == true
                    }
                    .lastOrNull()
                    ?: steps.firstOrNull()

            if (currentStep == null) {

                binding.tvBadgeStatus.visibility =
                    View.GONE

                return
            }

            binding.tvBadgeStatus.visibility =
                View.VISIBLE

            val color = parseColor(
                currentStep.color,
                Color.parseColor("#FF9500")
            )

            binding.tvBadgeStatus.text =
                "• ${currentStep.name.orEmpty()}"

            binding.tvBadgeStatus.setTextColor(
                color
            )

            binding.tvBadgeStatus.background =
                createRoundedBackground(
                    color = color,
                    cornerRadius = 30f,
                    alpha = 35
                )
        }

        private fun createRoundedBackground(
            color: Int,
            cornerRadius: Float,
            alpha: Int
        ): GradientDrawable {

            val backgroundColor = Color.argb(
                alpha,
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )

            return GradientDrawable().apply {

                shape =
                    GradientDrawable.RECTANGLE

                setColor(backgroundColor)

                this.cornerRadius =
                    cornerRadius
            }
        }

        private fun parseColor(
            color: String?,
            defaultColor: Int
        ): Int {

            return try {

                Color.parseColor(
                    color ?: ""
                )

            } catch (e: Exception) {

                defaultColor
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            LayoutOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ViewHolder(
            binding,
            listener
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        holder.bind(
            mList[position]
        )
    }

    override fun getItemCount(): Int {

        return mList.size
    }

    interface OnProductClickListener {

        fun onProductClick(
            data: OrderHistoryModel
        )
    }
}