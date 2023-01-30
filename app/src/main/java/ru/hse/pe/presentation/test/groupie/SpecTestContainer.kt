package ru.hse.pe.presentation.test.groupie

import android.view.View
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.SpecItemTestBinding
import ru.hse.pe.databinding.SpecTestContainerBinding
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.utils.callback.ContentClickListener

class SpecTestContainer(
    private val name: String,
    private val items: List<BindableItem<*>>
) : BindableItem<SpecTestContainerBinding>() {

    override fun bind(binding: SpecTestContainerBinding, position: Int) {
        binding.titleTest.text = name
        binding.recyclerView.adapter = GroupieAdapter().apply { addAll(items) }
    }

    override fun getLayout() = R.layout.spec_test_container

    override fun initializeViewBinding(view: View): SpecTestContainerBinding {
        return SpecTestContainerBinding.bind(view)
    }
}


class SpecTestItem(
    private val test: QuizEntity,
//    private val title: String,
//    private val countQues: Int,
//    private val time: Int,
//    private val imageId: Int,
    private val clickListener: ContentClickListener
) : BindableItem<SpecItemTestBinding>() {
    override fun bind(binding: SpecItemTestBinding, position: Int) {
        binding.specNameTest.text = test.name
        binding.specTestQues.text = "${test.questions.size} вопроса"
        binding.specTestTime.text = "${test.time} минут"
        binding.specImageTest.setImageResource(R.drawable.exampl)

//        binding.cardViewCont.setOnClickListener {
//            onClick("")
//        }
    }

    override fun getLayout() = R.layout.spec_item_test

    override fun initializeViewBinding(view: View): SpecItemTestBinding {
        return SpecItemTestBinding.bind(view)
    }
}





