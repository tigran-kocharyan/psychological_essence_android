package ru.hse.pe.presentation.test

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.TopAppBarFragment
import ru.hse.pe.databinding.ActivityTestBinding
import ru.hse.pe.databinding.SpecItemTestBinding
import ru.hse.pe.databinding.TestContainerBinding
import ru.hse.pe.domain.model.TestItem
import ru.hse.pe.presentation.courses.viewmodel.TopAbbBarViewModel
import ru.hse.pe.utils.Utils.openFragment
import java.util.Collections.addAll


class TestActivity : AppCompatActivity() {
    public lateinit var testItem: TestItem
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


    private fun getSpecTests(): BindableItem<TestContainerBinding> {
        return TestContainter(
            getString(R.string.specCourses), ::onTestClick,
            listOf(
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl
                ),
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl
                ),
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl
                ),
            ),
        )
    }

    private fun getAllTests(): BindableItem<TestContainerBinding> {
        return TestContainter(
            getString(R.string.tests), ::onTestClick,
            listOf(
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl
                ),
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl
                ),
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl
                ),
            ),
        )
    }

    private fun onTestClick(url: String) {

    }
}

class TestContainter(
    private val name: String,
    private val onClick: (url: String) -> Unit,
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
) : BindableItem<SpecItemTestBinding>() {
    override fun bind(binding: SpecItemTestBinding, position: Int) {
        binding.specNameTest.text = title
        binding.specTestQues.text = "$countQues вопроса"
        binding.specTestTime.text = "$time минут"
        binding.specImageTest.setImageResource(R.drawable.exampl)
    }

    override fun getLayout() = R.layout.spec_item_test

    override fun initializeViewBinding(view: View): SpecItemTestBinding {
        return SpecItemTestBinding.bind(view)
    }
}

class testItem(
    private val title: String,
    private val desc: String,
    private val countQues: Int,
    private val time: Int,
    private val imageId: Int,
) : BindableItem<SpecItemTestBinding>() {
    override fun bind(binding: SpecItemTestBinding, position: Int) {
        binding.specNameTest.text = title
        binding.specTestQues.text = "$countQues вопроса"
        binding.specTestTime.text = "$time минут"
        binding.specImageTest.setImageResource(R.drawable.exampl)
    }

    override fun getLayout() = R.layout.item_test

    override fun initializeViewBinding(view: View): SpecItemTestBinding {
        return SpecItemTestBinding.bind(view)
    }
}





//class TestActivity : ComponentActivity() {
//    public lateinit var testItem: TestItem
//    val context = this@TestActivity
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            TestTheme {
//                Navigation(context)
//            }
//        }
//    }
//}

/*
package ru.hse.pe.presentation.courses.allCourses

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.TopAppBarFragment
import ru.hse.pe.databinding.ActivityAllCoursesBinding
import ru.hse.pe.databinding.AllCoursesContainerBinding
import ru.hse.pe.databinding.AllCoursesItemBinding
import ru.hse.pe.presentation.courses.BottomSheetCourse.ActionBottomCourse
import ru.hse.pe.presentation.courses.BottomSheetCourse.ItemClickListener
import ru.hse.pe.presentation.courses.viewmodel.TopAbbBarViewModel
import ru.hse.pe.utils.Utils.openFragment

class AllCourses : AppCompatActivity(), ItemClickListener {
    private lateinit var binding: ActivityAllCoursesBinding
    private val dataModel: TopAbbBarViewModel by viewModels()

    // Экран со списком курсов (при нажатии на кнопку смотреть все)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val courses = listOf(getAllCourses())
        binding.rcview.adapter = GroupieAdapter().apply { addAll(courses) }

        openFragment(R.id.topappbar, TopAppBarFragment.newInstance())
        dataModel.title.value = getString(R.string.newCourses)

    }

    private fun onCourseClick(url: String) {
        val bottomDialogFrag = ActionBottomCourse.newInstance()
        bottomDialogFrag.show(
            supportFragmentManager, ActionBottomCourse.TAG
        )
    }


    private fun getAllCourses(): BindableItem<AllCoursesContainerBinding> {
        return AllCoursesContainer(
            listOf(
                AllCoursesItem(
                    "Кто я и чего хочу? Определяем ценности",
                    1,
                    R.drawable.course_small1,
                    ::onCourseClick,
                ),
                AllCoursesItem(
                    "Как наладить контакст с собою",
                    2,
                    R.drawable.course_small2,
                    ::onCourseClick,
                ),
                AllCoursesItem(
                    "Кто я и чего хочу? Определяем ценности",
                    8,
                    R.drawable.course_small3,
                    ::onCourseClick,
                ),
                AllCoursesItem(
                    "Выгорание: как вернуть интерес к работе и жизни",
                    3,
                    R.drawable.course_small1,
                    ::onCourseClick,
                ),
            ),
        )
    }

    override fun onItemClick(item: String?) {
        //
    }


}

class AllCoursesContainer(
    private val items: List<BindableItem<*>>
) : BindableItem<AllCoursesContainerBinding>() {
    override fun bind(binding: AllCoursesContainerBinding, position: Int) {
        binding.recyclerView.adapter = GroupieAdapter().apply { addAll(items) }
    }

    override fun getLayout() = R.layout.all_courses_container

    override fun initializeViewBinding(view: View): AllCoursesContainerBinding {
        return AllCoursesContainerBinding.bind(view)
    }
}

class AllCoursesItem(
    private val title: String,
    private val duration: Int,
    private val imgId: Int,
    private val onClick: (url: String) -> Unit,
) : BindableItem<AllCoursesItemBinding>() {

    override fun bind(binding: AllCoursesItemBinding, position: Int) {
        binding.titleAllCoursesItem.text = title

        binding.consLayoutAllCourses.setOnClickListener {
            onClick("")
        }

        when (duration) {
            1 -> binding.durAllICoursesItem.text = "Длительность: $duration месяц"
            in 2..4 -> {
                binding.durAllICoursesItem.text = "Длительность: $duration месяца"
            }
            else -> {
                binding.durAllICoursesItem.text = "Длительность: $duration месяцев"
            }
        }
        binding.imageAllCourseItem.setImageResource(imgId)
    }

    override fun getLayout() = R.layout.all_courses_item

    override fun initializeViewBinding(view: View): AllCoursesItemBinding {
        return AllCoursesItemBinding.bind(view)
    }
}


 */











