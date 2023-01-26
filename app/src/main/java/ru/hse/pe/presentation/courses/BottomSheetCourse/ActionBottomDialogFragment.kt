package ru.hse.pe.presentation.courses.BottomSheetCourse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.hse.pe.databinding.BottomCourseLayoutBinding
import ru.hse.pe.presentation.courses.lesson.Lesson
import ru.hse.pe.presentation.courses.lesson.LessonData
import ru.hse.pe.presentation.courses.lesson.LessonAdapter
import java.lang.RuntimeException

class ActionBottomDialogFragment : BottomSheetDialogFragment(), View.OnClickListener{
    private var mListener: ItemClickListener? = null
    private lateinit var bindingClass: BottomCourseLayoutBinding
    private val adapter = LessonAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingClass = BottomCourseLayoutBinding.inflate(inflater, container, false)
        return bindingClass.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bindingClass.apply {
            rcviewLesson.layoutManager = LinearLayoutManager(bindingClass.root.context, LinearLayoutManager.VERTICAL, false)
            rcviewLesson.adapter = adapter

            val lesson1 = LessonData("Грани моего Я. Из чего складывается наше представление о себе",
                "Выделите качества своей личности и сильные черты, которые помогут реализоваться и достичь целей. Познакомитесь с моделью личности Big Five. Узнаете, как черты личности влияют на поведение и общение с окружающими.")
            val lesson2 = LessonData("Поднять самооценку 2", "sdasad")
            val lesson3 = LessonData("Поднять самооценку 3", "sadsadsadas")

            adapter.addLesson(lesson1)
            adapter.addLesson(lesson1)
            adapter.addLesson(lesson1)
        }

        bindingClass.btnStartCourse.setOnClickListener {
            val intent = Intent(context, Lesson::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mListener = if (context is ItemClickListener){
            context
        }else {
            throw RuntimeException(
                "$context Must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}