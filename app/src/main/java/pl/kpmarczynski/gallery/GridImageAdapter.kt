package pl.kpmarczynski.gallery

import android.content.Context
import android.util.DisplayMetrics
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.GridView
import com.facebook.drawee.view.SimpleDraweeView

// references to our images


class GridImageAdapter(private val mContext: Context) : BaseAdapter() {

    override fun getCount(): Int = ImageRepository.getCount()

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = ImageRepository.getImageId(position)?.toLong() ?: 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): SimpleDraweeView {
        val imageView: SimpleDraweeView
        if (convertView == null) {
            imageView = SimpleDraweeView(mContext)
//            imageView

            val rownum = getRownum()
            val imageWidth: Int = getImageSize(rownum)

            (parent as GridView).numColumns = rownum
            imageView.layoutParams = ViewGroup.LayoutParams(imageWidth, imageWidth)

//            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//            imageView.setPadding(8,8,8,8)
        } else {
            imageView = convertView as SimpleDraweeView
        }

//        imageView.setImageResource(getItemId(position).toInt())
//        imageView.hierarchy = GenericDraweeHierarchyBuilder.newInstance(get)
        val imageId = ImageRepository.getImageId(position)
        if(imageId !=null){
            imageView.setImageURI("res:/$imageId")
        }
        return imageView
    }

    private fun getRownum(): Int {
        val window: WindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (window.defaultDisplay.rotation == Surface.ROTATION_0 || window.defaultDisplay.rotation == Surface.ROTATION_180) 2 else 4
    }

    private fun getImageSize(rownum: Int): Int {
        val window: WindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        window.defaultDisplay.getMetrics(displayMetrics)
        return (displayMetrics.widthPixels / rownum * 0.9).toInt()
    }
}