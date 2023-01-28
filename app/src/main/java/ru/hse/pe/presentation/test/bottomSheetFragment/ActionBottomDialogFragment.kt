package ru.hse.pe.presentation.test.bottomSheetFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.hse.pe.databinding.BottomTestLayoutBinding

import java.lang.RuntimeException


class ActionBottomDialogFragment : BottomSheetDialogFragment(), View.OnClickListener{
    private var mListener: ItemClickListenerTest? = null
    private lateinit var bindingClass: BottomTestLayoutBinding
   // private val adapter = LessonAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingClass = BottomTestLayoutBinding.inflate(inflater, container, false)
        return bindingClass.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        bindingClass.apply {
//            rcviewLesson.layoutManager = LinearLayoutManager(bindingClass.root.context, LinearLayoutManager.VERTICAL, false)
//            rcviewLesson.adapter = adapter
//        }

        bindingClass.btnStartCourse.setOnClickListener {
          //  val intent = Intent(context, Lesson::class.java)
          //  startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

//        mListener = if (context is ItemClickListenerTest){
//            context
//        }else {
//            throw RuntimeException(
//                "$context Must implement ItemClickListener"
//            )
//        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}