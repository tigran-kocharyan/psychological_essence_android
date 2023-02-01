package ru.hse.pe.presentation.test.groupie

import android.graphics.PorterDuff
import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.ItemTestBinding
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.utils.callback.ContentClickListener


class TestItem(
    private val test: QuizEntity,
    private val clickListener: ContentClickListener,
) : BindableItem<ItemTestBinding>() {
    override fun bind(binding: ItemTestBinding, position: Int) {
        binding.name.text = test.name
        binding.desc.text = test.description
        binding.countQuestions.text = "${test.questions.size} вопроса"
        binding.time.text = "Время прохождения: ${test.time} минут"
        binding.image.setImageResource(R.drawable.placeholder_article)

        val colors = listOf(R.color.skin, R.color.red, R.color.blue)
        binding.circle.setColorFilter(colors[(0..2).random()], PorterDuff.Mode.ADD)

        binding.root.setOnClickListener { clickListener.onContentClick(test, position) }
    }

    override fun getLayout() = R.layout.item_test

    override fun initializeViewBinding(view: View): ItemTestBinding {
        return ItemTestBinding.bind(view)
    }
}

