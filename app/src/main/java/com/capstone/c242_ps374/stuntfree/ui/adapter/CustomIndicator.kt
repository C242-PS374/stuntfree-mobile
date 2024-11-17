package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.capstone.c242_ps374.stuntfree.R

class CustomIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var indicators: List<View>

    init {
        LayoutInflater.from(context).inflate(R.layout.activity_get_started, this, true)
        indicators = listOf(
            findViewById(R.id.indicator1),
            findViewById(R.id.indicator2),
            findViewById(R.id.indicator3)
        )
    }

    fun setActivePosition(position: Int) {
        indicators.forEachIndexed { index, view ->
            view.isSelected = index == position
            view.animate()
                .scaleX(if (index == position) 1.2f else 1.0f)
                .scaleY(if (index == position) 1.2f else 1.0f)
                .setDuration(200)
                .start()
        }
    }
}