package ru.hse.pe.presentation.test.groupie

import android.graphics.PorterDuff
import android.view.View
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.ItemTestBinding
import ru.hse.pe.databinding.TestContainerBinding

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
    private val title: String,
    private val desc: String,
    private val countQues: Int,
    private val time: Int,
    private val imageId: Int,
    private val onClick: (String) -> Unit,
) : BindableItem<ItemTestBinding>() {
    override fun bind(binding: ItemTestBinding, position: Int) {
        binding.nameItemTest.text = title
        binding.descItemTest.text = desc
        binding.quesItemTest.text = "$countQues вопроса"
        binding.timeItemTest.text = "Время прохождения: $time минут"
        binding.imgItemTest.setImageResource(R.drawable.exampl)

        binding.cardViewCont.setOnClickListener {
            onClick("")
        }

        val colors = listOf(R.color.skin, R.color.red, R.color.blue)
        binding.circleTestItem.setColorFilter(colors[(0..2).random()], PorterDuff.Mode.ADD)
        //context.getDrawable(R.drawable.circle_test_item))
    }

    override fun getLayout() = R.layout.item_test

    override fun initializeViewBinding(view: View): ItemTestBinding {
        return ItemTestBinding.bind(view)
    }
}

