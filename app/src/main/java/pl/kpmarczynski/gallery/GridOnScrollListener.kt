package pl.kpmarczynski.gallery

import android.widget.AbsListView

class GridOnScrollListener : AbsListView.OnScrollListener {
    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view != null) {
                val offset = Math.abs(view.getChildAt(0).top)
                val height = view.getChildAt(0).height

                val threshold = 2.5
                if (offset < (height / threshold)) {
                    view.setSelection(view.firstVisiblePosition)
                } else if (offset > (height * (threshold - 1) / threshold)) {
                    view.setSelection(view.firstVisiblePosition + 2)
                }
            }
        }
    }

    override fun onScroll(
        view: AbsListView?,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ) {

    }
}
