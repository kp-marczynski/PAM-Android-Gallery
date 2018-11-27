package pl.kpmarczynski.gallery.layout.puzzle

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.support.constraint.ConstraintLayout
import android.widget.ImageView
import pl.kpmarczynski.gallery.MainActivity
import pl.kpmarczynski.gallery.R
import pl.kpmarczynski.gallery.layout.AbstractLayoutService
import pl.kpmarczynski.gallery.layout.Layout
import java.lang.Math.abs


class PuzzleService(activity: MainActivity) : AbstractLayoutService(activity, Layout.PUZZLE) {
    private lateinit var pieces: ArrayList<Bitmap>

    override fun setupLayout(position: Int) {
        activity.setContentView(layout.value)
        val layout: ConstraintLayout = activity.findViewById(R.id.layout)
        val imageView: ImageView = activity.findViewById(R.id.imageView)
        imageView.post {
            pieces = splitImage()
            for (piece in pieces) {
                val iv = ImageView(activity.applicationContext)
                iv.setImageBitmap(piece)
                layout.addView(iv)
            }
        }
    }

    override fun onBackPressed() = switchView(Layout.GRID)

    private fun splitImage(): ArrayList<Bitmap> {
        val piecesNumber = 12
        val rows = 4
        val cols = 3

        val imageView: ImageView = activity.findViewById(R.id.imageView)
        val pieces: ArrayList<Bitmap> = ArrayList(piecesNumber)

        // Get the bitmap of the source image
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        val dimensions = getBitmapPositionInsideImageView(imageView)
        val scaledBitmapLeft = dimensions[0]
        val scaledBitmapTop = dimensions[1]
        val scaledBitmapWidth = dimensions[2]
        val scaledBitmapHeight = dimensions[3]

        val croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft)
        val croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop)

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true)
        val croppedBitmap = Bitmap.createBitmap(
            scaledBitmap,
            abs(scaledBitmapLeft),
            abs(scaledBitmapTop),
            croppedImageWidth,
            croppedImageHeight
        )

        // Calculate the with and height of the pieces
        val pieceWidth = croppedImageWidth / cols
        val pieceHeight = croppedImageHeight / rows

        // Create each bitmap piece and add it to the resulting array
        var yCoord = 0
        for (row in 0 until rows) {
            var xCoord = 0
            for (col in 0 until cols) {
                pieces.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, pieceWidth, pieceHeight))
                xCoord += pieceWidth
            }
            yCoord += pieceHeight
        }

        return pieces
    }

    private fun getBitmapPositionInsideImageView(imageView: ImageView?): IntArray {
        val ret = IntArray(4)

        if (imageView == null || imageView.drawable == null)
            return ret

        // Get image dimensions
        // Get image matrix values and place them in an array
        val f = FloatArray(9)
        imageView.imageMatrix.getValues(f)

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        val scaleX = f[Matrix.MSCALE_X]
        val scaleY = f[Matrix.MSCALE_Y]

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        val d = imageView.drawable
        val origW = d.intrinsicWidth
        val origH = d.intrinsicHeight

        // Calculate the actual dimensions
        val actW = Math.round(origW * scaleX)
        val actH = Math.round(origH * scaleY)

        ret[2] = actW
        ret[3] = actH

        // Get image position
        // We assume that the image is centered into ImageView
        val imgViewW = imageView.width
        val imgViewH = imageView.height

        val top = (imgViewH - actH) / 2
        val left = (imgViewW - actW) / 2

        ret[0] = left
        ret[1] = top

        return ret
    }
}