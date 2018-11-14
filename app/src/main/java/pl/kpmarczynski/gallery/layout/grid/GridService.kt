package pl.kpmarczynski.gallery.layout.grid

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Surface
import android.view.View
import android.view.WindowManager
import pl.kpmarczynski.gallery.MainActivity
import pl.kpmarczynski.gallery.R
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.layout.LayoutService

class GridService(activity: MainActivity) : LayoutService(activity,
    Layout.GRID
) {
    private var viewAdapter: RecyclerView.Adapter<*>? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: GridLayoutManager
    private val onScrollListener: GridOnScrollListener =
        GridOnScrollListener()
    private var rotation = Surface.ROTATION_0

    override fun setupLayout(position: Int) {
        val newRotation = getRotation()
        if (viewAdapter == null || newRotation != rotation) {
            rotation = newRotation
            viewAdapter = GridImageAdapter(
                activity,
                getRownum()
            ) { pos: Int -> gridItemClicked(pos) }
        }
        activity.setContentView(layout.value)
        viewManager = GridLayoutManager(activity, getRownum())
        recyclerView = activity.findViewById<RecyclerView>(R.id.gridview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        recyclerView.addOnScrollListener(onScrollListener)
        recyclerView.scrollToPosition(position)
    }

    override fun handleButtonClick(view: View) = println("You've been mistaken. There is no buttons to click")

    override fun onBackPressed() = activity.finish()

    private fun gridItemClicked(position: Int) {
        this.position = position
        switchView()
    }

    private fun getRotation(): Int {
        val window: WindowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return window.defaultDisplay.rotation
    }

    private fun getRownum(): Int {
        val rotation = getRotation()
        return if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) 2 else 4
    }
}