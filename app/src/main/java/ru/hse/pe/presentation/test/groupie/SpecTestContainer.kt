package ru.hse.pe.presentation.test.groupie

import android.view.View
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.SpecialItemTestBinding
import ru.hse.pe.databinding.SpecialTestContainerBinding
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.utils.callback.ContentClickListener


// Special Test Container (SpecTestContainer) - специально для вас
// (контейнер где будут показываться подобранные тесты для пользователя
class SpecTestContainer(
    private val name: String,
    private val items: List<BindableItem<*>>
) : BindableItem<SpecialTestContainerBinding>() {

    override fun bind(binding: SpecialTestContainerBinding, position: Int) {
        binding.title.text = name
        binding.recyclerView.adapter = GroupieAdapter().apply { addAll(items) }
    }

    override fun getLayout() = R.layout.special_test_container

    override fun initializeViewBinding(view: View): SpecialTestContainerBinding {
        return SpecialTestContainerBinding.bind(view)
    }
}

// Special Test Item (SpecTestItem) - соответственно сам элемент
class SpecTestItem(
    private val test: QuizEntity,
    private val clickListener: ContentClickListener
) : BindableItem<SpecialItemTestBinding>() {
    override fun bind(binding: SpecialItemTestBinding, position: Int) {
        binding.name.text = test.name
        binding.countQuestions.text = "${test.questions.size} вопроса"
        binding.time.text = "${test.time} минут"
        binding.image.setImageResource(R.drawable.exampl)

        binding.root.setOnClickListener { clickListener.onContentClick(test, position) }
    }

    override fun getLayout() = R.layout.special_item_test

    override fun initializeViewBinding(view: View): SpecialItemTestBinding {
        return SpecialItemTestBinding.bind(view)
    }
}





