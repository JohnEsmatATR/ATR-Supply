package com.akhnaton.atrapp.ui.nav.profile.order.history

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.StatusGroup

class OrderStatusTimelineHelper(
    private val container: LinearLayout
) {

    fun bind(
        statuses: List<StatusGroup>
    ) {

        container.removeAllViews()

        if (statuses.isEmpty()) {

            container.visibility = View.GONE

            return
        }

        container.visibility = View.VISIBLE

        statuses.forEachIndexed { index, status ->

            val statusView =
                LayoutInflater.from(container.context)
                    .inflate(
                        R.layout.item_order_status,
                        container,
                        false
                    )

            val imgStatus =
                statusView.findViewById<ImageView>(
                    R.id.imgStatus
                )

            val tvStatusLabel =
                statusView.findViewById<TextView>(
                    R.id.tvStatusLabel
                )

            val tvStatusDate =
                statusView.findViewById<TextView>(
                    R.id.tvStatusDate
                )

            val lineBefore =
                statusView.findViewById<View>(
                    R.id.lineBefore
                )

            val lineAfter =
                statusView.findViewById<View>(
                    R.id.lineAfter
                )

            /*
             * --------------------------------
             * TEXT
             * --------------------------------
             */

            tvStatusLabel.text =
                status.name.orEmpty()

            tvStatusDate.text =
                status.date.orEmpty()

            /*
             * --------------------------------
             * STATUS COLOR
             * --------------------------------
             */

            val color =
                parseColor(
                    status.color,
                    Color.LTGRAY
                )

            /*
             * --------------------------------
             * CIRCLE
             * --------------------------------
             */

            if (status.isCompleted == true) {

                imgStatus.background =
                    createCircleDrawable(
                        color
                    )

                imgStatus.setImageResource(
                    R.drawable.ic_checked
                )

                imgStatus.setColorFilter(
                    Color.WHITE
                )

                tvStatusLabel.setTextColor(
                    color
                )

            } else {

                imgStatus.background =
                    createCircleDrawable(
                        Color.WHITE,
                        Color.parseColor(
                            "#DDDDDD"
                        )
                    )

                imgStatus.setImageDrawable(
                    null
                )

                imgStatus.clearColorFilter()

                tvStatusLabel.setTextColor(
                    Color.parseColor(
                        "#C5C5C5"
                    )
                )
            }

            tvStatusDate.setTextColor(
                Color.parseColor(
                    "#999999"
                )
            )

            /*
             * --------------------------------
             * LEFT LINE
             * --------------------------------
             *
             * First status has no line before it.
             */

            if (index == 0) {

                lineBefore.visibility =
                    View.INVISIBLE

            } else {

                lineBefore.visibility =
                    View.VISIBLE

                val previousStatus =
                    statuses[index - 1]

                lineBefore.setBackgroundColor(
                    getLineColor(
                        previousStatus,
                        status
                    )
                )
            }

            /*
             * --------------------------------
             * RIGHT LINE
             * --------------------------------
             *
             * Last status has no line after it.
             */

            if (index == statuses.lastIndex) {

                lineAfter.visibility =
                    View.INVISIBLE

            } else {

                lineAfter.visibility =
                    View.VISIBLE

                val nextStatus =
                    statuses[index + 1]

                lineAfter.setBackgroundColor(
                    getLineColor(
                        status,
                        nextStatus
                    )
                )
            }

            /*
             * Add this status to the main container.
             */
            container.addView(
                statusView
            )
        }
    }

    private fun getLineColor(
        currentStatus: StatusGroup,
        nextStatus: StatusGroup
    ): Int {

        return if (
            currentStatus.isCompleted == true &&
            nextStatus.isCompleted == true
        ) {

            parseColor(
                currentStatus.color,
                Color.parseColor(
                    "#FF9500"
                )
            )

        } else {

            Color.parseColor(
                "#E5E5E5"
            )
        }
    }

    private fun createCircleDrawable(
        color: Int,
        strokeColor: Int? = null
    ): GradientDrawable {

        return GradientDrawable().apply {

            shape =
                GradientDrawable.OVAL

            setColor(color)

            strokeColor?.let {
                setStroke(
                    2,
                    it
                )
            }
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