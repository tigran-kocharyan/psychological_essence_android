package ru.hse.pe.presentation.content.item

import android.view.View
import coil.load
import coil.transform.RoundedCornersTransformation
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderSpecialTestBinding
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.utils.callback.ContentClickListener

/**
 * Special Test Item (SpecialTestItem) - тест, специально подобранный под предпочтения пользователя
 */
class SpecialTestItem(
    private val test: QuizEntity,
    private val clickListener: ContentClickListener
) : BindableItem<HolderSpecialTestBinding>() {
    override fun bind(binding: HolderSpecialTestBinding, position: Int) {
        binding.name.text = test.name
        binding.countQuestions.text = "${test.questions.size} вопроса"
        binding.time.text = "${test.time} минут"
        if (test.imageUrl != null && test.imageUrl.isNotBlank()) {
            binding.image.load(test.imageUrl) {
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
        binding.root.setOnClickListener { clickListener.onContentClick(test, position) }
    }

    override fun getLayout() = R.layout.holder_special_test

    override fun initializeViewBinding(view: View) = HolderSpecialTestBinding.bind(view)
}





