package pl.kpmarczynski.gallery.layout.details

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import pl.kpmarczynski.gallery.R
import pl.kpmarczynski.gallery.layout.AbstractLayoutService
import pl.kpmarczynski.gallery.layout.Layout
import pl.kpmarczynski.gallery.repo.ImageRepository

class DetailsService : AbstractLayoutService(Layout.DETAILS) {

    override fun setupLayout(position: Int) {
        this.position = position
        updateCurrentImage { position }

        activity!!.findViewById<Button>(R.id.homeButton).setOnClickListener { switchView(Layout.GRID) }
        activity!!.findViewById<Button>(R.id.previousButton)
            .setOnClickListener { updateCurrentImage(ImageRepository.Companion::getPreviousPosition) }
        activity!!.findViewById<Button>(R.id.nextButton)
            .setOnClickListener { updateCurrentImage(ImageRepository.Companion::getNextPosition) }
        activity!!.findViewById<ImageView>(R.id.imageView).setOnLongClickListener { switchView(Layout.PUZZLE); true }
    }

    override fun onBackPressed() = switchView(Layout.GRID)

    private fun updateCurrentImage(getNewPosition: (Int) -> Int?) {
        val imageView = activity!!.findViewById<SimpleDraweeView>(R.id.imageView)

        val newPosition = getNewPosition(this.position)
        if (newPosition != null) {
            val imageId: Int? = ImageRepository.getImageId(newPosition)
            if (imageId != null) {
                imageView.setImageURI("res:/$imageId")
            }
        }
        activity!!.findViewById<TextView>(R.id.textView).text = newPosition.toString()
        this.position = newPosition!!
    }
}