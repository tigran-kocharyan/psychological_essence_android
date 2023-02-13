package ru.hse.pe.presentation.content.type.courses.sheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.hse.pe.databinding.BottomCourseLayoutBinding
import ru.hse.pe.presentation.content.type.courses.lesson.Lesson
import ru.hse.pe.presentation.content.type.courses.lesson.LessonData
import ru.hse.pe.presentation.content.type.courses.lesson.LessonAdapter

class CoursePreviewFragment : BottomSheetDialogFragment(), View.OnClickListener{
    private lateinit var binding: BottomCourseLayoutBinding
    private val adapter = LessonAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomCourseLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            rcviewLesson.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            rcviewLesson.adapter = adapter

            val lesson1 = LessonData("Грани моего Я. Из чего складывается наше представление о себе",
                "Выделите качества своей личности и сильные черты, которые помогут реализоваться и достичь целей. Познакомитесь с моделью личности Big Five. Узнаете, как черты личности влияют на поведение и общение с окружающими.")

            adapter.addLesson(lesson1)
            adapter.addLesson(lesson1)
            adapter.addLesson(lesson1)
        }

        binding.btnStartCourse.setOnClickListener {
            val intent = Intent(context, Lesson::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
    }
}