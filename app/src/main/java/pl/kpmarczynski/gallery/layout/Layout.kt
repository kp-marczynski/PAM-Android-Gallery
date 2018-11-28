package pl.kpmarczynski.gallery.layout

import pl.kpmarczynski.gallery.R

enum class Layout(val value: Int) {
    GRID(R.layout.grid_layout),
    DETAILS(R.layout.details_layout),
    PUZZLE(R.layout.puzzle_layout);

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