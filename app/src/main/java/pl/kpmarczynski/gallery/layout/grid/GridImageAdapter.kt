package pl.kpmarczynski.gallery.layout.grid

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import com.facebook.drawee.view.SimpleDraweeView
import pl.kpmarczynski.gallery.repo.ImageRepository

class GridImageAdapter(
    private val mContext: Context,
    private val rownum: Int,
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<GridImageAdapter.PartViewHolder>() {

    class PartViewHolder(val imageView: SimpleDraweeView) : RecyclerView.ViewHolder(imageView) {
        fun bind(position: Int, clickListener: (Int) -> Unit) {
            itemView.setOnClickListener { clickListener(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val imageView = SimpleDraweeView(mContext)

        val imageWidth: Int = getImageSize(rownum)

        imageView.layoutParams = ViewGroup.LayoutParams(imageWidth, imageWidth)
        imageView.setPadding(16, 16, 16, 16)

        return PartViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        val imageId = ImageRepository.getImageId(position)
        if (imageId != null) {
            holder.imageView.setImageURI("res:/$imageId")
        }
        holder.bind(position, clickListener)
    }

    override fun getItemCount() = ImageRepository.getCount()

    private fun getImageSize(rownum: Int): Int {
        val window: WindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        window.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels / rownum
    }
}