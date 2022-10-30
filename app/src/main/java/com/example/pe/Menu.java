package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Menu extends AppCompatActivity {
    TextView textView;
    private static String val = "", name = "";
    private static int countVisibleTests = 0;

    public static String getVal() {
        return val;
    }

    public static void setVal(String val) {
        Menu.val = val;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Menu.name = name;
    }

    public static int getCountVisibleTests() {
        return countVisibleTests;
    }

    public static void setCountVisibleTests(int countVisibleTests) {
        Menu.countVisibleTests = countVisibleTests;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void closeMenu(View view){
        Intent intent = new Intent(this, News.class);
        startActivity(intent);
    }

    public void goToSettings(View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void goToTest(View view){
        Intent intent = new Intent(this, Test.class);

        textView = ((TextView) view);
        String textViewStr = textView.getText().toString();
        if(textViewStr.equals("Характер человека")){
            Menu.setVal("CharacterOfUser");
            Menu.setName(textViewStr);
            Menu.setCountVisibleTests(12);
        }else if(textViewStr.equals("Межличностное отношение")){
            Menu.setVal("InterpersonalCommunication");
            Menu.setName(textViewStr);
            Menu.setCountVisibleTests(8);
        }else if(textViewStr.equals("Психологическое состояние")){
            Menu.setVal("PsychologicalState");
            Menu.setName(textViewStr);
            Menu.setCountVisibleTests(6);
        }else if(textViewStr.equals("Интеллект")){
            Menu.setVal("Intelligence");
            Menu.setName(textViewStr);
            Menu.setCountVisibleTests(0);
        }else if(textViewStr.equals("Профессиональная ориентация")){
            Menu.setVal("ProfessionalOrientation");
            Menu.setName(textViewStr);
            Menu.setCountVisibleTests(0);
        }


        intent.putExtra("typeOfTest", Menu.getVal());
        intent.putExtra("name", Menu.getName());
        intent.putExtra("countVisibleTests", Menu.getCountVisibleTests());
        startActivity(intent);
    }

    public void goToArticles(View view){
        Intent intent = new Intent(this, Articles.class);
        startActivity(intent);
    }
}