package pl.kpmarczynski.gallery

import android.view.View

abstract class LayoutService(protected val activity: MainActivity, protected val layout: Layout) {
    protected var position: Int = 0

    abstract fun setupLayout(position: Int)
    abstract fun handleButtonClick(view: View)
    abstract fun onBackPressed()

    fun refreshLayout() = setupLayout(this.position)

    protected fun switchView() = activity.updateView(Layout.switchLayout(layout), position)

}