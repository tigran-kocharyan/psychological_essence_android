package ru.hse.pe.presentation.courses.BottomSheetCourse

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.hse.pe.R
import java.lang.RuntimeException

class ActionBottomDialogFragment : BottomSheetDialogFragment(), View.OnClickListener{
    private var mListener: ItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_course_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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