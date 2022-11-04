package ru.hse.pe.article;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import ru.hse.pe.Menu;
import ru.hse.pe.R;

public class Articles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
    }

    public void openMenu(View view){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    public void openArticle(View view){
        Intent intent = new Intent(this, Article.class);
        startActivity(intent);
    }
}