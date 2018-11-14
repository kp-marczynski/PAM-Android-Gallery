package pl.kpmarczynski.gallery.layout

import pl.kpmarczynski.gallery.MainActivity

abstract class AbstractLayoutService(protected val activity: MainActivity, protected val layout: Layout) {
    protected var position: Int = 0

    abstract fun setupLayout(position: Int)
    abstract fun onBackPressed()

    fun refreshLayout() = setupLayout(this.position)

    protected fun switchView() = activity.updateView(Layout.switchLayout(layout), position)

}