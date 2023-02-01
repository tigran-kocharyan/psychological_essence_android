package ru.hse.pe.presentation.content.type.test.ui.sheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.hse.pe.App
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.BottomSheetTestBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.type.test.ui.compose.Navigation
import ru.hse.pe.presentation.content.type.test.utils.theme.TestTheme
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject


class TestBottomSheetDialogFragment : BottomSheetDialogFragment(){
    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private lateinit var binding: BottomSheetTestBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val viewModel: ContentViewModel by viewModels {
        ContentViewModelFactory(
            schedulers,
            interactor
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).getAppComponent().inject(this)
    }

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
        showMarkdown(sharedViewModel.quiz.value?.description)
        setContent()
    }

    private fun setContent(){
        binding.apply {
            if(sharedViewModel.quiz.value?.name?.length?.compareTo(20)!! > 0){
                name.text = sharedViewModel.quiz.value?.name?.substring(0, 20) + "..."
            }else{
                name.text = sharedViewModel.quiz.value?.name
            }
            title.text = sharedViewModel.quiz.value?.name
            desc.text = sharedViewModel.quiz.value?.description


            start.setOnClickListener {
                activity?.setContent {
                    TestTheme {
                        Navigation(sharedViewModel, viewModel, viewLifecycleOwner)
                    }
                }
                dismiss()
            }

            close.setOnClickListener{
                dismiss()
            }
        }
    }

    private fun showMarkdown(text: String?) {
        val sentences = text?.split(". ")
        Log.d("content", sentences.toString())
    }
}
