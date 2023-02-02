package ru.hse.pe.presentation.content.type.test.ui.sheet

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.image.ImagesPlugin
import org.commonmark.internal.ListItemParser
import org.commonmark.node.BulletList
import ru.hse.pe.App
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.BottomSheetTestBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.type.article.view.ArticleFragment
import ru.hse.pe.presentation.content.type.fact.view.FactBottomSheetDialogFragment
import ru.hse.pe.presentation.content.type.test.ui.TestsFragment
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
        parseDesc(sharedViewModel.quiz.value?.description)
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

    // парсим описание на само описание и инструкции
    private fun parseDesc(text: String?) {
        val sentences = text?.split("Инструкция:")

        context?.let { context ->
            val markwon = Markwon.builder(context)
                .usePlugin(object : AbstractMarkwonPlugin() {
                    override fun configureTheme(builder: MarkwonTheme.Builder) {
                        builder
                            .codeTextColor(Color.BLACK)
                            .codeBackgroundColor(Color.GREEN)
                            .headingTextSizeMultipliers(MULTIPLIERS)
                            .headingBreakHeight(0)
                            .linkColor(Color.parseColor(LINK_COLOR))

                    }
                })
                .usePlugin(SoftBreakAddsNewLinePlugin.create())
                .usePlugin(ImagesPlugin.create())
                .build()
            markwon.setMarkdown(binding.instruction, sentences?.get(1) ?: "")
        }

        binding.desc.text = sentences?.get(0) ?: ""
    }

    companion object {
        const val TAG = "TestBottomSheetDialogFragment"
        private val MULTIPLIERS = floatArrayOf(1.5F, 1.17F, 1F, 1F, .83F, .67F)
        private val LINK_COLOR = "#6766D8"

        /**
         * Получение объекта [TestBottomSheetDialogFragment]
         */
        fun newInstance() = TestBottomSheetDialogFragment()
    }
}


/*

 val desc = sentences[0]
        val instruction = sentences[1].split("(варианты ответов)")

        // варианты ответов
        val answers = instruction[1].split("\n\t").toList().toMutableList()
        var answersStr = StringBuilder()
        for (i in (answers.indices) - 1){
            answersStr.append("\t•${answers[i]}\n")
        }

        Log.d("answers", answers.toString())

        binding.desc.text = desc
        binding.instruction.text = sentences[1]
 */