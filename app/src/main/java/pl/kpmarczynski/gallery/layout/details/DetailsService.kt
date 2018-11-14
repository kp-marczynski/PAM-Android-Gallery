package pl.kpmarczynski.gallery.layout.details

import android.view.View
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import pl.kpmarczynski.gallery.ImageRepository
import pl.kpmarczynski.gallery.MainActivity
import pl.kpmarczynski.gallery.R
import pl.kpmarczynski.gallery.layout.AbstractLayoutService
import pl.kpmarczynski.gallery.layout.Layout

class DetailsService(activity: MainActivity) : AbstractLayoutService(
    activity,
    Layout.DETAILS
) {

    override fun setupLayout(position: Int) {
        activity.setContentView(layout.value)
        this.position = position
        updateCurrentImage { position }
    }

    override fun handleButtonClick(view: View) {
        when (view.id) {
            R.id.homeButton -> onHomeButtonClick(view)
            R.id.nextButton -> onNextButtonClick(view)
            R.id.previousButton -> onPreviousButtonClick(view)
        }
    }

    override fun onBackPressed() = switchView()

    private fun updateCurrentImage(getNewPosition: (Int) -> Int?) {
        val imageView = activity.findViewById<SimpleDraweeView>(R.id.imageView)

        val newPosition = getNewPosition(this.position)
        if (newPosition != null) {
            val imageId: Int? = ImageRepository.getImageId(newPosition)
            if (imageId != null) {
                imageView.setImageURI("res:/$imageId")
            }
        }
        activity.findViewById<TextView>(R.id.textView).text = newPosition.toString()
        this.position = newPosition!!
    }

    private fun onPreviousButtonClick(view: View) = updateCurrentImage(ImageRepository.Companion::getPreviousPosition)

    private fun onNextButtonClick(view: View) = updateCurrentImage(ImageRepository.Companion::getNextPosition)

    private fun onHomeButtonClick(view: View) = switchView()
}