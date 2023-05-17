package ru.hse.pe.presentation.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ru.hse.pe.R
import ru.hse.pe.databinding.FragmentResetPasswordBinding
import ru.hse.pe.utils.Utils.getLongSnackbar
import ru.hse.pe.utils.Utils.isInvalid
import ru.hse.pe.utils.Utils.setGone
import ru.hse.pe.utils.Utils.validateEmail
import ru.hse.pe.utils.Utils.value

class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var root: View

    private var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = binding.root
        binding.buttonReset.setOnClickListener { resetPassword() }
    }

    private fun resetPassword() {
        when {
            binding.emailInput.text?.isBlank() ?: true || binding.emailInput.isInvalid() || !binding.emailInput.text.toString()
                .validateEmail() -> {
                binding.emailInput.error = getString(R.string.login_valid_email)
                binding.emailInput.requestFocus()
            }
            else -> {
                showProgress(true)
                auth.sendPasswordResetEmail(binding.emailInput.value())
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            update()
                        } else {
                            getLongSnackbar(
                                root, getString(R.string.reset_error)
                            ).show()
                        }
                        showProgress(false)
                    }
            }
        }
    }

    private fun update() {
        binding.buttonReset.setOnClickListener { (activity as AppCompatActivity).supportFragmentManager.popBackStack() }
        binding.buttonReset.text = getString(R.string.reset_login)
        binding.subtitle.text = getString(R.string.reset_email)
        binding.email.setGone()
    }


    private fun showProgress(isVisible: Boolean) {
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "ResetPasswordFragment"

        /**
         * Получение объекта [ResetPasswordFragment]
         */
        fun newInstance(): ResetPasswordFragment {
            return ResetPasswordFragment()
        }
    }
}