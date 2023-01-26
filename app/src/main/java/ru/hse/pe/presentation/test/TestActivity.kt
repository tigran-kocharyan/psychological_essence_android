package ru.hse.pe.presentation.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.hse.pe.R
import ru.hse.pe.TopAppBarFragment
import ru.hse.pe.databinding.ActivityTestBinding
import ru.hse.pe.domain.model.TestItem
import ru.hse.pe.presentation.courses.viewmodel.TopAbbBarViewModel
import ru.hse.pe.utils.Utils.openFragment


class TestActivity : AppCompatActivity() {
    public lateinit var testItem: TestItem
    private lateinit var binding: ActivityTestBinding
    private val topAppBarModel: TopAbbBarViewModel by viewModels()
    val context = this@TestActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        openFragment(R.id.topappbar, TopAppBarFragment.newInstance())
        topAppBarModel.title.value = getString(R.string.tests)
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











