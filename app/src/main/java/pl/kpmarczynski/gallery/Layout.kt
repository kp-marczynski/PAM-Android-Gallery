package pl.kpmarczynski.gallery

enum class Layout(val value: Int, val viewName: String) {
    GRID(R.layout.grid_layout, "grid"),
    DETAILS(R.layout.details_layout, "details");

    companion object {
        fun createFromInt(layoutId: Int): Layout {
            return when (layoutId) {
                GRID.value -> Layout.GRID
                DETAILS.value -> Layout.DETAILS
                else -> Layout.GRID
            }
        }
    }

}