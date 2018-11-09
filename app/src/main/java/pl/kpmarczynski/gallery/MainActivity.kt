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

    private var adapter: GridImageAdapter? = null
    private var onScrollListener: GridOnScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GridImageAdapter(this)
        onScrollListener = GridOnScrollListener()
        setupGridLayout()
    }

    fun onPreviousButtonClick(view: View) = updateCurrentImage(::getPreviousPosition)

    fun onNextButtonClick(view: View) = updateCurrentImage(::getNextPosition)

    fun onHomeButtonClick(view: View) = setupGridLayout()

    private fun setupGridLayout() {
        setContentView(R.layout.activity_main)

        val gridview: GridView = findViewById(R.id.gridview)

        gridview.adapter = adapter

        gridview.onItemClickListener =
                AdapterView.OnItemClickListener { parent, v, position, id ->
                    setContentView(R.layout.single_image_layout)
                    updateCurrentImage { position }
                }
        gridview.setOnScrollListener(onScrollListener)
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
    }
}
