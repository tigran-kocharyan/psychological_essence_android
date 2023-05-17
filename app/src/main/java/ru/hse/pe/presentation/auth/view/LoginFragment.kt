package ru.hse.pe.presentation.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.hse.pe.R
import ru.hse.pe.databinding.FragmentLoginBinding
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.utils.Utils.getLongSnackbar
import ru.hse.pe.utils.Utils.getSnackbar
import ru.hse.pe.utils.Utils.isInvalid
import ru.hse.pe.utils.Utils.setCommonAnimations
import ru.hse.pe.utils.Utils.setVisible
import ru.hse.pe.utils.Utils.validateEmail
import ru.hse.pe.utils.Utils.value


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var root: View

    private var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = binding.root
        binding.buttonRegister.setOnClickListener { registerUser() }
        binding.buttonLogin.setOnClickListener { loginUser() }
        binding.buttonForgotPassword.setOnClickListener { resetPassword() }
    }

    private fun loginUser() {
        when {
            binding.emailInput.isInvalid() && binding.emailInput.text.toString()
                .validateEmail() -> {
                binding.emailInput.error = getString(R.string.login_valid_email)
                binding.emailInput.requestFocus()
            }
            binding.passwordInput.text.toString().isBlank() -> {
                getSnackbar(root, getString(R.string.login_hint_password))
                binding.passwordInput.requestFocus()
            }
            else -> {
                authorize()
            }
        }
    }

    private fun authorize() {
        showProgress(true)
        auth.signInWithEmailAndPassword(binding.emailInput.value(), binding.passwordInput.value())
            .addOnSuccessListener { result ->
                result.user?.let { user ->
                    if (user.isEmailVerified) {
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra(EXTRA_AUTH_DATA, user.uid)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        binding.resend.setVisible()
                        binding.resendCodeButton.setOnClickListener { verify(user) }
//                      binding.buttonVerify.setOnClickListener { verify(user) }
//                      binding.buttonVerify.visibility = View.VISIBLE
                    }
                    showProgress(false)
                }
            }.addOnFailureListener {
                getSnackbar(root, getString(R.string.login_error)).show()
                showProgress(false)
            }
    }

    private fun resetPassword() {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .setCommonAnimations()
            .addToBackStack(null)
            .add(
                R.id.fragment_container,
                ResetPasswordFragment.newInstance(),
                ResetPasswordFragment.TAG
            )
            .commit()
    }

    private fun registerUser() {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .setCommonAnimations()
            .addToBackStack(null)
            .add(R.id.fragment_container, RegisterFragment.newInstance(), RegisterFragment.TAG)
            .commit()
    }

    private fun verify(user: FirebaseUser) {
        user.sendEmailVerification().addOnCanceledListener {
            getLongSnackbar(
                root, getString(R.string.login_email_verify)
            ).show()
        }.addOnFailureListener {
            getLongSnackbar(
                root,
                getString(R.string.login_error_email_verify)
            ).show()
        }
    }

    private fun showProgress(isVisible: Boolean) {
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "LoginFragment"
        const val EXTRA_AUTH_DATA = "LoginFragment"

        /**
         * Получение объекта [LoginFragment]
         */
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}