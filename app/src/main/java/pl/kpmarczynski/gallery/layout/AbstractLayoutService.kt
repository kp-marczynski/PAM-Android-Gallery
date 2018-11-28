package pl.kpmarczynski.gallery.layout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.kpmarczynski.gallery.MainActivity

abstract class AbstractLayoutService(protected val layout: Layout) : Fragment() {

    var position: Int = 0

    abstract fun setupLayout(position: Int)
    abstract fun onBackPressed()

    fun refreshLayout() = setupLayout(this.position)

    fun switchView(target: Layout) = (activity as MainActivity).updateView(target, position)

    open fun saveState(bundle: Bundle) {
        bundle.putInt("LAYOUT", layout.value)
        bundle.putInt("POSITION", position)
    }

    open fun restoreState(bundle: Bundle) {
        position = bundle.getInt("POSITION")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layout.value, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupLayout(position)
    }

    companion object {
        fun restoreLayout(bundle: Bundle): Layout = Layout.createFromInt(bundle.getInt("LAYOUT"))
    }
}