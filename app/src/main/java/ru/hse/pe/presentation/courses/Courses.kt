package ru.hse.pe.presentation.courses

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.Cache.Companion.key
import ru.hse.pe.R
import ru.hse.pe.TopAppBarFragment
import ru.hse.pe.databinding.ActivityCoursesBinding
import ru.hse.pe.presentation.courses.adapter.CourseBigAdapter
import ru.hse.pe.presentation.courses.view.AdvertisementFragment
import ru.hse.pe.presentation.courses.view.CourseFragment
import ru.hse.pe.presentation.courses.viewmodel.CourseViewModel
import ru.hse.pe.utils.Utils.openFragment

class Courses : AppCompatActivity() {
    lateinit var bindingClass: ActivityCoursesBinding
    private val adapterBig = CourseBigAdapter()
    private val courseModel: CourseViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        openFragment(R.id.topappbar, TopAppBarFragment.newInstance())
        openFragment(R.id.newCourses, CourseFragment.newInstance())



        openFragment(R.id.advertisement, AdvertisementFragment.newInstance())
        courseModel.key.value = "special"
        openFragment(R.id.special, CourseFragment.newInstance())

        bindingClass.apply {
            rcView.layoutManager = LinearLayoutManager(this@Courses, LinearLayoutManager.HORIZONTAL, false)
            rcView.adapter = adapterBig

            val course1 = Course("Поднять самооценку 1", R.drawable.course_big)
            val course2 = Course("Поднять самооценку 2", R.drawable.course_big)
            val course3 = Course("Поднять самооценку 3", R.drawable.course_big)

            adapterBig.addCourse(course1)
            adapterBig.addCourse(course2)
            adapterBig.addCourse(course3)
        }
    }
}