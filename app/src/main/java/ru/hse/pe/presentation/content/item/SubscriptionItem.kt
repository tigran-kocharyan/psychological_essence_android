package ru.hse.pe.presentation.content.item

import android.view.View
import androidx.core.view.isVisible
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderTechniqueBinding
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.ContentEntity
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.domain.model.RecommendationEntity
import ru.hse.pe.utils.callback.ContentClickListener

class SubscriptionItem(
    private val content: ContentEntity,
    private val clickListener: ContentClickListener
) : BindableItem<HolderTechniqueBinding>() {

    override fun getLayout() = R.layout.holder_technique

    override fun initializeViewBinding(view: View): HolderTechniqueBinding {
        return HolderTechniqueBinding.bind(view)
    }

    override fun bind(binding: HolderTechniqueBinding, position: Int) {
        when (content) {
            is ArticleEntity -> {
                binding.root.setOnClickListener { clickListener.onContentClick(content, position) }
                if (content.imageUrl != null && content.imageUrl.isNotBlank()) {
                    Glide
                        .with(binding.root)
                        .load(content.imageUrl)
                        .transform(CenterCrop(), RoundedCorners(10))
                        .transition(withCrossFade())
                        .error(R.drawable.placeholder_article)
                        .into(binding.image)
                } else {
                    Glide
                        .with(binding.root)
                        .load(R.drawable.placeholder_article)
                        .transform(CenterCrop(), RoundedCorners(10))
                        .transition(withCrossFade())
                        .into(binding.image)
                }
                binding.title.text = content.title
                binding.category.text = content.category
                binding.lock.isVisible = content.requiresSubscription
            }
            is RecommendationEntity -> {
                binding.root.setOnClickListener { clickListener.onContentClick(content, position) }
                if (content.imageUrls.isNotEmpty()) {
                    binding.image.load(content.imageUrls[0]) {
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
                binding.title.text = content.title
                binding.category.text = content.category
                binding.lock.isVisible = content.requiresSubscription
            }
            is QuizEntity -> {
                binding.root.setOnClickListener { clickListener.onContentClick(content, position) }
                if (content.imageUrl != null && content.imageUrl.isNotBlank()) {
                    binding.image.load(content.imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher)
                        error(R.drawable.ic_launcher)
                        transformations(RoundedCornersTransformation(10f))
                    }
                } else {
                    binding.image.load(R.drawable.placeholder_article) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(10f))
                    }
                }
                binding.root.setOnClickListener { clickListener.onContentClick(content, position) }
                binding.title.text = content.name
                binding.category.text = content.category
                binding.lock.isVisible = content.requiresSubscription
            }
        }

    }
}
