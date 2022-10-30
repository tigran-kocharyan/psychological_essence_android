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

public class Test_CognitiveFlexibilityQuestionnaire extends AppCompatActivity {
    private List<String> questions;

    private int countOfQuestions = 20, counter = 1, counterQuestion = 0;
    private int currentProgress = 0;
    private int[] progressBarChecked = new int[countOfQuestions];

    private int sumAlternativesScoringTest = 0, sumControlScoringTest = 0, sumСommonScoringTest = 0;
    private List<Integer> points;
    private int[] plusAlternativesAgreePoints = new int[]{3, 5, 6, 8, 10, 12, 13, 14, 16, 18, 19, 20};
    private int[] plusControlAgreePoints = new int[]{1, 2, 4, 7, 9, 11, 15, 17};
    private int[] plusСommonAgreePoints = new int[]{1, 3, 5, 6, 8, 10, 12, 13, 14, 15, 16, 18, 19, 20};
    private Map<Integer, Integer> transcoding;

    private ProgressBar progressBar;
    private RadioButton radioButton;
    private TextView nameTest;
    private Button btnSkip, btnFinish;
    private List<RadioGroup> radioGroups;
    private TextView textView, test_title1, test_title2, test_title3,
            test_indicator1, test_indicator2, test_indicator3,
            test_text1, test_text2, test_text3;
    private TextView countOfProgress, question;
    private LinearLayout root, test_main, test_answers;
    private DatabaseReference mDataBase, commandsRef;
    private String TEST_KEY;

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1, R.id.test_title2,
    R.id.test_indicator2, R.id.test_text2, R.id.test_title3, R.id.test_indicator3, R.id.test_text3};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1, test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3};

    private final Map<Integer, String> ANSWERCHOISES = new HashMap<Integer, String>() {{
        put(1, "Полностью не согласен");
        put(2, "Не согласен");
        put(3, "Скорее не согласен");
        put(4, "Затрудняюсь ответить");
        put(5, "Скорее согласен");
        put(6, "Согласен");
        put(7, "Полностью согласен");
    }};
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cognitive_flexibility_questionnaire);

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
        questions = new ArrayList<>();
        points = new ArrayList<>();
        transcoding = new HashMap<>();


        for (int i = 1, j = 7; i < 8; i++, j--) {
            transcoding.put(i, j);
        }

        for (int i = 0; i < plusСommonAgreePoints.length; i++) {
            plusСommonAgreePoints[i]--;
        }
        for (int i = 0; i < plusAlternativesAgreePoints.length; i++) {
            plusAlternativesAgreePoints[i]--;
        }
        for (int i = 0; i < plusControlAgreePoints.length; i++) {
            plusControlAgreePoints[i]--;
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

        //Подсчет: Шкала «Альтернативы»
        for (int i = 0; i < plusAlternativesAgreePoints.length; i++) {
            sumAlternativesScoringTest += points.get(plusAlternativesAgreePoints[i]);
        }

        //Подсчет: Шкала «Контроль»
        for (int i = 0; i < plusControlAgreePoints.length; i++) {
            sumControlScoringTest += transcoding.get(points.get(plusControlAgreePoints[i]));
        }
        sumControlScoringTest += points.get(plusControlAgreePoints[0]);
        sumControlScoringTest += points.get(plusControlAgreePoints[6]);

        //Подсчет: Общий коэффициент когнитивной флексибильности
        for (int i = 0; i < plusСommonAgreePoints.length; i++) {
            sumСommonScoringTest += points.get(plusСommonAgreePoints[i]);
        }
        for (int i = 0; i < plusControlAgreePoints.length; i++) {
            sumСommonScoringTest += transcoding.get(points.get(plusControlAgreePoints[i]));
        }

        textViews[0].setText("• \tШкала альтернативы: " + sumAlternativesScoringTest + " балла (ов)");
        if(sumAlternativesScoringTest >= 67){
            textViews[1].setText("Высокие показатели (67+) балла");
            textViews[2].setText(R.string.CFQAlternativeHighRes);
        }else if(sumAlternativesScoringTest >= 34){
            textViews[1].setText("Средние показатели (34 - 66) балла");
            textViews[2].setText(R.string.CFQAlternativeAverageRes);
        }else {
            textViews[1].setText("Низкие показатели (12 - 33) балла");
            textViews[2].setText(R.string.CFQAlternativeLowRes);
        }

        textViews[3].setText("• \tШкала контроля: " + sumControlScoringTest + " балла (ов)");
        if(sumControlScoringTest >= 44){
            textViews[4].setText("Высокие показатели (44+) балла");
            textViews[5].setText(R.string.CFQControlHighRes);
        }else if(sumControlScoringTest >= 23){
            textViews[4].setText("Средние показатели (23 - 43) балла");
            textViews[5].setText(R.string.CFQControlAverageRes);
        }else {
            textViews[4].setText("Низкие показатели (8 - 22) балла");
            textViews[5].setText(R.string.CFQControlLowRes);
        }

        textViews[6].setText("• \tОбщий коэффициент когнитивной флексибильности: " + sumControlScoringTest + " балла (ов)");
        if(sumControlScoringTest >= 112){
            textViews[7].setText("Высокие показатели (112+) балла");
            textViews[8].setText(R.string.CFQCommomHighRes);
        }else if(sumControlScoringTest >= 57){
            textViews[7].setText("Средние показатели (57 - 111) балла");
            textViews[8].setText(R.string.CFQCommomAverageRes);
        }else {
            textViews[7].setText("Низкие показатели (20 - 56) балла");
            textViews[8].setText(R.string.CFQCommomLowRes);
        }

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
}