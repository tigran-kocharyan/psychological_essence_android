package ru.hse.pe.presentation.content.type.test.ui.sheet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.image.ImagesPlugin
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.BottomSheetTestBinding
import ru.hse.pe.presentation.content.type.test.ui.TestContentFragment
import ru.hse.pe.utils.Utils.setCommonAnimations


class TestPreviewFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetTestBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

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

        parseDescription(sharedViewModel.quiz.value?.description)
        setContent()
    }

    private fun setContent() {
        with(binding) {
            if (sharedViewModel.quiz.value?.name?.length?.compareTo(20)!! > 0) {
                name.text = sharedViewModel.quiz.value?.name?.substring(0, 20) + "..."
            } else {
                name.text = sharedViewModel.quiz.value?.name
            }

            title.text = sharedViewModel.quiz.value?.name

            start.setOnClickListener {
                (activity as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .setCommonAnimations()
                    .addToBackStack(null)
                    .add(
                        R.id.fragment_container,
                        TestContentFragment.newInstance(),
                        TestContentFragment.TAG
                    )
                    .commit()
                dismiss()
            }

            close.setOnClickListener {
                dismiss()
            }
        }
    }

    // парсим описание на само описание и инструкции
    private fun parseDescription(text: String?) {
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
        const val TAG = "TestPreviewFragment"
        private val MULTIPLIERS = floatArrayOf(1.5F, 1.17F, 1F, 1F, .83F, .67F)
        private val LINK_COLOR = "#6766D8"

        /**
         * Получение объекта [TestPreviewFragment]
         */
        fun newInstance() = TestPreviewFragment()
    }
}