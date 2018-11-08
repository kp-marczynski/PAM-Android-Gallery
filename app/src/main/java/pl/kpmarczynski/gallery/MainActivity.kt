package pl.kpmarczynski.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private var adapter: GridImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GridImageAdapter(this)
        setupGridLayout()
    }

    fun onPreviousButtonClick(view: View) {
        val position = Integer.parseInt(findViewById<ImageView>(R.id.imageView).tag.toString())
        updateCurrentImage(ImageRepository.getPreviousPosition(position))
    }

    fun onNextButtonClick(view: View) {
        val position = Integer.parseInt(findViewById<ImageView>(R.id.imageView).tag.toString())
        updateCurrentImage(ImageRepository.getNextPosition(position))
    }

    fun onHomeButtonClick(view: View) {
        setupGridLayout()
    }

    private fun setupGridLayout() {
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

    private fun updateCurrentImage(imagePosition: Int) {
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.tag = imagePosition.toString()
        imageView.setImageResource(ImageRepository.getImageId(imagePosition).toInt())
        findViewById<TextView>(R.id.textView).text = imagePosition.toString()
    }
}
