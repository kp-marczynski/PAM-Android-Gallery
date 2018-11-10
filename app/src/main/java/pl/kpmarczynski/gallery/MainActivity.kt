package pl.kpmarczynski.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import pl.kpmarczynski.gallery.ImageRepository.Companion.getNextPosition
import pl.kpmarczynski.gallery.ImageRepository.Companion.getPreviousPosition


class MainActivity : AppCompatActivity() {

    private var adapter: GridImageAdapter = GridImageAdapter(this)
    private var onScrollListener: GridOnScrollListener = GridOnScrollListener()
    private var state: State = State(Layout.GRID, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun setupGridLayout() {
        state.currentView = Layout.GRID
        setContentView(Layout.GRID.value)

        val gridview: GridView = findViewById(R.id.gridview)

        gridview.adapter = adapter

        gridview.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    state.currentView = Layout.DETAILS
                    state.currentImagePosition = position
                    setupDetailsLayout()
                }
        gridview.setOnScrollListener(onScrollListener)
        gridview.setSelection(state.currentImagePosition)
    }

    private fun setupDetailsLayout() {
        setContentView(Layout.DETAILS.value)
        updateCurrentImage { state.currentImagePosition }
    }

    private fun updateCurrentImage(getNewPosition: (Int) -> Int?) {
        val imageView = findViewById<ImageView>(R.id.imageView)

        val oldPosition = if (imageView.tag != null) Integer.parseInt(imageView.tag.toString()) else -1
        val newPosition = getNewPosition(oldPosition)
        imageView.tag = newPosition.toString()
        if (newPosition != null) {
            val imageId: Int? = ImageRepository.getImageId(newPosition)
            if (imageId != null) {
                imageView.setImageResource(imageId)
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
