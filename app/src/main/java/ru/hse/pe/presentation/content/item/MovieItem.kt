package ru.hse.pe.presentation.content.item

import android.view.View
import coil.load
import coil.transform.RoundedCornersTransformation
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderMovieBinding
import ru.hse.pe.domain.model.RecommendationEntity
import ru.hse.pe.utils.callback.ContentClickListener

class MovieItem(
    private val movie: RecommendationEntity,
    private val clickListener: ContentClickListener
) : BindableItem<HolderMovieBinding>() {

    override fun getLayout() = R.layout.holder_movie

    override fun initializeViewBinding(view: View): HolderMovieBinding {
        return HolderMovieBinding.bind(view)
    }

    override fun bind(binding: HolderMovieBinding, position: Int) {
        binding.root.setOnClickListener { clickListener.onContentClick(movie, position) }
        if (movie.imageUrls.isNotEmpty()) {
            binding.image.load(movie.imageUrls[0]) {
                crossfade(true)
                error(R.drawable.placeholder_movie)
                placeholder(R.drawable.placeholder_movie)
                transformations(RoundedCornersTransformation(20f))
            }
        } else {
            binding.image.load(R.drawable.placeholder_movie) {
                transformations(RoundedCornersTransformation(20f))
            }
        }
        binding.title.text = movie.title
        binding.duration.text = movie.duration
    }
}
