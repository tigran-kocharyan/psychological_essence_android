package ru.hse.pe.presentation.courses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ru.hse.pe.R
import ru.hse.pe.TopAppBarFragment
import ru.hse.pe.databinding.ActivityCoursesBinding
import ru.hse.pe.presentation.courses.adapter.CourseBigAdapter
import ru.hse.pe.presentation.courses.adapter.CourseSmallAdapter
import ru.hse.pe.presentation.courses.view.AdvertisementFragment
import ru.hse.pe.presentation.courses.view.CourseFragment

class Courses : AppCompatActivity() {
    lateinit var bindingClass: ActivityCoursesBinding
    private val adapterBig = CourseBigAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.topappbar, TopAppBarFragment.newInstance())
            .commit()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.newCourses, CourseFragment.newInstance())
            .commit()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.advertisement, AdvertisementFragment.newInstance())
            .commit()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.special, CourseFragment.newInstance())
            .commit()

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