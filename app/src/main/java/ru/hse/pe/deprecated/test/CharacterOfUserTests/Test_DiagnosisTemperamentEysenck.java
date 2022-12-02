package ru.hse.pe.deprecated.test.CharacterOfUserTests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.hse.pe.R;
import ru.hse.pe.deprecated.Functions;
import ru.hse.pe.deprecated.Menu;
import ru.hse.pe.deprecated.Test;

public class Test_DiagnosisTemperamentEysenck extends AppCompatActivity {

    private List<String> questions;
    private List<Integer> points;
    private List<RadioGroup> radioGroups;
    private RadioButton radioButton;
    private Button btnSkip, btnFinish;
    private ProgressBar progressBar;

    private TextView countOfProgress, question, nameTest;
    private LinearLayout root, test_main, test_answers;
    private DatabaseReference mDataBase, commandsRef;
    private String TEST_KEY;


    private final int countOfQuestions = 57;
    private int counter = 1;
    private int counterQuestion = 0;
    private final int[] progressBarChecked = new int[countOfQuestions];
    private int currentProgress = 0;
    private final int sumScoringTest = 0;
    private TextView textView, test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2, test_title3, test_indicator3, test_text3;
    private final int[] plusExtroversionPoints = {1, 3, 8, 10, 13, 17, 22, 25, 27, 39, 44, 46, 49, 53, 56};
    private final int[] minusExtroversionPoints = {5, 15, 20, 29, 32, 34, 37, 41, 51};

    private final int[] plusNeuroticismPoints = {2, 4, 7, 9, 11, 14, 16, 19, 21, 23, 26, 28, 31, 33, 35,
                38, 40, 43, 45, 47, 50, 52, 55, 57};
    private final int[] plusScaleLiesPoints = {6, 24, 36};
    private final int[] minusScaleLiesPoints = {12, 18, 30, 42, 48, 54};
    private int sumExtroversionPoints, sumNeuroticismPoints, sumScaleLiesPoints;

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1,
            R.id.test_title2, R.id.test_indicator2, R.id.test_text2,
            R.id.test_title3, R.id.test_indicator3, R.id.test_text3};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2, test_title3, test_indicator3, test_text3};
    private final String[] ANSWERCHOISES = new String[]{"Да", "Нет"};
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_diagnosis_temperament_eysenck);

        init();
    }

    public void init(){
        Bundle arguments = getIntent().getExtras();
        nameTest = findViewById(R.id.nameTest);
        nameTest.setText(arguments.get("name").toString());
        TEST_KEY = arguments.get("key").toString();

        root = findViewById(R.id.mainLL);
        typeOfTest = getIntent().getExtras().getString("typeOfTest");
        mDataBase = FirebaseDatabase.getInstance("https://zeta-turbine-297107-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/Tests/" + typeOfTest);

        commandsRef = mDataBase.child(TEST_KEY);
        nameTest = findViewById(R.id.nameTest);


        test_main = findViewById(R.id.test_main);
        test_answers = findViewById(R.id.test_answers);
        for (int i = 0; i < ids.length; i++) {
            textViews[i] = findViewById(ids[i]);
        }

        Functions.minusIndex(plusExtroversionPoints);
        Functions.minusIndex(minusExtroversionPoints);
        Functions.minusIndex(plusNeuroticismPoints);
        Functions.minusIndex(plusScaleLiesPoints);
        Functions.minusIndex(minusScaleLiesPoints);


        btnSkip = findViewById(R.id.btnSkip);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressTTU);
        progressBar.setMax(countOfQuestions);
        questions = new ArrayList<>();
        points = new ArrayList<>();
        countOfProgress = findViewById(R.id.countOfProgress);
        question = findViewById(R.id.question);

        getTestFromDB();

        radioGroups = new ArrayList<>();
        for (int i = 0; i < countOfQuestions; i++) {
            int id = getResources().getIdentifier("radioGroup" + i, "id", getPackageName());
            radioGroups.add(findViewById(id));
        }

        for (RadioGroup rg : radioGroups) {
            for (int i = 0, k = 0; i < rg.getChildCount(); i++, k++) {
                RadioButton radioButton = (RadioButton) rg.getChildAt(i);
                radioButton.setText(ANSWERCHOISES[k]);
            }
        }

        Arrays.fill(progressBarChecked, -1);
        for(RadioGroup radioGroup : radioGroups){
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (progressBarChecked[counterQuestion] != 1) {
                        currentProgress++;
                        progressBar.setProgress(currentProgress);
                        progressBarChecked[counterQuestion] = 1;
                    }
                    isFinish();
                }
            });
        }
    }

    public void btnBack(View view){
        if (radioGroups.get(counterQuestion).getCheckedRadioButtonId() == -1) {
            Snackbar.make(root, "Выберите вариант ответа или нажмите кнопку пропустить", Snackbar.LENGTH_SHORT).show();
            return;
        } else {
            counter--;
            counterQuestion--;

            if (counterQuestion < 0) {
                counter = countOfQuestions;
                counterQuestion = countOfQuestions - 1;
            }

            for (RadioGroup rg : radioGroups) {
                rg.setVisibility(View.GONE);
            }

            radioGroups.get(counterQuestion).setVisibility(View.VISIBLE);
            question.setText(questions.get(counterQuestion));
            countOfProgress.setText(counter + "/" + countOfQuestions);
        }
    }


    public void btnGo(View view){
        if (radioGroups.get(counterQuestion).getCheckedRadioButtonId() == -1) {
            Snackbar.make(root, "Выберите вариант ответа или нажмите кнопку пропустить", Snackbar.LENGTH_SHORT).show();
            return;
        } else {
            ++counter;
            ++counterQuestion;

            if (counterQuestion > countOfQuestions - 1) {
                counter = 1;
                counterQuestion = 0;
            }

            for (RadioGroup rg : radioGroups) {
                rg.setVisibility(View.GONE);
            }

            radioGroups.get(counterQuestion).setVisibility(View.VISIBLE);
            question.setText(questions.get(counterQuestion));
            countOfProgress.setText(counter + "/" + countOfQuestions);
        }
    }

    public void btnSkip(View view){
        ++counter;
        ++counterQuestion;
        if (counterQuestion > countOfQuestions - 1) {
            counter = 1;
            counterQuestion = 0;
        }

        for (RadioGroup rg : radioGroups) {
            rg.setVisibility(View.GONE);
        }

        radioGroups.get(counterQuestion).setVisibility(View.VISIBLE);
        question.setText(questions.get(counterQuestion));
        countOfProgress.setText(counter + "/" + countOfQuestions);
    }

    public void btnFinish(View view){
        List<RadioButton> pressedRB = new ArrayList<>();
        for (int i = 0; i < countOfQuestions; i++) {
            radioButton = findViewById(radioGroups.get(i).getCheckedRadioButtonId());
            pressedRB.add(radioButton);
        }

        //Подсчет: Экстраверсия - интроверсия:
        String txt;
        for (int i = 0; i < plusExtroversionPoints.length; i++) {
            txt = (String) pressedRB.get(plusExtroversionPoints[i]).getText();
            if(txt.equals(ANSWERCHOISES[0]) ){
                sumExtroversionPoints++;
            }
        }
        for (int i = 0; i < minusExtroversionPoints.length; i++) {
            txt = (String) pressedRB.get(minusExtroversionPoints[i]).getText();
            if(txt.equals(ANSWERCHOISES[1]) ){
                sumExtroversionPoints++;
            }
        }

        //Подсчет: Нейротизм
        for (int i = 0; i < plusNeuroticismPoints.length; i++) {
            txt = (String) pressedRB.get(plusNeuroticismPoints[i]).getText();
            if(txt.equals(ANSWERCHOISES[0]) ){
                sumNeuroticismPoints++;
            }
        }

        //Подсчет: Шкала лжи
        for (int i = 0; i < plusScaleLiesPoints.length; i++) {
            txt = (String) pressedRB.get(plusScaleLiesPoints[i]).getText();
            if(txt.equals(ANSWERCHOISES[0])){
                sumScaleLiesPoints++;
            }
        }
        for (int i = 0; i < minusScaleLiesPoints.length; i++) {
            txt = (String) pressedRB.get(minusScaleLiesPoints[i]).getText();
            if(txt.equals(ANSWERCHOISES[1])){
                sumScaleLiesPoints++;
            }
        }



        textViews[0].setText("Ваш итоговый балл экстраверсии/интроверсии: " + sumExtroversionPoints);
        if(sumExtroversionPoints > 19) {
            textViews[1].setText("Вы яркий экстраверт");
            textViews[2].setText(R.string.DTEExtroversion);
        }else if(sumExtroversionPoints > 15) {
            textViews[1].setText("Вы экстраверт");
            textViews[2].setText(R.string.DTEExtroversion);
        }else if(sumExtroversionPoints > 12) {
            textViews[1].setText("У вас среднее значение экстраверсии/интроверсии");
            textViews[2].setText(R.string.DTEAverageRes);
        }else if(sumExtroversionPoints > 9) {
            textViews[1].setText("Вы интроверт");
            textViews[2].setText(R.string.DTEIntroversion);
        }else {
            textViews[1].setText("Вы глубокий интроверт");
            textViews[2].setText(R.string.DTEIntroversion);
        }

        textViews[3].setText("Ваш итоговый балл нейротизма: " + sumNeuroticismPoints);
        if(sumNeuroticismPoints > 19) {
            textViews[4].setText("У вас очень высокий уровень нейротизма");
        }else if(sumNeuroticismPoints > 14) {
            textViews[4].setText("У вас высокий уровень нейротизма");
        }else if(sumNeuroticismPoints > 9) {
            textViews[4].setText("У вас средний уровень нейротизма");
        }else {
            textViews[4].setText("У вас низкий уровень нейротизма");
        }
        textViews[5].setText(R.string.DTEExtroversion);

        textViews[6].setText("Ваш итоговый балл искренности: " + sumScaleLiesPoints);
        if(sumScaleLiesPoints > 4) {
            textViews[7].setText("У вас неискренность в ответах, свидетельствующая также о некоторой демонстративности поведения и ориентированности испытуемого на социальное одобрение");
        }else {
            textViews[7].setText("Показатель в норме");
        }
        textViews[8].setText(R.string.DTEScaleLies);


        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
    }

    private void isFinish(){
        int c = 0;
        for (int i = 0; i < countOfQuestions; i++) {
            if (progressBarChecked[i] == 1) {
                c++;
            }
        }

        if(c == countOfQuestions){
            btnSkip.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
        }
    }


    public void openMenu(View view){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    public void finishTest(View view){
        Intent intent = new Intent(this, Test.class);

        intent.putExtra("typeOfTest", Menu.getVal());
        intent.putExtra("name", Menu.getName());
        intent.putExtra("countVisibleTests", Menu.getCountVisibleTests());
        startActivity(intent);
    }

    private void getTestFromDB(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str;
                for(DataSnapshot ds : snapshot.getChildren()){
                    for (int i = 0; i < countOfQuestions; i++) {
                        str = ds.child(String.valueOf(i)).getValue(String.class);
                        questions.add(str);
                    }
                }
                question.setText(questions.get(counterQuestion));
                countOfProgress.setText(counter + "/" + countOfQuestions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }

        };
        commandsRef.addValueEventListener(valueEventListener);
    }
}