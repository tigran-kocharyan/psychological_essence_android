package ru.hse.pe.presentation.test.groupie

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.SpecialItemTestBinding
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.utils.callback.ContentClickListener

// Special Test Item (SpecTestItem) - тест, специально подобранный под предпочтения пользователя
class SpecTestItem(
    private val test: QuizEntity,
    private val clickListener: ContentClickListener
) : BindableItem<SpecialItemTestBinding>() {
    override fun bind(binding: SpecialItemTestBinding, position: Int) {
        binding.name.text = test.name
        binding.countQuestions.text = "${test.questions.size} вопроса"
        binding.time.text = "${test.time} минут"
        binding.image.setImageResource(R.drawable.placeholder_article)

        binding.root.setOnClickListener { clickListener.onContentClick(test, position) }
    }

    override fun getLayout() = R.layout.special_item_test

    override fun initializeViewBinding(view: View): SpecialItemTestBinding {
        return SpecialItemTestBinding.bind(view)
    }
}





