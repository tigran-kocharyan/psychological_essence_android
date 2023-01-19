package ru.hse.pe.presentation.courses.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.hse.pe.R
import ru.hse.pe.databinding.FragmentCourseSmallBinding
import ru.hse.pe.presentation.courses.Course
import ru.hse.pe.presentation.courses.adapter.CourseSmallAdapter
import ru.hse.pe.presentation.courses.viewmodel.CourseViewModel


class CourseFragment : Fragment() {
    private lateinit var binding: FragmentCourseSmallBinding
    private val adapterSmall = CourseSmallAdapter()
    private val courseModel: CourseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseSmallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rcSmallView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rcSmallView.adapter = adapterSmall
        }

        var key = "newCourses"

        Log.d("keysda", key)
        when (key) {
            "newCourses" -> {
                binding.nameNewCourses.text = getString(R.string.newCourses)

                val course1 = Course("Кто я и чего хочу? Определяем ценности", R.drawable.course_small1)
                val course2 = Course("Как наладить контакт с миром и с собой", R.drawable.course_small2)
                val course3 = Course("Выгорание: как вернуть интерес к работе и жизни", R.drawable.course_small3)


                adapterSmall.addCourse(course1)
                adapterSmall.addCourse(course2)
                adapterSmall.addCourse(course3)
            }
            "special" -> {
                binding.nameNewCourses.text = getString(R.string.specCourses)

                val course1 = Course("Как выйти из депрессии", R.drawable.course_small1)
                val course2 = Course("Повышаем самооценку", R.drawable.course_small2)
                val course3 = Course("Как повысить свой тонус", R.drawable.course_small3)


                adapterSmall.addCourse(course1)
                adapterSmall.addCourse(course2)
                adapterSmall.addCourse(course3)
            }
        }



    }

    companion object {
        @JvmStatic
        fun newInstance() = CourseFragment()
    }
}