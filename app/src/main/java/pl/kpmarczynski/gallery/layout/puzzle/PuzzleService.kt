package pl.kpmarczynski.gallery.layout.puzzle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import pl.kpmarczynski.gallery.MainActivity
import pl.kpmarczynski.gallery.R
import pl.kpmarczynski.gallery.layout.AbstractLayoutService
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.repo.ImageRepository
import java.lang.Math.abs


class PuzzleService(activity: MainActivity) : AbstractLayoutService(activity, Layout.PUZZLE) {
    private lateinit var pieces: ArrayList<Bitmap>

    private var imageTopPosition: Int = 8
    private var imageLeftPosition: Int = 8
//    private var imageWidth: Int = 0
//    private var imageHeight: Int = 0

    override fun setupLayout(position: Int) {
        this.position = position
        activity.setContentView(layout.value)
        val layout: RelativeLayout = activity.findViewById(R.id.layout)

        val srcBitmap = BitmapFactory.decodeResource(activity.resources, ImageRepository.getImageId(position)!!)
        val scale = getScale(srcBitmap.width, srcBitmap.height)
        val puzzleImage =
            Bitmap.createScaledBitmap(
                srcBitmap,
                (srcBitmap.width * scale).toInt(),
                (srcBitmap.height * scale).toInt(),
                true
            )

        val imageView = ImageView(activity.applicationContext)
        imageView.setImageBitmap(puzzleImage)
        imageView.alpha = 0.5.toFloat()
        layout.addView(imageView)

        pieces = splitImage(puzzleImage)
        val touchListener = TouchListener()
        for (piece in pieces) {
            val iv = ImageView(activity.applicationContext)
            iv.setImageBitmap(piece)
            iv.setOnTouchListener(touchListener)
            layout.addView(iv)
        }
    }

    override fun onBackPressed() = switchView(Layout.GRID)

    private fun splitImage(puzzleImage: Bitmap): ArrayList<Bitmap> {
        val piecesNumber = 4
        val rows = 2
        val cols = 2

        val pieces: ArrayList<Bitmap> = ArrayList(piecesNumber)

        // Calculate the with and height of the pieces
        val pieceWidth = abs(puzzleImage.width / cols)
        val pieceHeight = abs(puzzleImage.height / rows)

        // Create each bitmap piece and add it to the resulting array
        var y = 0
        for (row in 0 until rows) {
            var x = 0
            for (col in 0 until cols) {
                pieces.add(Bitmap.createBitmap(puzzleImage, x, y, pieceWidth, pieceHeight))
                x += pieceWidth
            }
            y += pieceHeight
        }

        return pieces
    }

    private fun getScale(bitmapWidth: Int, bitmapHeight: Int): Double {
        val window: WindowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        window.defaultDisplay.getMetrics(displayMetrics)

        val maxHeight: Int = displayMetrics.heightPixels - this.imageTopPosition * 2
        val maxWidth: Int = displayMetrics.widthPixels - this.imageLeftPosition * 2

        val widthScale: Double = maxWidth / bitmapWidth.toDouble()
        val heightScale: Double = maxHeight / bitmapHeight.toDouble()

        return if (widthScale < heightScale) widthScale else heightScale
    }
}