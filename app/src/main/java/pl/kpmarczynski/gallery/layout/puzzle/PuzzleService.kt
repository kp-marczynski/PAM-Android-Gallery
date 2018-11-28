package pl.kpmarczynski.gallery.layout.puzzle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Surface
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.RelativeLayout
import pl.kpmarczynski.gallery.R
import pl.kpmarczynski.gallery.layout.AbstractLayoutService
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.repo.ImageRepository
import java.lang.Math.abs
import java.util.*


class PuzzleService : AbstractLayoutService(Layout.PUZZLE) {

    var pieces: ArrayList<PuzzlePiece>? = null

    private val touchListener = TouchListener(this)

    private var imageTopPosition: Int = 0
    private var imageLeftPosition: Int = 0
    private val padding: Int = 48
    private var drawerWidth = 0
    private var drawerHeight = 0
    private var drawerTop = 0
    private var drawerLeft = 0


    override fun setupLayout(position: Int) {
        this.position = position
        val layout: RelativeLayout = activity!!.findViewById(R.id.layout)

        layout.post {
            val puzzleBitmap = createScaledPuzzleBitmap(layout.width, layout.height)
            putPuzzleBitmapInLayout(puzzleBitmap, layout)

            createDrawer(puzzleBitmap.width, puzzleBitmap.height, layout)

            createPuzzlePieces(puzzleBitmap, layout)
        }
    }

    override fun onBackPressed() {
        pieces = null
        switchView(Layout.GRID)
    }

    fun putPieceInDrawer(piece: PuzzlePiece) {
        val x = Random().nextInt(drawerWidth - piece.pieceWidth) + drawerLeft
        val y = Random().nextInt(drawerHeight - piece.pieceHeight) + drawerTop

        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val params = piece.layoutParams as RelativeLayout.LayoutParams

                params.leftMargin += ((x - params.leftMargin) * interpolatedTime).toInt()
                params.topMargin += ((y - params.topMargin) * interpolatedTime).toInt()
                piece.layoutParams = params
            }
        }
        a.duration = 500
        piece.startAnimation(a)
    }

    private fun createPuzzlePieces(puzzleBitmap: Bitmap, layout: RelativeLayout) {
        pieces = splitImage(puzzleBitmap)
        for (piece in pieces!!) {
            piece.setOnTouchListener(touchListener)
            layout.addView(piece)
            if (piece.canMove) {
                putPieceInDrawer(piece)
            } else {
                val pieceParams = piece.layoutParams as RelativeLayout.LayoutParams
                pieceParams.leftMargin = piece.xCoord
                pieceParams.topMargin = piece.yCoord
                piece.layoutParams = pieceParams
            }
        }
    }

    private fun createDrawer(puzzleBitmapWidth: Int, puzzleBitmapHeight: Int, layout: RelativeLayout) {
        if (isPortrait()) {
            drawerWidth = layout.width - 2 * padding
            drawerHeight = layout.height - puzzleBitmapHeight - 3 * padding
        } else {
            drawerWidth = layout.width - puzzleBitmapWidth - 3 * padding
            drawerHeight = layout.height - 2 * padding
        }
        val drawerImage = Bitmap.createBitmap(drawerWidth, drawerHeight, Bitmap.Config.ARGB_8888)
        drawerImage.eraseColor(android.graphics.Color.LTGRAY)
        val solidImage = ImageView(activity)
        solidImage.setImageBitmap(drawerImage)
        layout.addView(solidImage)

        drawerLeft = if (isPortrait()) padding else puzzleBitmapWidth + 2 * padding
        drawerTop = if (isPortrait()) puzzleBitmapHeight + 2 * padding else padding
        val drawerParams = solidImage.layoutParams as RelativeLayout.LayoutParams
        drawerParams.leftMargin = drawerLeft
        drawerParams.topMargin = drawerTop

        solidImage.layoutParams = drawerParams
    }

    private fun putPuzzleBitmapInLayout(puzzleBitmap: Bitmap, layout: RelativeLayout) {
        val puzzleImageView = ImageView(activity!!.applicationContext)
        puzzleImageView.setImageBitmap(puzzleBitmap)
        puzzleImageView.alpha = 0.5.toFloat()
        layout.addView(puzzleImageView)

        if (isPortrait()) {
            this.imageLeftPosition = (layout.width - puzzleBitmap.width) / 2
            this.imageTopPosition = this.padding
        } else {
            this.imageTopPosition = (layout.height - puzzleBitmap.height) / 2
            this.imageLeftPosition = this.padding
        }

        val puzzleImageParams = puzzleImageView.layoutParams as RelativeLayout.LayoutParams
        puzzleImageParams.leftMargin = this.imageLeftPosition
        puzzleImageParams.topMargin = this.imageTopPosition

        puzzleImageView.layoutParams = puzzleImageParams
    }

    private fun createScaledPuzzleBitmap(maxWidth: Int, maxHeight: Int): Bitmap {
        val srcBitmap = BitmapFactory.decodeResource(resources, ImageRepository.getImageId(position)!!)
        val scale = getScale(srcBitmap.width, srcBitmap.height, maxWidth, maxHeight)
        return Bitmap.createScaledBitmap(
            srcBitmap,
            (srcBitmap.width * scale).toInt(),
            (srcBitmap.height * scale).toInt(),
            true
        )
    }

    private fun splitImage(puzzleImage: Bitmap): ArrayList<PuzzlePiece> {
        val rows = 2
        val cols = 2

        val localPieces: ArrayList<PuzzlePiece> = ArrayList(rows * cols)

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
                    activity!!.applicationContext,
                    pieceBitmap,
                    x + this.imageLeftPosition,
                    y + this.imageTopPosition,
                    pieceWidth,
                    pieceHeight
                )
                localPieces.add(piece)
                x += pieceWidth
            }
            y += pieceHeight
        }

        if (this.pieces != null && localPieces.size == this.pieces!!.size) {
            for (i in 0 until localPieces.size) {
                localPieces[i].canMove = this.pieces!![i].canMove
            }
        }
        return localPieces
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
        val window: WindowManager = activity!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return window.defaultDisplay.rotation
    }

    private fun isPortrait(): Boolean {
        val rotation = getRotation()
        return rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180
    }
}