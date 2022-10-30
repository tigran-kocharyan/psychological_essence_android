package com.example.pe.InterpersonalCommunicationTests;

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

import com.example.pe.FireBaseSuccessListener;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test_Como extends AppCompatActivity{
    private  boolean isMale;
    private List<String> questions;
    private Boolean whichSex = false;
    boolean[] bool = new boolean[1];

    private int countOfQuestions = 40, counter = 1, counterQuestion = 0;
    private int currentProgress = 0;
    private int[] progressBarChecked = new int[countOfQuestions];

    private int sumTensionPoints = 0, sumAlienationPoints = 0, sumConflictPoints = 0,
            sumAggressionPoints = 0, sumTranslatePoints;
    private List<Integer> points;

    private int[] plusTensionPoints =  {4, 8, 11, 19, 22, 26, 30,  35,  36, 38, 40};
    private int[] plusAlienationPoints =  {1, 5, 9, 12, 15, 23, 27, 31, 34, 37, 39};
    private int[] plusConflictPoints =  {2, 6, 13, 16, 18, 20, 24, 28, 32};
    private int[] plusAggressionPoints =  {3, 7, 10, 14, 17, 21, 25, 29, 33};

    private ProgressBar progressBar;
    private RadioButton radioButton;
    private TextView nameTest;
    private Button btnSkip, btnFinish;
    private List<RadioGroup> radioGroups;
    private TextView test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3,
            test_title4, test_indicator4, test_text4,
            test_title5, test_indicator5, test_text5;
    private TextView countOfProgress, question;
    private LinearLayout root, test_main, test_answers;
    private DatabaseReference mDataBase, commandsRef;
    private String TEST_KEY, typeOfTest;

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1,
            R.id.test_title2, R.id.test_indicator2, R.id.test_text2,
            R.id.test_title3, R.id.test_indicator3, R.id.test_text3,
            R.id.test_title4, R.id.test_indicator4, R.id.test_text4,
            R.id.test_title5, R.id.test_indicator5, R.id.test_text5};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3,
            test_title4, test_indicator4, test_text4,
            test_title5, test_indicator5, test_text5};

    private final Map<Integer, String> ANSWERCHOISES = new HashMap<Integer, String>() {{
        put(1, "Полностью не согласен");
        put(2, "Не согласен");
        put(3, "Скорее не согласен");
        put(4, "Затрудняюсь ответить");
        put(5, "Скорее согласен");
        put(6, "Согласен");
        put(7, "Полностью согласен");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_como);

        init();
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

    private void init(){
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


        btnSkip = findViewById(R.id.btnSkip);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressTTU);
        progressBar.setMax(countOfQuestions);
        questions = new ArrayList<>();
        points = new ArrayList<>();


        Functions.minusIndex(plusAggressionPoints);
        Functions.minusIndex(plusAlienationPoints);
        Functions.minusIndex(plusConflictPoints);
        Functions.minusIndex(plusTensionPoints);

        getTestFromDB();

        radioGroups = new ArrayList<>();
        for (int i = 0; i < countOfQuestions; i++) {
            int id = getResources().getIdentifier("radioGroup" + i, "id", getPackageName());
            radioGroups.add((RadioGroup) findViewById(id));
        }


        for (RadioGroup rg : radioGroups) {
            for (int i = 0, k = 1; i < rg.getChildCount(); i++, k++) {
                RadioButton radioButton = (RadioButton) rg.getChildAt(i);
                radioButton.setText(ANSWERCHOISES.get(k));
            }
        }


        countOfProgress = findViewById(R.id.countOfProgress);
        question = findViewById(R.id.question);

        Arrays.fill(progressBarChecked, -1);

        for(RadioGroup radioGroup : radioGroups){
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (progressBarChecked[counterQuestion] != 1) {
                    currentProgress++;
                    progressBar.setProgress(currentProgress);
                    progressBarChecked[counterQuestion] = 1;
                }
                isFinish();
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



    public void btnFinish(View view){
        for (int i = 0; i < countOfQuestions; i++) {
            radioButton = (RadioButton) findViewById(radioGroups.get(i).getCheckedRadioButtonId());

            for (Map.Entry<Integer, String> pair : ANSWERCHOISES.entrySet()) {
                if(pair.getValue().contentEquals(radioButton.getText())){
                    points.add(pair.getKey());
                }
            }
        }

         //Подсчет сырых баллов: 	Напряженность отношений
         for (int i = 0; i < plusTensionPoints.length; i++) {
            sumTensionPoints += points.get(plusTensionPoints[i]);
         }

        //Подсчет сырых баллов: 	Отчужденность
        for (int i = 0; i < plusAlienationPoints.length; i++) {
            sumAlienationPoints += points.get(plusAlienationPoints[i]);
        }

        //Подсчет сырых баллов: 	Конфликтность
        for (int i = 0; i < plusConflictPoints.length; i++) {
            sumConflictPoints += points.get(plusConflictPoints[i]);
        }

        //Подсчет сырых баллов: 	Агрессия
        for (int i = 0; i < plusAggressionPoints.length; i++) {
            sumAggressionPoints += points.get(plusAggressionPoints[i]);
        }

        if(isMale){
            countingStanMale(sumTensionPoints, sumAlienationPoints, sumConflictPoints, sumAggressionPoints);
        }else{
            countingStanFemale(sumTensionPoints, sumAlienationPoints, sumConflictPoints, sumAggressionPoints);
        }

        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
    }

    private void countingStanFemale(int n, int o, int  k, int a) {
        int sumn = 0, sumo = 0, sumk = 0, suma = 0;

        sumn = translateSum(n, 16, 22, 28, 34, 40, 46, 52, 58, 64);
        sumo = translateSum(o, 20, 25, 30, 35, 40, 45, 50, 55, 60);
        sumk = translateSum(k, 13, 18, 23, 28, 33, 38, 43, 48, 53);
        suma = translateSum(a, 14, 19, 24, 29, 34, 39, 44, 49, 54);

        int sumCommon = sumn + sumo + sumk + suma;

        countingFinalRes(sumn, sumo, sumk, suma, sumCommon);
    }

    private void countingStanMale(int n, int o, int k, int a) {
        int sumn = 0, sumo = 0, sumk = 0, suma = 0;

        sumn = translateSum(n, 17, 23, 29, 35, 41, 47, 53, 59, 65);
        sumo = translateSum(o, 18, 24, 30, 36, 42, 48, 54, 60, 66);
        sumk = translateSum(k, 15, 20, 25, 30, 35, 40, 45, 50, 55);
        suma = translateSum(a, 16, 21, 26, 31, 36, 41, 46, 51, 56);
        int sumCommon = sumn + sumo + sumk + suma;

        countingFinalRes(sumn, sumo, sumk, suma, sumCommon);
    }

    private void countingFinalRes(int sumn, int sumo, int sumk, int suma, int sumCommon) {
        int finalSumN = 0, finalSumO = 0, finalSumK = 0, finalSumA = 0, finalSumCommon;

        finalSumN = sumn;
        textViews[0].setText("Ваш балл напряженности отношений: " + finalSumN);
        if(finalSumN > 8){
            textViews[1].setText("Высокий показатель (от 8 до 10 стэнов)");
            textViews[2].setText(R.string.ComoNHighRes);
        }else if(finalSumN > 4){
            textViews[1].setText("Средний показатель (от 4 до 7 стэнов)");
            textViews[2].setText(R.string.ComoNAverageRes);
        }else{
            textViews[1].setText("Низкий показатель (от 1 до 3 стэнов)");
            textViews[2].setText(R.string.ComoNLowRes);
        }

        finalSumO = sumo;
        textViews[3].setText("Ваш балл отчужденности в отношениях: " + finalSumO);
        if(finalSumO > 8){
            textViews[4].setText("Высокий показатель (от 8 до 10 стэнов)");
            textViews[5].setText(R.string.ComoOHighRes);
        }else if(finalSumO > 4){
            textViews[4].setText("Средний показатель (от 4 до 7 стэнов)");
            textViews[5].setText(R.string.ComoOAverageRes);
        }else{
            textViews[4].setText("Низкий показатель (от 1 до 3 стэнов)");
            textViews[5].setText(R.string.ComoOLowRes);
        }

        finalSumK = sumk;
        textViews[6].setText("Ваш балл конфликтности в отношениях: " + finalSumK);
        if(finalSumK > 8){
            textViews[7].setText("Высокий показатель (от 8 до 10 стэнов)");
            textViews[8].setText(R.string.ComoKHighRes);
        }else if(finalSumK > 4){
            textViews[7].setText("Средний показатель (от 4 до 7 стэнов)");
            textViews[8].setText(R.string.ComoKAverageRes);
        }else{
            textViews[7].setText("Низкий показатель (от 1 до 3 стэнов)");
            textViews[8].setText(R.string.ComoKLowRes);
        }

        finalSumA = suma;
        textViews[9].setText("Ваш балл конфликтности в отношениях: " + finalSumA);
        if(finalSumA > 8){
            textViews[10].setText("Высокий показатель (от 8 до 10 стэнов)");
            textViews[11].setText(R.string.ComoKHighRes);
        }else if(finalSumA > 4){
            textViews[10].setText("Средний показатель (от 4 до 7 стэнов)");
            textViews[11].setText(R.string.ComoKAverageRes);
        }else{
            textViews[10].setText("Низкий показатель (от 1 до 3 стэнов)");
            textViews[11].setText(R.string.ComoKLowRes);
        }

        finalSumCommon = translateSum(sumCommon, 10, 13, 16, 19, 22, 25, 28, 31, 34);
        textViews[12].setText("Ваш итоговый балл: " + finalSumCommon);
        if(finalSumCommon > 8){
            textViews[13].setText("Высокий показатель (от 8 до 10 стэнов)");
            textViews[14].setText(R.string.ComoCommonHighRes);
        }else if(finalSumCommon > 4){
            textViews[13].setText("Средний показатель (от 4 до 7 стэнов)");
            textViews[14].setText(R.string.ComoCommonAverageRes);
        }else{
            textViews[13].setText("Низкий показатель (от 1 до 3 стэнов)");
            textViews[14].setText(R.string.ComoCommonLowRes);
        }
    }

    private int translateSum(int num, int a, int b, int c, int d, int e, int f, int g, int h, int i){
        int sum = 0;
        if(num == a){
            sum++;
        }else if(num < b){
            sum = 2;
        }else if(num < c){
            sum = 3;
        }else if(num < d){
            sum = 4;
        }else if(num < e){
            sum = 5;
        }else if(num < f){
            sum = 6;
        }else if(num < g){
            sum = 7;
        }else if(num < h){
            sum = 8;
        }else if(num < i){
            sum = 9;
        }else{
            sum = 10;
        }
        return sum;
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