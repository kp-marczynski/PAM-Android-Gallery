package pl.kpmarczynski.gallery

import android.support.v7.widget.RecyclerView
import android.widget.AbsListView

class GridOnScrollListener(private val rownum: Int) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(view: RecyclerView, scrollState: Int) {
        super.onScrollStateChanged(view, scrollState)

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            val offset = Math.abs(view.getChildAt(0).top)
            val height = view.getChildAt(0).height

            val threshold = 2
            if (offset < (height / threshold)) {
                scrollTo(view, 0)
            } else if (offset > (height * (threshold - 1) / threshold)) {
                scrollTo(view, rownum)
            }
        }

    }

    private fun scrollTo(view: RecyclerView, position: Int) {
        view.smoothScrollBy(0, view.getChildAt(position).top)
    }
}
