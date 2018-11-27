package pl.kpmarczynski.gallery.layout.puzzle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Surface
import android.view.WindowManager
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

    private var imageTopPosition: Int = 0
    private var imageLeftPosition: Int = 0
    private val padding: Int = 48
    var drawerWidth = 0
    var drawerHeight = 0
    var drawerTop = 0
    var drawerLeft = 0
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

            if (isPortrait()) {
                this.imageLeftPosition = (layout.width - puzzleImage.width) / 2
                this.imageTopPosition = this.padding

                drawerWidth = layout.width - 2 * padding
                drawerHeight = layout.height - puzzleImage.height - 3 * padding
            } else {
                this.imageTopPosition = (layout.height - puzzleImage.height) / 2
                this.imageLeftPosition = this.padding

                drawerWidth = layout.width - puzzleImage.width - 3 * padding
                drawerHeight = layout.height - 2 * padding
            }

            val puzzleImageParams = imageView.layoutParams as RelativeLayout.LayoutParams
            puzzleImageParams.leftMargin = this.imageLeftPosition
            puzzleImageParams.topMargin = this.imageTopPosition

            imageView.layoutParams = puzzleImageParams

            val drawerImage = Bitmap.createBitmap(drawerWidth, drawerHeight, Bitmap.Config.ARGB_8888)
            drawerImage.eraseColor(android.graphics.Color.LTGRAY)
            val solidImage = ImageView(activity)
            solidImage.setImageBitmap(drawerImage)
            layout.addView(solidImage)

            drawerLeft = if (isPortrait()) padding else puzzleImage.width + 2 * padding
            drawerTop = if (isPortrait()) puzzleImage.height + 2 * padding else padding
            val drawerParams = solidImage.layoutParams as RelativeLayout.LayoutParams
            drawerParams.leftMargin = drawerLeft
            drawerParams.topMargin = drawerTop

            solidImage.layoutParams = drawerParams

            pieces = splitImage(puzzleImage)
            val touchListener = TouchListener(this)
            for (piece in pieces) {
                piece.setOnTouchListener(touchListener)
                layout.addView(piece)
                putPieceInDrawer(piece)
            }
        }
    }

    fun putPieceInDrawer(piece: PuzzlePiece) {
        val x = Random().nextInt(drawerWidth - piece.pieceWidth) + drawerLeft
        val y = Random().nextInt(drawerHeight - piece.pieceHeight) + drawerTop

//        val animX = ObjectAnimator.ofFloat(piece, "x", x.toFloat())
//        val animY = ObjectAnimator.ofFloat(piece, "y", y.toFloat())
//        val animSetXY = AnimatorSet()
//        animSetXY.playTogether(animX, animY)
////        animSetXY.startDelay = 100
//        animSetXY.addListener(AnimatorListener(piece, x,y))
//        animSetXY.start()
////        animSetXY.setupEndValues()
        val lParams = piece.layoutParams as RelativeLayout.LayoutParams
        lParams.leftMargin = x
        lParams.topMargin = y
        piece.layoutParams = lParams


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
                val piece = PuzzlePiece(
                    activity.applicationContext,
                    pieceBitmap,
                    x + this.imageLeftPosition,
                    y + this.imageTopPosition,
                    pieceWidth,
                    pieceHeight
                )
                pieces.add(piece)
                x += pieceWidth
            }
            y += pieceHeight
        }

        return pieces
    }

    private fun getScale(bitmapWidth: Int, bitmapHeight: Int, layoutWidth: Int, layoutHeight: Int): Double {
        var maxHeight: Int = layoutHeight - padding * 2
        var maxWidth: Int = layoutWidth - padding * 2

        if (isPortrait()) {
            maxHeight = (maxHeight * 0.5).toInt()
        } else {
            maxWidth = (maxWidth * 0.5).toInt()
        }

        val widthScale: Double = maxWidth / bitmapWidth.toDouble()
        val heightScale: Double = maxHeight / bitmapHeight.toDouble()

        return if (widthScale < heightScale) widthScale else heightScale
    }

    private fun getRotation(): Int {
        val window: WindowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return window.defaultDisplay.rotation
    }

    private fun isPortrait(): Boolean {
        val rotation = getRotation()
        return rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180
    }
}