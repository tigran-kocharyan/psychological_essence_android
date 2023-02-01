package ru.hse.pe.presentation.test.bottomSheetFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_test.*
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.BottomSheetTestBinding
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.test.Navigation
import ru.hse.pe.presentation.test.utils.theme.TestTheme


class TestBottomSheetDialogFragment : BottomSheetDialogFragment(), View.OnClickListener{
    private lateinit var binding: BottomSheetTestBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).isBottomNavVisible(false)
        showMarkdown(sharedViewModel.fact.value?.content)
        setContent()
    }

    private fun setContent(){
        binding.apply {
            Log.d("test", sharedViewModel.quiz.value.toString())
            if(sharedViewModel.quiz.value?.name?.length?.compareTo(20)!! > 0){
                tvName.text = sharedViewModel.quiz.value?.name?.substring(0, 20) + "..."
            }else{
                tvName.text = sharedViewModel.quiz.value?.name
            }
            tvTitle.text = sharedViewModel.quiz.value?.name
            tvContent.text = sharedViewModel.quiz.value?.description


            btnStartCourse.setOnClickListener {
                activity?.setContent {
                    TestTheme {
                        Navigation(sharedViewModel)
                    }
                }
                dismiss()
            }

            image_close.setOnClickListener{
                dismiss()
            }
        }
    }

    private fun showMarkdown(text: String?) {
    }

    override fun onClick(v: View?) {

    }
}
