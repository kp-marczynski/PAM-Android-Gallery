package pl.kpmarczynski.gallery.layout

import android.view.View
import pl.kpmarczynski.gallery.MainActivity

abstract class LayoutService(protected val activity: MainActivity, protected val layout: Layout) {
    protected var position: Int = 0

    abstract fun setupLayout(position: Int)
    abstract fun handleButtonClick(view: View)
    abstract fun onBackPressed()

    fun refreshLayout() = setupLayout(this.position)

    protected fun switchView() = activity.updateView(Layout.switchLayout(layout), position)

}