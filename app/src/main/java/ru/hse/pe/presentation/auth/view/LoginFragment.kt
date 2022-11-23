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
import ru.hse.pe.presentation.test.TestActivity
import ru.hse.pe.utils.Utils.getLongSnackbar
import ru.hse.pe.utils.Utils.getSnackbar
import ru.hse.pe.utils.Utils.isInvalid
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
                binding.emailInput.error = "Введите корректную почту"
                binding.emailInput.requestFocus()
            }
            binding.passwordInput.text.toString().isBlank() -> {
                getSnackbar(root, "Введите пароль")
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
                        val intent = Intent(activity, TestActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        binding.resend.setVisible()
                        binding.resendCodeButton.setOnClickListener { verify(user) }
//                        binding.buttonVerify.setOnClickListener { verify(user) }
//                        binding.buttonVerify.visibility = View.VISIBLE
                    }
                    showProgress(false)
                }
            }.addOnFailureListener {
                getSnackbar(root, "Неверная почта или пароль").show()
                showProgress(false)
            }
    }

    private fun resetPassword() {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
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
            .addToBackStack(null)
            .add(R.id.fragment_container, RegisterFragment.newInstance(), RegisterFragment.TAG)
            .commit()
    }

    private fun verify(user: FirebaseUser) {
        user.sendEmailVerification().addOnCanceledListener {
            getLongSnackbar(
                root, "На вашу почту отправлена ссылка для подтверждения аккаунта!" +
                        "\nПерейдите по ссылке для подтверждения"
            ).show()
        }.addOnFailureListener {
            getLongSnackbar(
                root,
                "Не удалось отправить письмо-подтверждение\nВозможно недавно письмо уже посылалось!"
            ).show()
        }
    }

    private fun showProgress(isVisible: Boolean) {
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "LoginFragment"

        /**
         * Получение объекта [LoginFragment]
         */
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}