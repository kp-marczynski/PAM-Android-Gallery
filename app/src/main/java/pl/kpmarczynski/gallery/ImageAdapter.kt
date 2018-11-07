package pl.kpmarczynski.gallery

import android.content.Context
import android.util.DisplayMetrics
import android.view.Surface.ROTATION_0
import android.view.Surface.ROTATION_180
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView

// references to our images
private val mThumbIds = arrayOf<Int>(
    R.drawable.sample_2, R.drawable.sample_3,
    R.drawable.sample_4, R.drawable.sample_5,
    R.drawable.sample_6, R.drawable.sample_7,
    R.drawable.sample_0, R.drawable.sample_1,
    R.drawable.sample_2, R.drawable.sample_3,
    R.drawable.sample_4, R.drawable.sample_5,
    R.drawable.sample_6, R.drawable.sample_7,
    R.drawable.sample_0, R.drawable.sample_1,
    R.drawable.sample_2, R.drawable.sample_3,
    R.drawable.sample_4, R.drawable.sample_5,
    R.drawable.sample_6, R.drawable.sample_7
)

class ImageAdapter(private val mContext: Context) : BaseAdapter() {

    override fun getCount(): Int = mThumbIds.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = mThumbIds[position].toLong()

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)

            val rownum = getRownum()
            val imageWidth: Int = getImageSize(rownum)

            (parent as GridView).numColumns = rownum
            imageView.layoutParams = ViewGroup.LayoutParams(imageWidth, imageWidth)

            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            imageView = convertView as ImageView
        }

        imageView.setImageResource(mThumbIds[position])
        return imageView
    }

    fun getRownum(): Int {
        val window: WindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (window.defaultDisplay.rotation == ROTATION_0 || window.defaultDisplay.rotation == ROTATION_180) 2 else 4
    }

    fun getImageSize(rownum: Int): Int {
        val window: WindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        window.defaultDisplay.getMetrics(displayMetrics)
        return (displayMetrics.widthPixels / rownum * 0.9).toInt()
    }

//    fun getPreviousId(position: Int): Int {
//        return getItemId(getPreviousPosition(position)).toInt()
//    }

    fun getNextId(position: Int): Int {
        return getItemId(getNextPosition(position)).toInt()
    }

    fun getNextPosition(position: Int): Int {
        val next = if (position == mThumbIds.size - 1) 0 else position + 1
        return next
    }

    fun getPreviousPosition(position: Int): Int {
        val previous = if (position == 0) mThumbIds.size - 1 else position - 1
        return previous
    }

}