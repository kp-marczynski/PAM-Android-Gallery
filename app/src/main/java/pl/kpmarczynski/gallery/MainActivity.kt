package pl.kpmarczynski.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.layout.LayoutServiceFacade


class MainActivity : AppCompatActivity() {

    private val layoutServiceFacade = LayoutServiceFacade()
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        updateView(layoutServiceFacade.currentLayout, layoutServiceFacade.position)
    }

    override fun onBackPressed() = layoutServiceFacade.current.onBackPressed()

    fun updateView(layout: Layout, position: Int) {
        layoutServiceFacade.currentLayout = layout
        layoutServiceFacade.position = position
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, layoutServiceFacade.current)
        fragmentTransaction.commit()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        layoutServiceFacade.saveState(bundle)
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)

        layoutServiceFacade.restoreState(bundle)
        updateView(layoutServiceFacade.currentLayout, layoutServiceFacade.position)
    }
}
