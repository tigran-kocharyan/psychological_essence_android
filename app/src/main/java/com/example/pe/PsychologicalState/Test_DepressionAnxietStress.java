package com.example.pe.PsychologicalState;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.pe.Functions;
import com.example.pe.FireBaseSuccessListener;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test_DepressionAnxietStress extends AppCompatActivity {

    private List<String> questions;
    private List<Integer> points;
    private List<RadioGroup> radioGroups;
    private RadioButton radioButton;
    private Button btnSkip, btnFinish;
    private ProgressBar progressBar;
    private boolean isMale;


    private TextView countOfProgress, question, nameTest;
    private LinearLayout root, test_main, test_answers;
    private DatabaseReference mDataBase, commandsRef;
    private String TEST_KEY;


    private int countOfQuestions = 21, counter = 1, counterQuestion = 0;
    private int[] progressBarChecked = new int[countOfQuestions];
    private int currentProgress = 0;
    private int sumScoringTest = 0, sumDepressionPoints = 0, sumAnxietPoints = 0,
            sumStressPoints = 0, sumCommonPoints;
    private TextView textView, test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3;
    private int[] plusDepressionPoints = new int[]{3, 5, 10, 13, 16, 17, 21};
    private int[] plusAnxietPoints = new int[]{2, 4, 7, 9, 15, 19, 20};
    private int[] plusStressPoints = new int[]{1, 6, 8, 11, 12, 14, 18};

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1,
            R.id.test_title2, R.id.test_indicator2, R.id.test_text2,
            R.id.test_title3, R.id.test_indicator3, R.id.test_text3};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3};
    private String typeOfTest;
    private final Map<Integer, String> ANSWERCHOISES = new HashMap<Integer, String>() {{
        put(0, "Вообще не относится ко мне");
        put(1, "Относилось ко мне до некоторой степени или некоторое время");
        put(2, "Относилось ко мне в значительной мере или значительную часть времени");
        put(3, "Относилось ко мне полностью или большую часть времени");
    }};
    private Map<Integer, Integer> transcoding = new HashMap<Integer, Integer>() {{
        put(3, 0);
        put(2, 1);
        put(1, 2);
        put(0, 3);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_depression_anxiet_stress);

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


        Functions.minusIndex(plusDepressionPoints);
        Functions.minusIndex(plusStressPoints);
        Functions.minusIndex(plusAnxietPoints);

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
            radioGroups.add((RadioGroup) findViewById(id));
        }

        for (RadioGroup rg : radioGroups) {
            for (int i = 0, k = 0; i < rg.getChildCount(); i++, k++) {
                RadioButton radioButton = (RadioButton) rg.getChildAt(i);
                radioButton.setText(ANSWERCHOISES.get(k));
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


        Functions.getSexFromDB(new FireBaseSuccessListener() {
            @Override
            public void onDataFound(boolean isDataFetched) {
                userIsOnFirebase(isDataFetched);
            }
        });
    }

    private boolean userIsOnFirebase(boolean isOnFirebase){
        isMale = isOnFirebase;
        Log.e("Test_Como", "isMale" + isMale);
        return isOnFirebase;
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
        for (int i = 0; i < countOfQuestions; i++) {
            radioButton = (RadioButton) findViewById(radioGroups.get(i).getCheckedRadioButtonId());

            for (Map.Entry<Integer, String> pair : ANSWERCHOISES.entrySet()) {
                if(pair.getValue().contentEquals(radioButton.getText())){
                    points.add(pair.getKey());
                }
            }
        }

        // Считаем депрессию
        countingDepression(points);
        // Считаем тревожность
        countingAnxiet(points);
        // Считаем стресс
        countingStress(points);


        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
    }

    private void countingDepression(List<Integer> pressedRB) {
        for (int i = 0; i < plusDepressionPoints.length; i++) {
            sumDepressionPoints += points.get(plusDepressionPoints[i]);
        }

        textViews[0].setText("Ваш балл депрессии: " + sumDepressionPoints);
        if(sumDepressionPoints >= 14){
            textViews[1].setText("Крайне выраженный уровень (14+)");
            textViews[2].setText(R.string.DASDepVeryHighRes);
        }else if(sumDepressionPoints >= 11){
            textViews[1].setText("Сильно выраженный уровень (11-13)");
            textViews[2].setText(R.string.DASDepHighRes);
        }else if(sumDepressionPoints >= 7){
            textViews[1].setText("Умеренный уровень (7-10)");
            textViews[2].setText(R.string.DASDepAverageRes);
        }else if(sumDepressionPoints >= 5){
            textViews[1].setText("Слабо выраженный уровень (5-6)");
            textViews[2].setText(R.string.DASDepLowRes);
        }else {
            textViews[1].setText("Норма (0-4)");
            textViews[2].setText(R.string.DASDepVeryLowRes);
        }
    }

    private void countingAnxiet(List<Integer> points) {
        for (int i = 0; i < plusAnxietPoints.length; i++) {
            sumAnxietPoints += points.get(plusAnxietPoints[i]);
        }

        textViews[3].setText("Ваш балл тревожности: " + sumAnxietPoints);
        if(sumAnxietPoints >= 10){
            textViews[4].setText("Крайне выраженный уровень (10+)");
            textViews[5].setText(R.string.DASAnxVeryHighRes);
        }else if(sumAnxietPoints >= 8){
            textViews[4].setText("Сильно выраженный уровень (8-9)");
            textViews[5].setText(R.string.DASAnxHighRes);
        }else if(sumAnxietPoints >= 6){
            textViews[4].setText("Умеренный уровень (6-7)");
            textViews[5].setText(R.string.DASAnxAverageRes);
        }else if(sumAnxietPoints >= 4){
            textViews[4].setText("Слабо выраженный уровень (4-5)");
            textViews[5].setText(R.string.DASAnxLowRes);
        }else {
            textViews[4].setText("Норма (0-3)");
            textViews[5].setText(R.string.DASAnxVeryLowRes);
        }
    }

    private void countingStress(List<Integer> points) {
        for (int i = 0; i < plusStressPoints.length; i++) {
            sumStressPoints += points.get(plusStressPoints[i]);
        }

        textViews[6].setText("Ваш балл стресса: " + sumStressPoints);
        if(sumStressPoints >= 17){
            textViews[7].setText("Крайне выраженный уровень (17+)");
            textViews[8].setText(R.string.DASStrVeryHighRes);
        }else if(sumStressPoints >= 13){
            textViews[7].setText("Сильно выраженный уровень (13-16)");
            textViews[8].setText(R.string.DASStrHighRes);
        }else if(sumStressPoints >= 10){
            textViews[7].setText("Умеренный уровень (10-12)");
            textViews[8].setText(R.string.DASStrAverageRes);
        }else if(sumStressPoints >= 8){
            textViews[7].setText("Слабо выраженный уровень (8-9)");
            textViews[8].setText(R.string.DASStrLowRes);
        }else {
            textViews[7].setText("Норма (0-7)");
            textViews[8].setText(R.string.DASStrVeryLowRes);
        }
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
    };
}