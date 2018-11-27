package pl.kpmarczynski.gallery.layout

import pl.kpmarczynski.gallery.R

enum class Layout(val value: Int, val viewName: String) {
    GRID(R.layout.grid_layout, "grid"),
    DETAILS(R.layout.details_layout, "details"),
    PUZZLE(R.layout.puzzle_layout, "puzzle");

    companion object {
        fun createFromInt(layoutId: Int): Layout {
            return when (layoutId) {
                GRID.value -> GRID
                DETAILS.value -> DETAILS
                PUZZLE.value -> PUZZLE
                else -> GRID
            }
        }
    }

}