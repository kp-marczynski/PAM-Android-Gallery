package pl.kpmarczynski.gallery

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.kpmarczynski.gallery.layout.AbstractLayoutService
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.layout.details.DetailsService
import pl.kpmarczynski.gallery.layout.grid.GridService
import pl.kpmarczynski.gallery.layout.puzzle.PuzzleService


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

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        getService(currentLayout).saveState(bundle)
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)

        val layout = AbstractLayoutService.restoreLayout(bundle)
        getService(layout).restoreState(bundle)
        updateView(layout, getService(layout).position)
    }
}
