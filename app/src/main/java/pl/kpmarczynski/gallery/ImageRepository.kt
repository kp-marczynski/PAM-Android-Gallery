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

        fun getImageId(position: Int): Int = repo[position]

        fun getNextPosition(position: Int): Int {
            return if (position == repo.size - 1) 0 else position + 1
        }

        fun getPreviousPosition(position: Int): Int {
            return if (position == 0) repo.size - 1 else position - 1
        }
    }
}