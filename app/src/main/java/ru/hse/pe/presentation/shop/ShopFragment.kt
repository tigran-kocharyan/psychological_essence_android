package ru.hse.pe.presentation.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.hse.pe.databinding.FragmentShopBinding
import ru.hse.pe.presentation.MainActivity


/**
 * [Fragment] to display the shop.
 *
 * @author Kocharyan Tigran
 */
class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).isBottomNavVisible(true)
        binding.subscription.setOnClickListener {
            SubscriptionBottomSheetDialogFragment.newInstance().show(
                (activity as AppCompatActivity).supportFragmentManager,
                SubscriptionBottomSheetDialogFragment.TAG
            )
        }
    }

    companion object {
        const val TAG = "ShopFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [ShopFragment]
         */
        fun newInstance() = ShopFragment()
    }
}