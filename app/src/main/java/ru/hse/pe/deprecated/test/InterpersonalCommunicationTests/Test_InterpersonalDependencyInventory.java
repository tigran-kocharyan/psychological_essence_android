package ru.hse.pe.deprecated.test.InterpersonalCommunicationTests;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.hse.pe.R;
import ru.hse.pe.deprecated.Functions;
import ru.hse.pe.deprecated.Menu;
import ru.hse.pe.deprecated.Test;
import ru.hse.pe.utils.callback.FirebaseSuccessListener;

public class Test_InterpersonalDependencyInventory extends AppCompatActivity {
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


    private int countOfQuestions = 48, counter = 1, counterQuestion = 0;
    private int[] progressBarChecked = new int[countOfQuestions];
    private int currentProgress = 0;
    private int sumScoringTest = 0, sumEmotionalPoints = 0, sumDoubtPoints = 0,
            sumAutonomyPoints = 0, sumCommonPoints;
    private TextView textView, test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3,
            test_title4, test_indicator4, test_text4;
    private int[] plusEmotionalPoints = new int[]{3, 6, 7, 9, 12, 15, 16, 19, 22, 26, 29,
    35, 38, 40, 43, 45, 47};
    private int[] plusDoubtPoints = new int[]{2, 5, 13, 17, 20, 24, 27, 30, 32, 36, 39, 41, 46};
    private int[] plusAutonomyPoints = new int[]{1, 4, 8, 11, 14, 18, 21, 25, 28, 31, 34, 37, 42, 48};

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1,
            R.id.test_title2, R.id.test_indicator2, R.id.test_text2,
            R.id.test_title3, R.id.test_indicator3, R.id.test_text3,
            R.id.test_title4, R.id.test_indicator4, R.id.test_text4};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3,
            test_title4, test_indicator4, test_text4};
    private String typeOfTest;
    private final Map<Integer, String> ANSWERCHOISES = new HashMap<Integer, String>() {{
        put(1, "Совершенно не согласен");
        put(2, "Скорее не согласен");
        put(3, "Скорее согласен");
        put(4, "Совершенно ответить");
    }};
    private Map<Integer, Integer> transcoding = new HashMap<Integer, Integer>() {{
        put(4, 1);
        put(3, 2);
        put(2, 3);
        put(1, 4);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_interpersonal_dependency_inventory);

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


        Functions.minusIndex(plusEmotionalPoints);
        Functions.minusIndex(plusAutonomyPoints);
        Functions.minusIndex(plusDoubtPoints);

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
            for (int i = 0, k = 1; i < rg.getChildCount(); i++, k++) {
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


        Functions.getSexFromDB(new FirebaseSuccessListener() {
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

        // Считаем эмоциональную опору
        countingEmotional(points);
        // Считаем неуверенность в себе
        countingDoubt(points);
        // Считаем стремление к автономии
        countingAutonomy(points);
        // Главная интерпретация итогового значения
        countingCommon(points);

        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
    }

    private void countingEmotional(List<Integer> pressedRB) {
        for (int i = 0; i < plusEmotionalPoints.length; i++) {
            sumEmotionalPoints += points.get(plusEmotionalPoints[i]);
        }

        if(isMale){
            textViews[0].setText("Ваш балл эмоциональной опоры на других (для мужчин): " + sumEmotionalPoints);
            if(sumEmotionalPoints >= 49){
                textViews[1].setText("Высокий балл эмоциональной опоры");
            }else if(sumEmotionalPoints >= 32){
                textViews[1].setText("Средний балл эмоциональной опоры");
            }else{
                textViews[1].setText("Низкий балл эмоциональной опоры");
            }
        }else {
            textViews[0].setText("Ваш итоговый балл сопереживания (для женщин): " + sumEmotionalPoints);
            if(sumEmotionalPoints >= 55){
                textViews[1].setText("Высокий балл эмоциональной опоры");
            }else if(sumEmotionalPoints >= 38){
                textViews[1].setText("Средний балл эмоциональной опоры");
            }else{
                textViews[1].setText("Низкий балл эмоциональной опоры");
            }
        }

        textViews[2].setText(R.string.IDIEmotinalRes);
    }

    private void countingDoubt(List<Integer> points) {
        for (int i = 0; i < plusDoubtPoints.length; i++) {
            sumDoubtPoints += points.get(plusDoubtPoints[i]);
        }

        sumDoubtPoints += transcoding.get(points.get(9));
        sumDoubtPoints += transcoding.get(points.get(22));
        sumDoubtPoints += transcoding.get(points.get(43));

        if(isMale){
            textViews[3].setText("Ваш балл неуверенности в себе (для мужчин): " + sumDoubtPoints);
            if(sumDoubtPoints >= 38){
                textViews[4].setText("Высокий балл неуверенности в себе");
            }else if(sumDoubtPoints >= 25){
                textViews[4].setText("Средний балл неуверенности в себе");
            }else{
                textViews[4].setText("Низкий балл неуверенности в себе");
            }
        }else {
            textViews[2].setText("Ваш балл неуверенности в себе (для женщин): " + sumDoubtPoints);
            if(sumDoubtPoints >= 38){
                textViews[3].setText("Высокий балл неуверенности в себе");
            }else if(sumDoubtPoints >= 25){
                textViews[4].setText("Средний балл неуверенности в себе");
            }else{
                textViews[4].setText("Низкий балл неуверенности в себе");
            }
        }
        textViews[5].setText(R.string.IDIDoubtRes);
    }

    private void countingAutonomy(List<Integer> points) {
        for (int i = 0; i < plusAutonomyPoints.length; i++) {
            sumAutonomyPoints += points.get(plusAutonomyPoints[i]);
        }

        if(isMale){
            textViews[6].setText("Ваш балл стремлении к автономии (для мужчин): " + sumAutonomyPoints);
            if(sumAutonomyPoints >= 38){
                textViews[7].setText("Высокий балл стремлении к автномии");
            }else if(sumAutonomyPoints >= 27){
                textViews[7].setText("Средний балл стремлении к автномии");
            }else{
                textViews[7].setText("Низкий балл стремлении к автномии");
            }
        }else {
            textViews[6].setText("Ваш балл стремлении к автономии (для женщин): " + sumAutonomyPoints);
            if(sumAutonomyPoints >= 34){
                textViews[7].setText("Высокий балл стремлении к автномии");
            }else if(sumAutonomyPoints >= 23){
                textViews[7].setText("Средний балл стремлении к автномии");
            }else{
                textViews[7].setText("Низкий балл стремлении к автномии");
            }
        }
        textViews[8].setText(R.string.IDIAutonomyRes);
    }

    private void countingCommon(List<Integer> points) {
        sumCommonPoints = sumEmotionalPoints + sumDoubtPoints - sumAutonomyPoints;

        if(isMale){
            textViews[9].setText("Ваш балл стремлении к автномии (для мужчин): " + sumCommonPoints);
            if(sumCommonPoints >= 53){
                textViews[10].setText("Высокий уровень чрезмерной межличностной зависимости.");
                textViews[11].setText(R.string.IDICommonHighRes);
            }else if(sumCommonPoints >= 25){
                textViews[10].setText("Средний уровень дисфункционального отделения.");
                textViews[11].setText(R.string.IDICommonAverageRes);
            }else{
                textViews[10].setText("Низкий уровень нормативной зависимости.");
                textViews[11].setText(R.string.IDICommonLowRes);
            }
        }else {
            textViews[9].setText("Ваш балл чрезмерной межличностной зависимости (для женщин): " + sumCommonPoints);
            if(sumCommonPoints >= 63){
                textViews[10].setText("Высокий уровень чрезмерной межличностной зависимости.");
                textViews[11].setText(R.string.IDICommonHighRes);
            }else if(sumCommonPoints >= 36){
                textViews[10].setText("Средний уровень дисфункционального отделения.");
                textViews[11].setText(R.string.IDICommonAverageRes);
            }else{
                textViews[10].setText("Низкий уровень нормативной зависимости.");
                textViews[11].setText(R.string.IDICommonLowRes);
            }
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