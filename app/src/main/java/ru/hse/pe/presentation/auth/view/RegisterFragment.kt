package ru.hse.pe.presentation.auth.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import ru.hse.pe.App
import ru.hse.pe.BuildConfig
import ru.hse.pe.R
import ru.hse.pe.databinding.FragmentRegisterBinding
import ru.hse.pe.domain.interactor.AuthInteractor
import ru.hse.pe.domain.model.UserEntity
import ru.hse.pe.presentation.auth.viewmodel.AuthViewModel
import ru.hse.pe.presentation.auth.viewmodel.AuthViewModelFactory
import ru.hse.pe.utils.Utils.getLongSnackbar
import ru.hse.pe.utils.Utils.getSnackbar
import ru.hse.pe.utils.Utils.setCommonAnimations
import ru.hse.pe.utils.Utils.setGone
import ru.hse.pe.utils.Utils.setVisible
import ru.hse.pe.utils.Utils.validateEmail
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var root: View

    private var auth = FirebaseAuth.getInstance()
    private var users = FirebaseDatabase.getInstance(FIREBASE_URL).getReference(USER_KEY)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = binding.root
        binding.buttonRegister.setOnClickListener { registerUser() }
        binding.buttonLogin.setOnClickListener { openLogin() }
        binding.accept.setOnClickListener { openToU() }
//        observeLiveData()
    }

    private fun registerUser() {
        val name = binding.nameInput.text.toString()
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val repeatPassword = binding.passwordRepeatInput.text.toString()
        val sex =
            binding.root.findViewById<RadioButton>(binding.sexRadio.checkedRadioButtonId)?.text.toString()

        when {
            name.isEmpty() -> {
                binding.nameInput.error = getString(R.string.register_hintName)
                binding.nameInput.requestFocus()
            }
            email.isEmpty() -> {
                binding.emailInput.error = getString(R.string.register_hintEmail)
                binding.emailInput.requestFocus()
            }
            sex.isEmpty() -> {
                binding.female.error = getString(R.string.register_hintSex)
                binding.sexRadio.requestFocus()
            }
            !email.validateEmail() -> {
                binding.emailInput.error = getString(R.string.register_hintErrorEmail)
                binding.emailInput.requestFocus()
            }
            password.length < 5 -> {
                getSnackbar(root, getString(R.string.register_hintErrorPassword)).show()
                binding.passwordInput.requestFocus()
            }
            repeatPassword != password -> {
                getSnackbar(root, getString(R.string.register_hintMisMatchPassword)).show()
                binding.passwordRepeatInput.requestFocus()
            }
            !binding.acceptCheckbox.isChecked -> {
                getSnackbar(root, getString(R.string.register_hintUserAgreement)).show()
                binding.acceptCheckbox.requestFocus()
            }
            else -> {
                showProgress(true)
                createUser(UserCredentials(name, email, password, sex))
            }
        }
    }

    private fun createUser(credentials: UserCredentials) {
        auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
            .addOnSuccessListener {
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                    if (!it.isSuccessful) getSnackbar(
                        root,
                        getString(R.string.register_hintErrorAccount)
                    ).show()
                }
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                val user = UserEntity(
                    uid,
                    credentials.name,
                    credentials.sex,
                    credentials.email,
                    credentials.password,
                    false
                )
                users.child(uid)
                    .setValue(user)
                    .addOnSuccessListener {
                        viewModel.addUser(user)
                        update()
                    }.addOnFailureListener {
                        getSnackbar(binding.root, it.message.toString()).show()
                    }
                showProgress(false)
            }
            .addOnFailureListener {
                when (it) {
                    is FirebaseAuthWeakPasswordException -> {
                        getSnackbar(root, getString(R.string.register_hintWeakPassword)).show()
                        binding.passwordInput.requestFocus()
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        binding.emailInput.error = getString(R.string.register_hintWrongEmail)
                        binding.emailInput.requestFocus()
                    }
                    is FirebaseAuthUserCollisionException -> {
                        binding.emailInput.error = getString(R.string.register_hintEmailExisted)
                        binding.emailInput.requestFocus()
                    }
                    else -> {
                        getLongSnackbar(root, it.message.toString()).show()
                    }
                }
                showProgress(false)
            }
    }

    private fun openToU() {
        ToUBottomSheetDialogFragment.newInstance().show(
            (activity as AppCompatActivity).supportFragmentManager,
            ToUBottomSheetDialogFragment.TAG
        )
    }

    private fun openLogin() {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .setCommonAnimations()
            .addToBackStack(null)
            .add(
                R.id.fragment_container,
                LoginFragment.newInstance(),
                LoginFragment.TAG
            )
            .commit()
    }

    private fun update() {
        binding.hiddenInfo.setGone()
        binding.fillInfo.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean = true
        })
        binding.subtitle.setVisible()
        binding.buttonRegister.text = getString(R.string.register_hintCheckEmail)
        binding.buttonRegister.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                this.startActivity(intent)
            } catch (ignored: ActivityNotFoundException) {
            }
        }
    }

//    private fun observeLiveData() {
//        viewModel.errorLiveData.observe(viewLifecycleOwner, this::showError)
//        viewModel.progressLiveData.observe(viewLifecycleOwner, this::showProgress)
//    }

    private fun showProgress(isVisible: Boolean) {
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
//
//    private fun showError(throwable: Throwable) {
//        Log.d(ArticlesFragment.TAG, "showError() called with: throwable = $throwable")
//        Snackbar.make(binding.root, throwable.toString(), Snackbar.LENGTH_SHORT)
//            .show()
//    }

    companion object {
        const val TAG = "RegisterFragment"
        const val FIREBASE_URL: String = BuildConfig.FirebaseUrl
        const val USER_KEY = "User"

        /**
         * Получение объекта [RegisterFragment]
         */
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }

        data class UserCredentials(
            val name: String,
            val email: String,
            val password: String,
            val sex: String
        )
    }
}