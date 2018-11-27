package pl.kpmarczynski.gallery.layout.puzzle

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import pl.kpmarczynski.gallery.layout.Layout
import java.lang.Math.*


class TouchListener(val puzzleService: PuzzleService) : View.OnTouchListener {
    private var xDelta: Float = 0.toFloat()
    private var yDelta: Float = 0.toFloat()

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.rawX
        val y = motionEvent.rawY
        val tolerance = sqrt(pow(view.width.toDouble(), 2.0) + pow(view.height.toDouble(), 2.0)) / 5

        val piece = view as PuzzlePiece
        if (!piece.canMove) {
            return true
        }

        val lParams = view.getLayoutParams() as RelativeLayout.LayoutParams
        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                xDelta = x - lParams.leftMargin
                yDelta = y - lParams.topMargin
                piece.bringToFront()
            }
            MotionEvent.ACTION_MOVE -> {
                lParams.leftMargin = (x - xDelta).toInt()
                lParams.topMargin = (y - yDelta).toInt()
                view.setLayoutParams(lParams)
            }
            MotionEvent.ACTION_UP -> {
                val xDiff = abs(piece.xCoord - lParams.leftMargin)
                val yDiff = abs(piece.yCoord - lParams.topMargin)
                if (xDiff <= tolerance && yDiff <= tolerance) {
                    lParams.leftMargin = piece.xCoord
                    lParams.topMargin = piece.yCoord
                    piece.layoutParams = lParams
                    piece.canMove = false
                    sendViewToBack(piece)
                    if (isGameOver()) {
                        puzzleService.pieces = null
                        puzzleService.switchView(Layout.GRID)
                    }
                } else {
                    puzzleService.putPieceInDrawer(piece)
                }
            }
        }

        return true
    }

    private fun isGameOver(): Boolean {
        if (puzzleService.pieces != null) {
            for (piece in puzzleService.pieces!!) {
                if (piece.canMove) {
                    return false
                }
            }
            return true
        } else return false
    }

    fun sendViewToBack(child: View) {
        val parent = child.parent as ViewGroup?
        if (null != parent) {
            parent.removeView(child)
            parent.addView(child, 0)
        }
    }
}