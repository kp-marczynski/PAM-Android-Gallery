package pl.kpmarczynski.gallery.layout

import pl.kpmarczynski.gallery.R

enum class Layout(val value: Int, val viewName: String) {
    GRID(R.layout.grid_layout, "grid"),
    DETAILS(R.layout.details_layout, "details");

    companion object {
        fun createFromInt(layoutId: Int): Layout {
            return when (layoutId) {
                GRID.value -> GRID
                DETAILS.value -> DETAILS
                else -> GRID
            }
        }

        fun switchLayout(layout: Layout): Layout {
            return when (layout) {
                GRID -> DETAILS
                DETAILS -> GRID
            }
        }
    }

}