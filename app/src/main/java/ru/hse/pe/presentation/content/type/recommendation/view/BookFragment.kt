package ru.hse.pe.presentation.content.type.recommendation.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.image.ImagesPlugin
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentBookRecommendationBinding
import ru.hse.pe.domain.model.RecommendationEntity
import ru.hse.pe.presentation.MainActivity


class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookRecommendationBinding
    private lateinit var root: View
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = binding.root
        (activity as MainActivity).isBottomNavVisible(false)
        showRecommendation(sharedViewModel.recommendation.value ?: RecommendationEntity())

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.supportFragmentManager?.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    private fun showRecommendation(recommendation: RecommendationEntity) {
        showMarkdown(recommendation.content)
        binding.title.text = recommendation.title
        binding.type.text = recommendation.type
        binding.country.text =
            context?.getString(R.string.country_recommendation, recommendation.country) ?: ""
        binding.category.text =
            context?.getString(R.string.category_recommendation, recommendation.category) ?: ""
        binding.image.load(recommendation.imageUrls.firstOrNull()) {
            placeholder(R.drawable.placeholder_book)
            error(R.drawable.placeholder_book)
            crossfade(true)
        }
    }

    companion object {
        const val TAG = "BookFragment"
        private val MULTIPLIERS = floatArrayOf(1.5F, 1.17F, 1F, 1F, .83F, .67F)
        private val LINK_COLOR = "#6766D8"

        /**
         * Получение объекта [BookFragment]
         */
        fun newInstance(): BookFragment {
            return BookFragment()
        }
    }
}