package pl.kpmarczynski.gallery.layout.puzzle

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.RelativeLayout
import pl.kpmarczynski.gallery.MainActivity
import pl.kpmarczynski.gallery.R
import pl.kpmarczynski.gallery.layout.AbstractLayoutService
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.repo.ImageRepository
import java.lang.Math.abs
import java.util.*


class PuzzleService(activity: MainActivity) : AbstractLayoutService(activity, Layout.PUZZLE) {
    lateinit var pieces: ArrayList<PuzzlePiece>

    private var imageTopPosition: Int = 8
    private var imageLeftPosition: Int = 8
//    private var imageWidth: Int = 0
//    private var imageHeight: Int = 0

    override fun setupLayout(position: Int) {
        this.position = position
        activity.setContentView(layout.value)
        val layout: RelativeLayout = activity.findViewById(R.id.layout)

        layout.post {
            val srcBitmap = BitmapFactory.decodeResource(activity.resources, ImageRepository.getImageId(position)!!)
            val scale = getScale(srcBitmap.width, srcBitmap.height, layout.width, layout.height)
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
            val touchListener = TouchListener(this)
            for (piece in pieces) {
                piece.setOnTouchListener(touchListener)
                layout.addView(piece)

                val lParams = piece.layoutParams as RelativeLayout.LayoutParams
                lParams.leftMargin = Random().nextInt(layout.width - piece.pieceWidth)
                lParams.topMargin = layout.height - piece.pieceHeight
                piece.layoutParams = lParams
            }
        }
    }

    override fun onBackPressed() = switchView(Layout.GRID)

    private fun splitImage(puzzleImage: Bitmap): ArrayList<PuzzlePiece> {
        val rows = 2
        val cols = 2

        val pieces: ArrayList<PuzzlePiece> = ArrayList(rows * cols)

        // Calculate the with and height of the pieces
        val pieceWidth = abs(puzzleImage.width / cols)
        val pieceHeight = abs(puzzleImage.height / rows)

        // Create each bitmap piece and add it to the resulting array
        var y = 0
        for (row in 0 until rows) {
            var x = 0
            for (col in 0 until cols) {
                val pieceBitmap = Bitmap.createBitmap(puzzleImage, x, y, pieceWidth, pieceHeight)
                val piece = PuzzlePiece(activity.applicationContext, pieceBitmap, x, y, pieceWidth, pieceHeight)
                pieces.add(piece)
                x += pieceWidth
            }
            y += pieceHeight
        }

        return pieces
    }

    private fun getScale(bitmapWidth: Int, bitmapHeight: Int, layoutWidth: Int, layoutHeight: Int): Double {

        val maxHeight: Int = layoutHeight - this.imageTopPosition * 2
        val maxWidth: Int = layoutWidth - this.imageLeftPosition * 2

        val widthScale: Double = maxWidth / bitmapWidth.toDouble()
        val heightScale: Double = maxHeight / bitmapHeight.toDouble()

        return if (widthScale < heightScale) widthScale else heightScale
    }
}