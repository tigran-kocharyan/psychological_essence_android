package ru.hse.pe.presentation.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.hse.pe.R
import ru.hse.pe.databinding.FragmentAuthBinding
import ru.hse.pe.utils.Utils.setCommonAnimations

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = binding.root
        binding.buttonRegister.setOnClickListener { registerUser() }
        binding.buttonLogin.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .setCommonAnimations()
            .addToBackStack(null)
            .replace(R.id.fragment_container, LoginFragment.newInstance(), LoginFragment.TAG)
            .commit()
    }

    private fun registerUser() {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .setCommonAnimations()
            .addToBackStack(null)
            .replace(R.id.fragment_container, RegisterFragment.newInstance(), RegisterFragment.TAG)
            .commit()
    }

    companion object {
        const val TAG = "AuthFragment"

        /**
         * Получение объекта [AuthFragment]
         */
        fun newInstance(): AuthFragment {
            return AuthFragment()
        }
    }
}