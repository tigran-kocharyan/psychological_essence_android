package ru.hse.pe.presentation.article.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.image.ImagesPlugin
import ru.hse.pe.R
import ru.hse.pe.data.model.Article
import ru.hse.pe.databinding.FragmentArticleBinding
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.article.viewmodel.ArticleViewModel
import ru.hse.pe.utils.Utils.getSnackbar
import ru.hse.pe.utils.scheduler.SchedulersProviderImpl


class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var root: View
    private lateinit var viewModel: ArticleViewModel


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
        createViewModel()
        observeLiveData()

        viewModel.getArticlesFromFB()
        viewModel.getArticleFromFB()
        showProgress(true)
    }

    private fun createViewModel() {
        viewModel = ArticleViewModel(SchedulersProviderImpl())
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
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

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "showError() called with: throwable = $throwable")
        getSnackbar(binding.root, throwable.toString()).show()
    }

    private fun showArticle(article: Article) {
        viewModel.getMarkdownText(article.mdUrl)
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
        viewModel.getMarkdownText(article.mdUrl)
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getMarkdownLiveData().observe(viewLifecycleOwner, this::showMarkdown)
        viewModel.getArticleLiveData().observe(viewLifecycleOwner, this::showArticle)
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