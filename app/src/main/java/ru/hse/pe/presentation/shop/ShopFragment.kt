package ru.hse.pe.presentation.shop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SHOP_URL)))
        }
    }

    companion object {
        const val TAG = "ShopFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"
        private const val SHOP_URL = "http://www.google.com"

        /**
         * Получение объекта [ShopFragment]
         */
        fun newInstance() = ShopFragment()
    }
}