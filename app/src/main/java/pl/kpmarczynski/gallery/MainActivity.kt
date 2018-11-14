package pl.kpmarczynski.gallery

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import pl.kpmarczynski.gallery.layout.details.DetailsService
import pl.kpmarczynski.gallery.layout.grid.GridService
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.layout.LayoutService


class MainActivity : AppCompatActivity() {
    //    private val state: State = State(Layout.GRID, 0)
    private val detailsService: LayoutService =
        DetailsService(this)
    private val gridService: LayoutService =
        GridService(this)
    private var currentLayout: Layout = Layout.GRID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        updateView(currentLayout, 0)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        getService(currentLayout).refreshLayout()
    }

    override fun onBackPressed() = getService(currentLayout).onBackPressed()

    fun updateView(layout: Layout, position: Int) {
        currentLayout = layout
        getService(layout).setupLayout(position)
    }

    fun onButtonClick(view: View) = getService(currentLayout).handleButtonClick(view)

    private fun getService(layout: Layout): LayoutService {
        return when (layout) {
            Layout.GRID -> gridService
            Layout.DETAILS -> detailsService
        }
    }
}
