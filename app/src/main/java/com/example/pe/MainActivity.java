package com.example.pe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button btnSignIn;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    LinearLayout root;

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSignIn = findViewById(R.id.btnSignIn);
        root = findViewById(R.id.login_register);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }

        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        fa = this;
    }

    private void loginUser() {
        EditText email = findViewById(R.id.input_login);
        EditText password = findViewById(R.id.input_pass);

        if(TextUtils.isEmpty(email.getText().toString())){
            Snackbar.make(root, "Введите email", Snackbar.LENGTH_SHORT).show();
            return;
        }else if(!email.getText().toString().contains("@")){
            Snackbar.make(root, "Некорректный email", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password.getText().toString().toString())){
            Snackbar.make(root, "Введите пароль", Snackbar.LENGTH_SHORT).show();
            return;
        }


        auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Snackbar.make(root, "Успешно", Snackbar.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, News.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Ошибка: Неверный email или пароль", Snackbar.LENGTH_SHORT).show();
                    }
                });


    }

    public void goToRegister(View view){
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }


    public void showPassword(View view) {
    }
}