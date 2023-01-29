package ru.hse.pe.presentation.test

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.TopAppBarFragment
import ru.hse.pe.databinding.*
import ru.hse.pe.domain.model.TestItem
import ru.hse.pe.presentation.courses.viewmodel.TopAbbBarViewModel
import ru.hse.pe.presentation.test.bottomSheetFragment.ActionTestBottom
import ru.hse.pe.presentation.test.utils.theme.TestTheme
import ru.hse.pe.utils.Utils.openFragment


class TestActivity : AppCompatActivity() {
    private lateinit var testItem: TestItem
    private lateinit var binding: ActivityTestBinding
    private val topAppBarModel: TopAbbBarViewModel by viewModels()
    val context = this@TestActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tests = listOf(getSpecTests(), getAllTests())
        binding.rcView.adapter = GroupAdapter<GroupieViewHolder>().apply { addAll(tests) }

        openFragment(R.id.topappbar, TopAppBarFragment.newInstance())
        topAppBarModel.title.value = getString(R.string.tests)

    }

    private fun getSpecTests(): BindableItem<SpecTestContainerBinding> {
        return SpecTestContainer(
            getString(R.string.specCourses),
            listOf(
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick,
                ),
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick
                ),
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick
                ),
            ),
        )
    }

    private fun getAllTests(): BindableItem<TestContainerBinding> {
        return TestContainer(
            getString(R.string.tests),
            listOf(
                TestItem(
                    "Тест на определение типа личности",
                    getString(R.string.tempLorem),
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick,
                ),
                TestItem(
                    "Тест на определение типа личности",
                    getString(R.string.tempLorem),
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick,
                ),
                TestItem(
                    "Тест на определение типа личности",
                    getString(R.string.tempLorem),
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick,
                ),
            ),
        )
    }

    private fun onTestClick(url: String) {

        val bottomDialogFrag = ActionTestBottom.newInstance()
        bottomDialogFrag.show(
            supportFragmentManager, ActionTestBottom.TAG
        )
    }
}

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



class SpecTestItem(
    private val title: String,
    private val countQues: Int,
    private val time: Int,
    private val imageId: Int,
    private val onClick: (String) -> Unit,
) : BindableItem<SpecItemTestBinding>() {
    override fun bind(binding: SpecItemTestBinding, position: Int) {
        binding.specNameTest.text = title
        binding.specTestQues.text = "$countQues вопроса"
        binding.specTestTime.text = "$time минут"
        binding.specImageTest.setImageResource(R.drawable.exampl)

        binding.cardViewCont.setOnClickListener {
            onClick("")
        }
    }

    override fun getLayout() = R.layout.spec_item_test

    override fun initializeViewBinding(view: View): SpecItemTestBinding {
        return SpecItemTestBinding.bind(view)
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





