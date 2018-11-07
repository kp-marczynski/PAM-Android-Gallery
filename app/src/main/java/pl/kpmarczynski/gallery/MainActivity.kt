package pl.kpmarczynski.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    var adapter: ImageAdapter? = null
    var currentImage: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ImageAdapter(this)
        setupGridLayout()
    }

    fun setupGridLayout() {
        setContentView(R.layout.activity_main)

        val gridview: GridView = findViewById(R.id.gridview)

        gridview.adapter = adapter

        gridview.onItemClickListener =
                AdapterView.OnItemClickListener { parent, v, position, id ->
                    setContentView(R.layout.single_image_layout)
                    updateCurrentImage(position)
                }
        gridview.setOnScrollListener(GridOnScrollListener())
    }

    fun onPreviousButtonClick(view: View) {
        updateCurrentImage(adapter?.getPreviousPosition(currentImage!!))
    }

    fun onNextButtonClick(view: View) {
        updateCurrentImage(adapter?.getNextPosition(currentImage!!))
    }

    fun onHomeButtonClick(view: View) {
        currentImage = null
        setupGridLayout()
    }

    fun updateCurrentImage(imageId: Int?) {
        currentImage = imageId
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(adapter!!.getItemId(currentImage!!).toInt())
        findViewById<TextView>(R.id.textView).text = currentImage.toString()
    }
}
