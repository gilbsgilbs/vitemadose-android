package com.cvtracker.vmd.custom

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FilterSwitchItemDecoration(private val filterSwitchView: FilterSwitchView) :
    RecyclerView.ItemDecoration() {

    init {
        filterSwitchView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        filterSwitchView.layout(parent.left, 0, parent.right, filterSwitchView.measuredHeight);
        (0 until parent.childCount).find { parent.getChildAdapterPosition(parent.getChildAt(it)) == 0 }
            ?.let {
                c.save()
                val top = parent.getChildAt(it).top - filterSwitchView.measuredHeight
                c.translate(0f, top.toFloat())
                filterSwitchView.draw(c)
                c.restore()
            }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1
        if (parent.getChildAdapterPosition(view) < spanCount) {
            outRect.set(0, filterSwitchView.measuredHeight, 0, 0)
        } else {
            outRect.setEmpty()
        }
    }

    fun update() {
        filterSwitchView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
    }
}
