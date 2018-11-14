package pl.kpmarczynski.gallery

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Surface
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import pl.kpmarczynski.gallery.ImageRepository.Companion.getNextPosition
import pl.kpmarczynski.gallery.ImageRepository.Companion.getPreviousPosition


class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: GridLayoutManager
    private lateinit var onScrollListener: GridOnScrollListener
    private var state: State = State(Layout.GRID, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rownum = getRownum()
        viewAdapter = GridImageAdapter(this, rownum) { position: Int -> gridItemClicked(position) }
        onScrollListener = GridOnScrollListener(rownum)
        Fresco.initialize(this)
        updateViewWithState()
    }

    private fun updateViewWithState() {
        when (state.currentView) {
            Layout.GRID -> setupGridLayout()
            Layout.DETAILS -> setupDetailsLayout()
        }
    }

    fun onPreviousButtonClick(view: View) = updateCurrentImage(::getPreviousPosition)

    fun onNextButtonClick(view: View) = updateCurrentImage(::getNextPosition)

    fun onHomeButtonClick(view: View) = setupGridLayout()

    private fun getRownum(): Int {
        val window: WindowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (window.defaultDisplay.rotation == Surface.ROTATION_0 || window.defaultDisplay.rotation == Surface.ROTATION_180) 2 else 4
    }

    private fun gridItemClicked(position: Int) {
        state.currentView = Layout.DETAILS
        state.currentImagePosition = position
        setupDetailsLayout()
    }

    private fun setupGridLayout() {
        state.currentView = Layout.GRID
        setContentView(Layout.GRID.value)
        viewManager = GridLayoutManager(this, getRownum())
        recyclerView = findViewById<RecyclerView>(R.id.gridview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        recyclerView.addOnScrollListener(onScrollListener)
    }

    private fun setupDetailsLayout() {
        setContentView(Layout.DETAILS.value)
        updateCurrentImage { state.currentImagePosition }
    }

    private fun updateCurrentImage(getNewPosition: (Int) -> Int?) {
        val imageView = findViewById<SimpleDraweeView>(R.id.imageView)

        val newPosition = getNewPosition(state.currentImagePosition)
        if (newPosition != null) {
            val imageId: Int? = ImageRepository.getImageId(newPosition)
            if (imageId != null) {
                imageView.setImageURI("res:/$imageId")
            }
        }
        findViewById<TextView>(R.id.textView).text = newPosition.toString()
        state.currentImagePosition = newPosition!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("LAYOUT", state.currentView.value)
        outState.putInt("POSITION", state.currentImagePosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        state.currentImagePosition = savedInstanceState.getInt("POSITION")
        state.currentView = Layout.createFromInt(savedInstanceState.getInt("LAYOUT"))
        updateViewWithState()
    }
}
