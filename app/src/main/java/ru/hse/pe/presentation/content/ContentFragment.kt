package ru.hse.pe.presentation.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.hse.pe.R
import ru.hse.pe.databinding.FragmentContentBinding
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.type.article.view.ArticlesFragment
import ru.hse.pe.presentation.content.type.fact.view.FactsFragment
import ru.hse.pe.presentation.content.type.recommendation.view.RecommendationsFragment
import ru.hse.pe.presentation.content.type.technique.TechniquesFragment
import ru.hse.pe.presentation.test.TestFragment

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
        binding.tests.setOnClickListener {
            setCurrentFragment(
                TestFragment.newInstance(),
                TestFragment.TAG
            )
        }

        binding.articles.setOnClickListener {
            setCurrentFragment(
                ArticlesFragment.newInstance(),
                ArticlesFragment.TAG
            )
        }
        binding.recommendations.setOnClickListener {
            setCurrentFragment(
                RecommendationsFragment.newInstance(),
                RecommendationsFragment.TAG
            )
        }
        binding.facts.setOnClickListener {
            setCurrentFragment(
                FactsFragment.newInstance(),
                FactsFragment.TAG
            )
        }
        binding.techniques.setOnClickListener {
            setCurrentFragment(
                TechniquesFragment.newInstance(),
                TechniquesFragment.TAG
            )
        }
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