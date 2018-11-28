package pl.kpmarczynski.gallery.repo

import pl.kpmarczynski.gallery.R

class ImageRepository {
    companion object {

        private val repo = listOf<Int>(
            R.drawable.emi1,
            R.drawable.emi2,
            R.drawable.emi3,
            R.drawable.emi4,
            R.drawable.emi5,
            R.drawable.emi6,
            R.drawable.emi7,
            R.drawable.emi8,
            R.drawable.emi9,
            R.drawable.emi10,
            R.drawable.emi11,
            R.drawable.emi12,
            R.drawable.emi13,
            R.drawable.emi14,
            R.drawable.emi15,
            R.drawable.emi16,
            R.drawable.emi17,
            R.drawable.emi18,
            R.drawable.emi19
        )

        fun getCount(): Int = repo.size

        fun getImageId(position: Int): Int? = if (position >= 0 && position < repo.size) repo[position] else null

        fun getNextPosition(position: Int): Int? =
            if (position >= 0 && position < repo.size) (if (position == repo.size - 1) 0 else position + 1) else null

        fun getPreviousPosition(position: Int): Int? =
            if (position >= 0 && position < repo.size) (if (position == 0) repo.size - 1 else position - 1) else null
    }
}