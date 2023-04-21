package ru.hse.pe.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.hse.pe.databinding.FragmentPlaceholderBinding


/**
 * [Fragment] to display the shop.
 *
 * @author Kocharyan Tigran
 */
class PlaceholderFragment : Fragment() {
    private lateinit var binding: FragmentPlaceholderBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceholderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).isBottomNavVisible(true)
        binding.buttonContent.setOnClickListener {
            (activity as MainActivity).setDefaultMenuItemSelected()
        }
    }

    companion object {
        const val TAG = "PlaceholderFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [PlaceholderFragment]
         */
        fun newInstance() = PlaceholderFragment()
    }
}