package com.akhnaton.atrapp.ui.nav.profile.order.history

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrapp.data.model.orderHistory.StatusGroup
import com.akhnaton.atrapp.databinding.LayoutOrderBinding

class OrderHistoryAdapter : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

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

        fun bind(item: OrderHistoryModel) {
            binding.data = item
            val steps = item.statusGroup.orEmpty()
            binding.tvOrderNo.text = item.ORDER_ID.orEmpty()
            bindStatusBadge(steps)

            bindStep(
                binding.imgStep1,
                binding.tvStep1Label,
                binding.tvStep1Date,
                steps.getOrNull(0)
            )

            bindStep(
                binding.imgStep2,
                binding.tvStep2Label,
                binding.tvStep2Date,
                steps.getOrNull(1)
            )

            bindStep(
                binding.imgStep3,
                binding.tvStep3Label,
                binding.tvStep3Date,
                steps.getOrNull(2)
            )

            bindLine(binding.lineStart, steps.getOrNull(0))
            bindLine(binding.line1, steps.getOrNull(0))
            bindLine(binding.line2, steps.getOrNull(1))
            bindLine(binding.lineEnd, steps.getOrNull(2))

            binding.btnViewOrder.apply {
                isEnabled = true
                isClickable = true
                isFocusable = true

                setOnClickListener {
                    Log.d(
                        "VIEW_ORDER_DEBUG",
                        "BUTTON CLICKED: ${item}"
                    )
                    listener.onProductClick(item)
                }
            }
        }

        private fun bindStep(
            imageView: ImageView,
            labelView: TextView,
            dateView: TextView,
            step: StatusGroup?
        ) {
            if (step == null) {
                imageView.visibility = View.INVISIBLE
                labelView.text = ""
                dateView.text = ""
                return
            }

            imageView.visibility = View.VISIBLE
            labelView.text = step.name.orEmpty()
            dateView.text = step.date.orEmpty()

            val color = parseColor(step.color, Color.LTGRAY)

            if (step.isCompleted == true) {
                imageView.background = createCircleDrawable(color)

                imageView.setImageResource(R.drawable.ic_checked)
                imageView.setColorFilter(Color.WHITE)
                labelView.setTextColor(color)

            } else {
                imageView.background = createCircleDrawable(
                    Color.WHITE,
                    Color.parseColor("#DDDDDD")
                )
                imageView.setImageDrawable(null)
                imageView.clearColorFilter()
                labelView.setTextColor(Color.parseColor("#C5C5C5"))
            }
        }

        private fun bindLine(line: View, step: StatusGroup?) {
            if (step?.isCompleted == true) {
                line.setBackgroundColor(
                    parseColor(step.color, Color.parseColor("#FF9500"))
                )
            } else {
                line.setBackgroundColor(Color.parseColor("#E5E5E5"))
            }
        }

        private fun bindStatusBadge(steps: List<StatusGroup>) {
            val currentStep = steps.filter { it.isCompleted == true }.lastOrNull()
                ?: steps.firstOrNull()

            if (currentStep == null) {
                binding.tvBadgeStatus.visibility = View.GONE
                return
            }

            binding.tvBadgeStatus.visibility = View.VISIBLE

            val color = parseColor(currentStep.color, Color.parseColor("#FF9500"))

            binding.tvBadgeStatus.text = "• ${currentStep.name.orEmpty()}"
            binding.tvBadgeStatus.setTextColor(color)

            binding.tvBadgeStatus.background = createRoundedBackground(
                color = color,
                cornerRadius = 30f,
                alpha = 35
            )
        }

        private fun createCircleDrawable(
            color: Int,
            strokeColor: Int? = null
        ): GradientDrawable {

            return GradientDrawable().apply {

                shape = GradientDrawable.OVAL

                setColor(color)

                strokeColor?.let {
                    setStroke(2, it)
                }
            }
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

                shape = GradientDrawable.RECTANGLE

                setColor(backgroundColor)

                this.cornerRadius = cornerRadius
            }
        }

        private fun parseColor(
            color: String?,
            defaultColor: Int
        ): Int {

            return try {
                Color.parseColor(color ?: "")
            } catch (e: Exception) {
                defaultColor
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = LayoutOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnProductClickListener {
        fun onProductClick(data: OrderHistoryModel)
    }
}