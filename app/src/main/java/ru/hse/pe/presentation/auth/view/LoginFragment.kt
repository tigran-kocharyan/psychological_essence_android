package ru.hse.pe.presentation.auth.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentLoginBinding
import ru.hse.pe.domain.interactor.AuthInteractor
import ru.hse.pe.domain.model.SubscriptionStatusException
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.auth.viewmodel.AuthViewModel
import ru.hse.pe.presentation.auth.viewmodel.AuthViewModelFactory
import ru.hse.pe.utils.Utils.getLongSnackbar
import ru.hse.pe.utils.Utils.getSnackbar
import ru.hse.pe.utils.Utils.isInvalid
import ru.hse.pe.utils.Utils.setCommonAnimations
import ru.hse.pe.utils.Utils.setVisible
import ru.hse.pe.utils.Utils.validateEmail
import ru.hse.pe.utils.Utils.value
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var root: View

    @Inject
    lateinit var interactor: AuthInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            schedulers,
            interactor
        )
    }
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
        (activity?.applicationContext as App).getAppComponent().inject(this)
        root = binding.root
        binding.buttonRegister.setOnClickListener { registerUser() }
        binding.buttonLogin.setOnClickListener { loginUser() }
        binding.buttonForgotPassword.setOnClickListener { resetPassword() }
        observeLiveData()
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
                        sharedViewModel.setUid(user.uid)
                        viewModel.getUserSubscriptionStatus(user.uid)
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
            .replace(
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
            .replace(R.id.fragment_container, RegisterFragment.newInstance(), RegisterFragment.TAG)
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

    private fun observeLiveData() {
        viewModel.errorLiveData.observe(viewLifecycleOwner, this::showError)
        viewModel.progressLiveData.observe(viewLifecycleOwner, this::showProgress)
        viewModel.isSubscribedLiveData.observe(viewLifecycleOwner, this::isUserSubscribed)
    }


    private fun showError(throwable: Throwable) {
        if (throwable is SubscriptionStatusException) {
            isUserSubscribed(false)
        }
    }


    private fun showProgress(isVisible: Boolean) {
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun isUserSubscribed(isSubscribed: Boolean) {
        Log.d(TAG, "isUserSubscribed: ${isSubscribed}")
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra(EXTRA_AUTH_UID, sharedViewModel.uid.value)
        intent.putExtra(EXTRA_AUTH_IS_SUBSCRIBED, isSubscribed)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        const val TAG = "LoginFragment"
        const val EXTRA_AUTH_UID = "EXTRA_AUTH_DATA"
        const val EXTRA_AUTH_IS_SUBSCRIBED = "EXTRA_AUTH_IS_SUBSCRIBED"

        /**
         * Получение объекта [LoginFragment]
         */
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}