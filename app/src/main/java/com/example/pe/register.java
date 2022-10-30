package com.example.pe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.example.pe.Models.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    private Button btnRegister;
    private LinearLayout root;
    private CheckBox accept;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private String USER_KEY = "User";
    private List<String> listEmails;

    private EditText name, email, password, repeatPassword;
    private RadioButton male, female, radioButton;
    private RadioGroup radioGroup;
    private Boolean isValidate;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        root = findViewById(R.id.login_register);

        auth =  FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://zeta-turbine-297107-default-rtdb.europe-west1.firebasedatabase.app/");
        users = db.getReference(USER_KEY);

        init();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameTxt = name.getText().toString();
                if(TextUtils.isEmpty(nameTxt)){
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(!(male.isChecked() || female.isChecked())){
                    Snackbar.make(root, "Выберите пол", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                registerUser();
            }
        });
    }

    private void init(){
        name = findViewById(R.id.register_nameInp);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        male = (RadioButton) findViewById(R.id.register_male);
        female = (RadioButton) findViewById(R.id.register_female);
        email = findViewById(R.id.register_emailInp);
        password = findViewById(R.id.register_password);
        repeatPassword = findViewById(R.id.register_repeatPassword);
        accept = findViewById(R.id.register_accept);
        listEmails = new ArrayList<>();
    }

    private void registerUser() {
        String nameTxt = name.getText().toString();

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        String whichSex = radioButton.getText().toString();
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();
        String repeatPasswordTxt = repeatPassword.getText().toString();

        if(TextUtils.isEmpty(nameTxt)){
            Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
            return;
        }

       if(TextUtils.isEmpty(emailTxt)){
            Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
            return;
       }

       isValidate = validate(emailTxt);
       if(!isValidate){
           Snackbar.make(root, "Некорректный email", Snackbar.LENGTH_SHORT).show();
           return;
       }
       getUserFromDB(emailTxt);

        if(passwordTxt.length() < 5){
            Snackbar.make(root, "Введите пароль, который больше 5 символов", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(!repeatPasswordTxt.equals(passwordTxt)){
            Snackbar.make(root, "Пароли не совпадают", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(!accept.isChecked()){
            Snackbar.make(root, "Потвердите пользовательское соглашение!", Snackbar.LENGTH_SHORT).show();
            return;
        }



        // Регистрация пользователя
        auth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User(nameTxt, whichSex, emailTxt, passwordTxt);
                        users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent = new Intent(register.this, News.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        MainActivity.fa.finish();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(root, "Ошибка:  " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void getUserFromDB(String emailTxt){
        ValueEventListener valueEventListener  = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String emailDB = ds.child("email").getValue(String.class);
                    if(emailTxt.equals(emailDB)){
                        Snackbar.make(root, "Данный email уже существует!", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }

        };
        users.addValueEventListener(valueEventListener);
    };

    public void acceptText(View view){
        accept.setChecked(!accept.isChecked());
    }
}