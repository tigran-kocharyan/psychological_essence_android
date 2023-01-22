package ru.hse.pe.presentation.courses

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.TopAppBarFragment
import ru.hse.pe.databinding.ActivityCoursesBinding
import ru.hse.pe.databinding.MainCourseContainerBinding
import ru.hse.pe.presentation.courses.adapter.CourseBigAdapter
import ru.hse.pe.presentation.courses.groupie.courseBig.CourseBigItem
import ru.hse.pe.presentation.courses.groupie.courseSmall.CourseSmall
import ru.hse.pe.presentation.courses.groupie.courseSmall.CourseSmallItem
import ru.hse.pe.presentation.courses.groupie.MainCourseContainer
import ru.hse.pe.presentation.courses.viewmodel.CourseViewModel
import ru.hse.pe.presentation.courses.viewmodel.TopAbbBarViewModel
import ru.hse.pe.utils.Utils.openFragment

class Courses : AppCompatActivity() {
    lateinit var bindingClass: ActivityCoursesBinding
    private val adapterBig = CourseBigAdapter()
    private val courseModel: CourseViewModel by viewModels()
    private val topAppBarModel: TopAbbBarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        openFragment(R.id.topappbar, TopAppBarFragment.newInstance())

        val courses = listOf(getAllCourses())
        bindingClass.courseContainter.adapter = GroupieAdapter().apply { addAll(courses) }
        topAppBarModel.title.value = getString(R.string.courses)
    }

    private fun getAllCourses(): BindableItem<MainCourseContainerBinding> {
        return MainCourseContainer(
            listOf(
                CourseBigItem(
                    "Поднять самооценку",
                    R.drawable.course_big
                ),
                CourseBigItem(
                    "Обрести спокойствие",
                    R.drawable.course_big
                ),
                CourseBigItem(
                    "Избавиться от токсиков",
                    R.drawable.course_big
                )
            ),
            listOf(
                CourseSmall(
                    "Новинки", "Смотреть все", ::onItemClick,
                    listOf(
                        CourseSmallItem(
                            "Кто я и чего я не хочу?",
                            R.drawable.course_small1
                        ),
                        CourseSmallItem(
                            "Как поднять самооценку",
                            R.drawable.course_small2
                        ),
                        CourseSmallItem(
                            "Техника бей-беги",
                            R.drawable.course_small3
                        ),
                    )
                )
            ),
            listOf(
                CourseSmall(
                    "Специально для вас", "Смотреть все", ::onItemClick,
                    listOf(
                        CourseSmallItem(
                            "Кто я и чего хочу? Определяем ценности",
                            R.drawable.course_small1
                        ),
                        CourseSmallItem(
                            "Кто я и чего хочу? Определяем ценности",
                            R.drawable.course_small2
                        ),
                        CourseSmallItem(
                            "Кто я и чего хочу? Определяем ценности",
                            R.drawable.course_small3
                        ),
                    )
                )
            )
        )
    }

    private fun onItemClick(url: String) {
        Log.d("click", "clicked")
    }
}


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        bindingClass = ActivityCoursesBinding.inflate(layoutInflater)
//        setContentView(bindingClass.root)
//
//        openFragment(R.id.topappbar, TopAppBarFragment.newInstance())
//
//        val courses = listOf(getAllCourses())
//        bindingClass.courseContainter.adapter = GroupieAdapter().apply { addAll(courses) }
//
//        openFragment(R.id.newCourses, CourseFragment.newInstance())
//
//        openFragment(R.id.advertisement, AdvertisementFragment.newInstance())
//        courseModel.key.value = "special"
//        openFragment(R.id.special, CourseFragment.newInstance())
//
//        bindingClass.apply {
//            rcView.layoutManager = LinearLayoutManager(this@Courses, LinearLayoutManager.HORIZONTAL, false)
//            rcView.adapter = adapterBig
//
//            val course1 = Course("Поднять самооценку 1", R.drawable.course_big)
//            val course2 = Course("Поднять самооценку 2", R.drawable.course_big)
//            val course3 = Course("Поднять самооценку 3", R.drawable.course_big)
//
//            adapterBig.addCourse(course1)
//            adapterBig.addCourse(course2)
//            adapterBig.addCourse(course3)
//        }
//    }