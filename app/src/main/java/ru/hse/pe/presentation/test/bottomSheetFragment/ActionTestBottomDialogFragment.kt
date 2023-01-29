package ru.hse.pe.presentation.test.bottomSheetFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.hse.pe.databinding.BottomTestLayoutBinding
import ru.hse.pe.presentation.test.Navigation
import ru.hse.pe.presentation.test.TestActivity
import ru.hse.pe.presentation.test.utils.theme.TestTheme


class ActionTestBottomDialogFragment : BottomSheetDialogFragment(), View.OnClickListener{
    private lateinit var bindingClass: BottomTestLayoutBinding

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

        bindingClass.btnStartCourse.setOnClickListener {
            activity?.setContent {
                TestTheme {
                    Navigation(context = activity as TestActivity)
                }
            }
            dismiss()
        }
    }

    override fun onClick(v: View?) {

    }
}


//        bindingClass.apply {
//            rcviewLesson.layoutManager = LinearLayoutManager(bindingClass.root.context, LinearLayoutManager.VERTICAL, false)
//            rcviewLesson.adapter = adapter
//        }