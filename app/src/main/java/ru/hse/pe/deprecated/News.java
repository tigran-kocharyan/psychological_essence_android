package ru.hse.pe.deprecated;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ru.hse.pe.R;

public class News extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }

    public void openMenu(View view){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}