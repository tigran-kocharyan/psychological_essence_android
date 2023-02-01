package ru.hse.pe.presentation.test.groupie

import android.graphics.PorterDuff
import android.view.View
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.ItemTestBinding
import ru.hse.pe.databinding.TestContainerBinding
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.utils.callback.ContentClickListener

class TestContainer(
    private val name: String,
    private val items: List<BindableItem<*>>
) : BindableItem<TestContainerBinding>() {
    override fun bind(binding: TestContainerBinding, position: Int) {
        binding.titleTest.text = name
        binding.recyclerView.adapter = GroupieAdapter().apply { addAll(items) }
    }
    override fun getLayout() = R.layout.test_container

    override fun initializeViewBinding(view: View): TestContainerBinding {
        return TestContainerBinding.bind(view)
    }
}

class TestItem(
    private val test: QuizEntity,
    private val clickListener: ContentClickListener,
) : BindableItem<ItemTestBinding>() {
    override fun bind(binding: ItemTestBinding, position: Int) {
        binding.nameItemTest.text = test.name
        binding.descItemTest.text = test.description
        binding.quesItemTest.text = "${test.questions.size} вопроса"
        binding.timeItemTest.text = "Время прохождения: ${test.time} минут"
        binding.imgItemTest.setImageResource(R.drawable.exampl)

        val colors = listOf(R.color.skin, R.color.red, R.color.blue)
        binding.circleTestItem.setColorFilter(colors[(0..2).random()], PorterDuff.Mode.ADD)
        //context.getDrawable(R.drawable.circle_test_item))

        binding.root.setOnClickListener { clickListener.onContentClick(test, position) }
    }

    override fun getLayout() = R.layout.item_test

    override fun initializeViewBinding(view: View): ItemTestBinding {
        return ItemTestBinding.bind(view)
    }
}

