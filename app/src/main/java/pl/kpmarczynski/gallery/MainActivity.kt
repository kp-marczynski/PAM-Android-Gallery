package pl.kpmarczynski.gallery

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.kpmarczynski.gallery.layout.AbstractLayoutService
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.layout.details.DetailsService
import pl.kpmarczynski.gallery.layout.grid.GridService
import pl.kpmarczynski.gallery.layout.puzzle.PuzzlePiece
import pl.kpmarczynski.gallery.layout.puzzle.PuzzleService
import java.util.*


class MainActivity : AppCompatActivity() {
    private val detailsService: AbstractLayoutService = DetailsService()
    private val gridService: AbstractLayoutService = GridService()
    private val puzzleService: AbstractLayoutService = PuzzleService()
    private var currentLayout: Layout = Layout.GRID
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        updateView(currentLayout, 0)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        getService(currentLayout).refreshLayout()
    }

    override fun onBackPressed() = getService(currentLayout).onBackPressed()

    fun updateView(layout: Layout, position: Int) {
        currentLayout = layout
        val fragmentTransaction = fragmentManager.beginTransaction()
        getService(layout).position = position
        fragmentTransaction.replace(R.id.fragment_container, getService(layout))
        fragmentTransaction.commit()
    }

    private fun getService(layout: Layout): AbstractLayoutService {
        return when (layout) {
            Layout.GRID -> gridService
            Layout.DETAILS -> detailsService
            Layout.PUZZLE -> puzzleService
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("LAYOUT", currentLayout.value)
        outState.putInt("POSITION", getService(currentLayout).position)
        if (currentLayout == Layout.PUZZLE) {
            val puzzles = getService(Layout.PUZZLE) as PuzzleService
            outState.putSerializable("PIECES", puzzles.pieces)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val layout = Layout.createFromInt(savedInstanceState.getInt("LAYOUT"))
        if (layout == Layout.PUZZLE) {
            val puzzles = getService(Layout.PUZZLE) as PuzzleService
            puzzles.pieces = savedInstanceState.getSerializable("PIECES") as ArrayList<PuzzlePiece>
        }
        updateView(
            layout,
            savedInstanceState.getInt("POSITION")
        )
    }
}
