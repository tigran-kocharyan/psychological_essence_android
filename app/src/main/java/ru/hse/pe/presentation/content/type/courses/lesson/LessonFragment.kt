package ru.hse.pe.presentation.content.type.courses.lesson

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import coil.load
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tables.TableTheme
import io.noties.markwon.image.ImagesPlugin
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentLessonBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.LessonEntity
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.presentation.content.type.test.ui.TestContentFragment
import ru.hse.pe.presentation.content.type.test.ui.TestsFragment
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject

/*
* Урок из курса
*/
class LessonFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private lateinit var binding: FragmentLessonBinding

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
        binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        viewModel.getLesson(sharedViewModel.lesson.value?.id.toString())

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    // Показываем контент
    private fun showLesson(lesson: LessonEntity) {
        binding.title.text = lesson.name
        showMarkdown(lesson.content)
        binding.start.setOnClickListener {
            viewModel.getQuiz(lesson.quiz?.id.toString())
        }

        if (lesson.imageUrl != null && lesson.imageUrl.isNotBlank()) {
            binding.image.load(lesson.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_article)
                error(R.drawable.placeholder_article)
            }
        } else {
            binding.image.load(R.drawable.placeholder_article)
        }
        context?.let {
            Glide.with(it)
                .load(lesson.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(binding.image)
        }
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TestsFragment.TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable) {
        Log.d(TestsFragment.TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showQuiz(quiz: QuizEntity) {
        sharedViewModel.setQuiz(quiz)

        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.pop_enter,
                R.anim.pop_exit
            )
            .add(
                R.id.fragment_container, TestContentFragment.newInstance(),
                TestContentFragment.TAG
            )
            .commit()
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getLessonLiveData().observe(viewLifecycleOwner, this::showLesson)
        viewModel.getQuizLiveData().observe(viewLifecycleOwner, this::showQuiz)
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
                .usePlugin(TablePlugin.create(tableTheme))
                .build()
            markwon.setMarkdown(binding.markdownLesson, text ?: "")
        }
    }

    companion object {
        const val TAG = "LessonFragment"
        private val MULTIPLIERS = floatArrayOf(1.5F, 1.17F, 1F, 1F, .83F, .67F)
        private val LINK_COLOR = "#6766D8"

        // дизайн таблицы (если есть)
        private val tableTheme = TableTheme.Builder()
            .tableBorderColor(Color.BLUE)
            .tableBorderWidth(0)
            .tableCellPadding(0)
            .tableHeaderRowBackgroundColor(Color.BLACK)
            .tableEvenRowBackgroundColor(Color.GREEN)
            .tableOddRowBackgroundColor(Color.YELLOW)
            .build()

        /**
         * Получение объекта [LessonFragment]
         */
        fun newInstance() = LessonFragment()
    }
}