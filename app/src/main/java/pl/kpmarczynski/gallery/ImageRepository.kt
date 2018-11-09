package pl.kpmarczynski.gallery

class ImageRepository {
    companion object {

        private val repo = listOf<Int>(
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

        fun getCount(): Int = repo.size

        fun getImageId(position: Int): Int? = if (position >= 0 && position < repo.size) repo[position] else null

        fun getNextPosition(position: Int): Int? =
            if (position >= 0 && position < repo.size) (if (position == repo.size - 1) 0 else position + 1) else null

        fun getPreviousPosition(position: Int): Int? =
            if (position >= 0 && position < repo.size) (if (position == 0) repo.size - 1 else position - 1) else null
    }
}