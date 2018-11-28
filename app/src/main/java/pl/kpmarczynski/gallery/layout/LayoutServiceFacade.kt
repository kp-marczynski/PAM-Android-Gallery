package pl.kpmarczynski.gallery.layout

import android.os.Bundle
import pl.kpmarczynski.gallery.layout.details.DetailsService
import pl.kpmarczynski.gallery.layout.grid.GridService
import pl.kpmarczynski.gallery.layout.puzzle.PuzzleService

class LayoutServiceFacade {
    private val detailsService: AbstractLayoutService = DetailsService()
    private val gridService: AbstractLayoutService = GridService()
    private val puzzleService: AbstractLayoutService = PuzzleService()

    var currentLayout: Layout = Layout.GRID

    val current: AbstractLayoutService
        get() = when (currentLayout) {
            Layout.GRID -> gridService
            Layout.DETAILS -> detailsService
            Layout.PUZZLE -> puzzleService
        }
    var position: Int
        get() = current.position
        set(position) {
            current.position = position
        }

    fun saveState(bundle: Bundle) = current.saveState(bundle)

    fun restoreState(bundle: Bundle){
        currentLayout = AbstractLayoutService.restoreLayout(bundle)
        current.restoreState(bundle)
    }
}