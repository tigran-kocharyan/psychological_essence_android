package com.example.pe.CharacterOfUserTests;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.pe.Functions;
import com.example.pe.Menu;
import com.example.pe.R;
import com.example.pe.Test;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test_HardinessSurvey extends AppCompatActivity {
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

    private int countOfQuestions = 45, counter = 1, counterQuestion = 0;
    private int[] progressBarChecked = new int[countOfQuestions];
    private int currentProgress = 0;
    private int sumScoringTest = 0;
    private TextView textView, test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2, test_title3, test_indicator3, test_text3,
            test_title4, test_indicator4, test_text4;
    
    private int sumInvolvementPoints = 0, sumControlPoints = 0, sumRiscAcPoints = 0;
    private int[] plusInvolvementPoints = new int[]{4, 12, 22, 23, 24, 29, 41};
    private int[] plusInvolvementPointsR = new int[]{2, 3, 10, 11, 14, 28, 32, 37};
    private int[] plusControlPoints = new int[]{9, 15, 17, 21, 25, 44};
    private int[] plusControlPointsR = new int[]{1, 5, 6, 8, 16, 20, 27, 31, 35, 39, 43};
    private int[] plusRiscAcPoints = new int[]{34, 45};
    private int[] plusRiscAcPointsR = new int[]{7, 13, 18, 19, 26, 30, 33, 36};


    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1,
            R.id.test_title2, R.id.test_indicator2, R.id.test_text2,
            R.id.test_title3, R.id.test_indicator3, R.id.test_text3,
            R.id.test_title4, R.id.test_indicator4, R.id.test_text4};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2, test_title3, test_indicator3, test_text3,
            test_title4, test_indicator4, test_text4};

    private final Map<Integer, String> ANSWERCHOISES = new HashMap<Integer, String>() {{
        put(0, "Нет");
        put(1, "Скорее нет, чем да");
        put(2, "Скорее да чем нет");
        put(3, "Да");
    }};
    private HashMap<Integer, Integer> transcoding;
    private int transcodingvar = 3;
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_hardiness_survey);

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

        test_main = findViewById(R.id.test_main);
        test_answers = findViewById(R.id.test_answers);
        for (int i = 0; i < ids.length; i++) {
            textViews[i] = findViewById(ids[i]);
        }

        Functions.minusIndex(plusInvolvementPoints);
        Functions.minusIndex(plusInvolvementPointsR);
        Functions.minusIndex(plusControlPoints);
        Functions.minusIndex(plusControlPointsR);
        Functions.minusIndex(plusRiscAcPoints);
        Functions.minusIndex(plusRiscAcPointsR);

        btnSkip = findViewById(R.id.btnSkip);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressTTU);
        progressBar.setMax(countOfQuestions);
        questions = new ArrayList<>();
        points = new ArrayList<>();
        countOfProgress = findViewById(R.id.countOfProgress);
        question = findViewById(R.id.question);

        transcoding = new HashMap<>();
        for (int i = 0, j = transcodingvar; i < transcodingvar + 1; i++, j--) {
            transcoding.put(i, j);
        }
        getTestFromDB();

        radioGroups = new ArrayList<>();
        for (int i = 0; i < countOfQuestions; i++) {
            int id = getResources().getIdentifier("radioGroup" + i, "id", getPackageName());
            radioGroups.add((RadioGroup) findViewById(id));
        }

        for (RadioGroup rg : radioGroups) {
            for (int i = 0, k = 0; i < rg.getChildCount(); i++, k++) {
                RadioButton radioButton = (RadioButton) rg.getChildAt(i);
                radioButton.setText(ANSWERCHOISES.get(k));
            }
        }

        for (int i = 0; i < progressBarChecked.length; i++) {
            progressBarChecked[i] = -1;
        }
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
            radioButton = (RadioButton) findViewById(radioGroups.get(i).getCheckedRadioButtonId());
            for (Map.Entry<Integer, String> pair : ANSWERCHOISES.entrySet()) {
                if(pair.getValue().contentEquals(radioButton.getText())){
                    points.add(pair.getKey());
                }
            }
        }



        //Подсчет: Вовлеченность
        for (int i = 0; i < plusInvolvementPoints.length; i++) {
            sumInvolvementPoints += points.get(plusInvolvementPoints[i]);
            sumScoringTest += points.get(plusInvolvementPoints[i]);
        }
        for (int i = 0; i < plusInvolvementPointsR.length; i++) {
            sumInvolvementPoints += transcoding.get(points.get(plusInvolvementPointsR[i]));
            sumScoringTest += transcoding.get(points.get(plusInvolvementPointsR[i]));
        }

        //Подсчет: Контроль
        for (int i = 0; i < plusControlPoints.length; i++) {
            sumControlPoints += points.get(plusControlPoints[i]);
            sumScoringTest += points.get(plusControlPoints[i]);
        }
        for (int i = 0; i < plusControlPointsR.length; i++) {
            sumControlPoints += transcoding.get(points.get(plusControlPointsR[i]));
            sumScoringTest += transcoding.get(points.get(plusControlPointsR[i]));
        }

        //Подсчет: Принятие риска
        for (int i = 0; i < plusRiscAcPoints.length; i++) {
            sumRiscAcPoints += points.get(plusRiscAcPoints[i]);
            sumScoringTest += points.get(plusRiscAcPoints[i]);
        }
        for (int i = 0; i < plusRiscAcPointsR.length; i++) {
            sumRiscAcPoints += transcoding.get(points.get(plusRiscAcPointsR[i]));
            sumScoringTest += transcoding.get(points.get(plusControlPointsR[i]));
        }



        textViews[0].setText("Общие баллы жизнестойкости: " + sumScoringTest);
        if(sumScoringTest >= 100){
            textViews[1].setText("Высокие показатели (100 - 135) балла (ов)");
            textViews[2].setText(R.string.HSCommonHighRes);
        }else if(sumScoringTest >= 62){
            textViews[1].setText("Средние показатели (62 - 99) балла (ов)");
            textViews[2].setText(R.string.HSCommonAverageRes);
        }else{
            textViews[1].setText("Низкие показатели (0 - 61) балла (ов)");
            textViews[2].setText(R.string.HSCommonLowRes);
        }

        textViews[3].setText("Ваш балл вовлеченности: " + sumInvolvementPoints);
        if(sumInvolvementPoints >= 47){
            textViews[4].setText("Высокие показатели (47 - 54) балла (ов)");
            textViews[5].setText(R.string.HSInvolvementHighRes);
        }else if(sumInvolvementPoints >= 30){
            textViews[4].setText("Средние показатели (30 - 46) балла (ов)");
            textViews[5].setText(R.string.HSInvolvementAverageRes);
        }else{
            textViews[4].setText("Низкие показатели (0 - 29) балла (ов)");
            textViews[5].setText(R.string.HSInvolvementLowRes);
        }

        textViews[6].setText("Ваш балл контроля: " + sumControlPoints);
        if(sumControlPoints >= 39){
            textViews[7].setText("Высокие показатели (39 - 51) балла (ов)");
            textViews[8].setText(R.string.HSControlHighRes);
        }else if(sumControlPoints >= 21){
            textViews[7].setText("Средние показатели (21 - 38) балла (ов)");
            textViews[8].setText(R.string.HSControlAverageRes);
        }else{
            textViews[7].setText("Низкие показатели (0 - 20) балла (ов)");
            textViews[8].setText(R.string.HSControlLowRes);
        }

        textViews[9].setText("Ваш балл принятия риска: " + sumRiscAcPoints);
        if(sumRiscAcPoints >= 19){
            textViews[10].setText("Высокие показатели (19 - 30) балла (ов)");
            textViews[11].setText(R.string.HSRiscAcHighRes);
        }else if(sumRiscAcPoints >= 9){
            textViews[10].setText("Средние показатели (9 - 18) балла (ов)");
            textViews[11].setText(R.string.HSRiscAcAverageRes);
        }else{
            textViews[10].setText("Низкие показатели (0 - 8) балла (ов)");
            textViews[11].setText(R.string.HSRiscAcLowRes);
        }

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
        ValueEventListener valueEventListener  = new ValueEventListener() {
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
    };
}