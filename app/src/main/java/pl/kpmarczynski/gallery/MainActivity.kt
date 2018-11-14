package pl.kpmarczynski.gallery

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import pl.kpmarczynski.gallery.layout.AbstractLayoutService
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.layout.details.DetailsService
import pl.kpmarczynski.gallery.layout.grid.GridService


class MainActivity : AppCompatActivity() {
    private val detailsService: AbstractLayoutService = DetailsService(this)
    private val gridService: AbstractLayoutService = GridService(this)
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

    private fun getService(layout: Layout): AbstractLayoutService {
        return when (layout) {
            Layout.GRID -> gridService
            Layout.DETAILS -> detailsService
        }
    }
}
