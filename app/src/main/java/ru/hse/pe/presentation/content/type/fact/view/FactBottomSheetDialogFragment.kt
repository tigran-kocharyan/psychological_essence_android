package ru.hse.pe.presentation.content.type.fact.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.image.ImagesPlugin
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.BottomSheetFactBinding
import ru.hse.pe.presentation.MainActivity


class FactBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetFactBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).isBottomNavVisible(false)
        showMarkdown(sharedViewModel.fact.value?.content)
    }

    private fun showMarkdown(text: String?) {
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
                .usePlugin(ImagesPlugin.create())
                .build()
            markwon.setMarkdown(binding.markdownArticle, text ?: "")
        }
    }

    companion object {
        const val TAG = "ArticleFragment"
        private val MULTIPLIERS = floatArrayOf(1.5F, 1.17F, 1F, 1F, .83F, .67F)
        private const val LINK_COLOR = "#6766D8"

        /**
         * Получение объекта [FactBottomSheetDialogFragment]
         */
        fun newInstance(): FactBottomSheetDialogFragment {
            return FactBottomSheetDialogFragment()
        }
    }
}