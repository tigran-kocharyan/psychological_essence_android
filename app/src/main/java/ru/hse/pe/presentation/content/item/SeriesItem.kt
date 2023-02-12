package ru.hse.pe.presentation.content.item

import android.view.View
import coil.load
import coil.transform.RoundedCornersTransformation
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderSeriesBinding
import ru.hse.pe.domain.model.RecommendationEntity
import ru.hse.pe.utils.callback.ContentClickListener

class SeriesItem(
    private val series: RecommendationEntity,
    private val clickListener: ContentClickListener
) : BindableItem<HolderSeriesBinding>() {

    override fun getLayout() = R.layout.holder_series

    override fun initializeViewBinding(view: View): HolderSeriesBinding {
        return HolderSeriesBinding.bind(view)
    }

    override fun bind(binding: HolderSeriesBinding, position: Int) {
        val context = binding.root.context
        binding.root.setOnClickListener { clickListener.onContentClick(series, position) }
        if (series.imageUrls.isNotEmpty()) {
            binding.image.load(series.imageUrls[0]) {
                crossfade(true)
                placeholder(R.drawable.placeholder_series)
                error(R.drawable.placeholder_series)
                transformations(RoundedCornersTransformation(5f))
            }
        } else {
            binding.image.load(R.drawable.placeholder_series) {
                transformations(RoundedCornersTransformation(5f))
            }
        }
        binding.title.text = series.title
        binding.date.text = context?.getString(R.string.date_recommendation, series.year) ?: ""
        binding.category.text =
            context?.getString(R.string.category_recommendation, series.category) ?: ""
        binding.description.text = series.content
        binding.episodes.text =
            context?.getString(R.string.episodes_recommendation, series.episodes) ?: ""
    }
}
