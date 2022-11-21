package ru.hse.pe.presentation.test.InterpersonalCommunicationTests;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.hse.pe.Functions;
import ru.hse.pe.Menu;
import ru.hse.pe.R;
import ru.hse.pe.Test;

public class Test_BornsteinRelationshipProfile extends AppCompatActivity {

    private List<String> questions;
    private HashMap<Integer, Integer> transcoding;


    private List<Integer> points;

    private int[] plusDestructiveDependence = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private int sumDestructiveDependence = 0;

    private int[] plusDysfunctionalSeparation = new int[]{11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    private int sumDysfunctionalSeparation = 0;

    private int[] plusHealthyAddiction = new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    private int sumHealthyAddiction = 0;


    private int countOfQuestions = 30, counter = 1, counterQuestion = 0,  transcodingvar = 5;
    private int currentProgress = 0;
    private int[] progressBarChecked = new int[countOfQuestions];

    private ProgressBar progressBar;
    private RadioButton radioButton;
    private TextView nameTest;
    private Button btnSkip, btnFinish;
    private List<RadioGroup> radioGroups;
    private TextView test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3;
    private TextView countOfProgress, question;
    private LinearLayout root, test_main, test_answers;
    private DatabaseReference mDataBase, commandsRef;
    private String TEST_KEY;

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1,
            R.id.test_title2, R.id.test_indicator2, R.id.test_text2,
            R.id.test_title3, R.id.test_indicator3, R.id.test_text3};

    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3};

    private final Map<Integer, String> ANSWERCHOISES = new HashMap<Integer, String>() {{
        put(1, "Cовсем не характерно для меня");
        put(2, "Cкорее не характерно для меня");
        put(3, "Не знаю");
        put(4, "Cкорее характерно для меня");
        put(5, "Очень сильно характерно для меня");
    }};
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bornstein_relationship_profile);

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

        Functions.minusIndex(plusDestructiveDependence);
        Functions.minusIndex(plusDysfunctionalSeparation);
        Functions.minusIndex(plusHealthyAddiction);

        btnSkip = findViewById(R.id.btnSkip);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressTTU);
        progressBar.setMax(countOfQuestions);
        countOfProgress = findViewById(R.id.countOfProgress);
        question = findViewById(R.id.question);
        questions = new ArrayList<>();
        points = new ArrayList<>();
        transcoding = new HashMap<>();
        for (int i = 1, j = transcodingvar; i < transcodingvar + 1; i++, j--) {
            transcoding.put(i, j);
        }
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

        // Считаем Деструктивная сверхзависимость (мужчины/женщины)
        countingDestructiveDependence();
        // Считаем Дисфункциональное отделение
        countingDysfunctionalSeparation();
        // Считаем Здоровая зависимость
        countingHealthyAddiction();


        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
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

    private void countingDestructiveDependence(){
        for (int i = 0; i < plusDestructiveDependence.length; i++) {
            sumDestructiveDependence += points.get(plusDestructiveDependence[i]);
        }

        boolean isMale = true;

        if(isMale){
            textViews[0].setText("Деструктивная сверхзависимость (мужчины)");
            textViews[1].setText("Ваш балл по шкале “Деструктивная сверхзависимость” (для мужчин:) "
                    + sumDestructiveDependence);

            if(sumDestructiveDependence >= 31) {
                textViews[2].setText(R.string.BRPDestructiveDependenceHighRes);
            }else if (sumDestructiveDependence >= 18){
                textViews[2].setText(R.string.BRPDestructiveDependenceAverageRes);
            }else{
                textViews[2].setText(R.string.BRPDestructiveDependenceLowRes);
            }
        }else{
            textViews[0].setText("Деструктивная сверхзависимость (женщины)");
            textViews[1].setText("Ваш балл по шкале “Деструктивная сверхзависимость” (для женщин:) "
                    + sumDestructiveDependence);

            if(sumDestructiveDependence >= 35) {
                textViews[2].setText(R.string.BRPDestructiveDependenceHighRes);
            }else if (sumDestructiveDependence >= 23){
                textViews[2].setText(R.string.BRPDestructiveDependenceAverageRes);
            }else{
                textViews[2].setText(R.string.BRPDestructiveDependenceLowRes);
            }
        }




    }

    private void countingDysfunctionalSeparation() {
        for (int i = 0; i < plusDysfunctionalSeparation.length; i++) {
            sumDysfunctionalSeparation += points.get(plusDysfunctionalSeparation[i]);
        }

        textViews[3].setText("Дисфункциональное отделение");
        textViews[4].setText("Ваш балл по шкале “Дисфункциональное отделение”: " + sumDysfunctionalSeparation);

        if(sumDysfunctionalSeparation >= 38) {
            textViews[5].setText(R.string.BRPDysfunctionalSeparationHighRes);
        }else if (sumDysfunctionalSeparation >= 28){
            textViews[5].setText(R.string.BRPDysfunctionalSeparationAverageRes);
        }else{
            textViews[5].setText(R.string.BRPDysfunctionalSeparationLowRes);
        }
    }


    private void countingHealthyAddiction() {
        for (int i = 0; i < plusHealthyAddiction.length; i++) {
            sumHealthyAddiction += points.get(plusHealthyAddiction[i]);
        }


        textViews[6].setText("Здоровая зависимость");
        textViews[7].setText("Ваш балл по шкале “Здоровая зависимость”: " + sumHealthyAddiction);

        if(sumHealthyAddiction >= 41) {
            textViews[8].setText(R.string.BRPHealthyAddictionHighRes);
        }else if (sumDestructiveDependence >= 21){
            textViews[8].setText(R.string.BRPHealthyAddictionAverageRes);
        }else{
            textViews[8].setText(R.string.BRPHealthyAddictionLowRes);
        }
    }



}