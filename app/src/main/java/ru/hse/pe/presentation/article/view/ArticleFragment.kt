package ru.hse.pe.presentation.article.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.image.ImagesPlugin
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentArticleBinding
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.presentation.MainActivity


class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var root: View
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = binding.root
        (activity as MainActivity).isBottomNavVisible(false)
        showArticle(sharedViewModel.article.value ?: ArticleEntity())

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
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

    private fun showArticle(article: ArticleEntity) {
        showMarkdown(article.content)
        binding.title.text = article.title
        binding.time.text = context?.getString(R.string.time_article, article.time) ?: ""
        binding.category.text = article.category
        context?.let {
            Glide.with(it)
                .load(article.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(binding.image)
        }
    }

    companion object {
        const val TAG = "ArticleFragment"
        private val MULTIPLIERS = floatArrayOf(1.5F, 1.17F, 1F, 1F, .83F, .67F)
        private val LINK_COLOR = "#6766D8"

        /**
         * Получение объекта [ArticleFragment]
         */
        fun newInstance(): ArticleFragment {
            return ArticleFragment()
        }
    }
}