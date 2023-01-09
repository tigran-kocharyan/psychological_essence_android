package ru.hse.pe.presentation.content

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.hse.pe.presentation.content.adapter.ContentAdapter
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentContentBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.article.view.ArticleFragment
import ru.hse.pe.presentation.article.view.ArticlesFragment
import ru.hse.pe.presentation.content.view.article.viewmodel.ContentViewModel
import ru.hse.pe.utils.callback.ContentClickListener
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject

/**
 * [Fragment] to display the trending events.
 *
 * @author Kocharyan Tigran
 */
class ContentFragment : Fragment() {
    private lateinit var binding: FragmentContentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).isBottomNavVisible(true)
        binding.articles.setOnClickListener { setCurrentFragment(ArticlesFragment.newInstance(), ArticlesFragment.TAG) }
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String) =
        parentFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.pop_enter,
                R.anim.pop_exit
            )
            .add(R.id.fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()

    companion object {
        const val TAG = "ContentFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [ContentFragment]
         */
        fun newInstance() = ContentFragment()
    }
}