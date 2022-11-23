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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.hse.pe.BuildConfig
import ru.hse.pe.R
import ru.hse.pe.data.model.User
import ru.hse.pe.databinding.FragmentRegisterBinding
import ru.hse.pe.utils.Utils.getLongSnackbar
import ru.hse.pe.utils.Utils.getSnackbar
import ru.hse.pe.utils.Utils.isInvalid
import ru.hse.pe.utils.Utils.setGone
import ru.hse.pe.utils.Utils.setVisible
import ru.hse.pe.utils.Utils.validateEmail

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var root: View

    private var auth = FirebaseAuth.getInstance()
    private var users = FirebaseDatabase.getInstance(FIREBASE_URL).getReference(USER_KEY)

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
    }

    private fun registerUser() {
        val name = binding.nameInput.text.toString()
        val surname = binding.surnameInput.text.toString()
        val patronymic = binding.patronymicInput.text.toString()
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val repeatPassword = binding.passwordRepeatInput.text.toString()
        val sex =
            binding.root.findViewById<RadioButton>(binding.sexRadio.checkedRadioButtonId)?.text.toString()

        when {
            name.isEmpty() -> {
                binding.nameInput.error = "Введите ваше имя"
                binding.nameInput.requestFocus()
            }
            binding.surnameInput.isInvalid() -> {
                binding.nameInput.error = "Введите вашу фамилию"
                binding.nameInput.requestFocus()
            }
            binding.patronymicInput.isInvalid() -> {
                binding.nameInput.error = "Введите ваше отчество"
                binding.nameInput.requestFocus()
            }
            email.isEmpty() -> {
                binding.emailInput.error = "Введите вашу почту"
                binding.emailInput.requestFocus()
            }
            sex.isEmpty() -> {
                binding.female.error = "Выберите ваш пол"
                binding.sexRadio.requestFocus()
            }
            !email.validateEmail() -> {
                binding.emailInput.error = "Некорректная почта"
                binding.emailInput.requestFocus()
            }
            password.length < 5 -> {
                getSnackbar(root, "Пароль должен быть больше 5 символов").show()
                binding.passwordInput.requestFocus()
            }
            repeatPassword != password -> {
                getSnackbar(root, "Пароли не совпадают").show()
                binding.passwordRepeatInput.requestFocus()
            }
            !binding.acceptCheckbox.isChecked -> {
                getSnackbar(root, "Подтвердите пользовательское соглашение!").show()
                binding.acceptCheckbox.requestFocus()
            }
            else -> {
                showProgress(true)
                val user = UserData(name, surname, patronymic, email, password, sex)
                createUser(user)
            }
        }
    }

    private fun isRegistered(user: UserData) {
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val emailDB = ds.child("email").getValue(String::class.java)
                    if (user.email == emailDB) {
                        getSnackbar(root, "Данная почта уже зарегистрирована!").show()
                        return
                    }
                }
                createUser(user)
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        }
        users.addValueEventListener(valueEventListener)
    }

    private fun createUser(data: UserData) {
        auth.createUserWithEmailAndPassword(data.email, data.password)
            .addOnSuccessListener {
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                    if (!it.isSuccessful) getSnackbar(root, "Не удается создать аккаунт").show()
                }

                val user = User(
                    "${data.surname} ${data.name} ${data.patronymic}",
                    data.sex,
                    data.email,
                    data.password
                )
                users.child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                    .setValue(user)
                    .addOnSuccessListener {
                        update()
                    }.addOnFailureListener {
                        getSnackbar(binding.root, it.message.toString()).show()
                    }
                showProgress(false)
            }
            .addOnFailureListener {
                when (it) {
                    is FirebaseAuthWeakPasswordException -> {
                        getSnackbar(root, "Слабый пароль").show()
                        binding.passwordInput.requestFocus()
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        binding.emailInput.error = "Неверная почта"
                        binding.emailInput.requestFocus()
                    }
                    is FirebaseAuthUserCollisionException -> {
                        binding.emailInput.error = "Данная почта уже зарегистрирована!"
                        binding.emailInput.requestFocus()
                    }
                    else -> {
                        getLongSnackbar(root, it.message.toString()).show()
                    }
                }
                showProgress(false)
            }
    }

    private fun showProgress(isVisible: Boolean) {
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun openLogin() {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
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
        binding.buttonRegister.text = "Проверить почту"
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

        data class UserData(
            val name: String,
            val surname: String,
            val patronymic: String,
            val email: String,
            val password: String,
            val sex: String
        )
    }
}