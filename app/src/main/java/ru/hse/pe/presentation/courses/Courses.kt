package ru.hse.pe.presentation.courses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ru.hse.pe.R
import ru.hse.pe.TopAppBarFragment
import ru.hse.pe.databinding.ActivityCoursesBinding

class Courses : AppCompatActivity() {
    lateinit var bindingClass: ActivityCoursesBinding
    private val adapter = CourseAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.topappbar, TopAppBarFragment.newInstance())
            .commit()

        bindingClass.apply {
            rcView.layoutManager = LinearLayoutManager(this@Courses, LinearLayoutManager.HORIZONTAL, false)
            rcView.adapter = adapter

            val course1 = Course("Поднять самооценку 1", R.drawable.course_big)
            val course2 = Course("Поднять самооценку 2", R.drawable.course_big)
            val course3 = Course("Поднять самооценку 3", R.drawable.course_big)

            adapter.addCourse(course1)
            adapter.addCourse(course2)
            adapter.addCourse(course3)
        }
    }
}