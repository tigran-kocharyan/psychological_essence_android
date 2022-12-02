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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.hse.pe.R;
import ru.hse.pe.deprecated.Functions;
import ru.hse.pe.deprecated.Menu;
import ru.hse.pe.deprecated.Test;

public class Test_ImplicitTheoriesLearning extends AppCompatActivity {

    private List<String> questions;

    private int countOfQuestions = 28, counter = 1, counterQuestion = 0;
    private int currentProgress = 0;
    private int[] progressBarChecked = new int[countOfQuestions];

    private int sumIncreasingIntelligence = 0, sumEnrichedPersonality = 0,
            sumAcceptanceLearning = 0, sumAssessmentLearning = 0;
    private List<Integer> points;
    private int[] plusIncreasingIntelligencePoints = {8, 15, 22, 26};
    private int[] plusIncreasingIntelligencePointsR = {1, 6, 12, 19};
    private int[] plusEnrichedPersonalityPoints = {3, 14, 25, 28};
    private int[] plusEnrichedPersonalityPointsR = {7, 10, 18, 21};

    private int[] plusAcceptanceLearningPoints = {4, 17, 23};
    private int[] plusAcceptanceLearningPointsR = {11, 24, 27};
    private int[] plusAssessmentLearningPoints = {2, 9, 16};
    private int[] plusAssessmentLearningPointsR = {5, 13, 20, 27};
    private Map<Integer, Integer> transcoding;

    private ProgressBar progressBar;
    private RadioButton radioButton;
    private TextView nameTest;
    private Button btnSkip, btnFinish;
    private List<RadioGroup> radioGroups;
    private TextView textView, test_title1, test_title2, test_title3,
            test_indicator1, test_indicator2, test_indicator3,
            test_text1, test_text2, test_text3, test_title4, test_indicator4, test_text4;
    private TextView countOfProgress, question;
    private LinearLayout root, test_main, test_answers;
    private DatabaseReference mDataBase, commandsRef;
    private String TEST_KEY;

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1,
            R.id.test_title2, R.id.test_indicator2, R.id.test_text2, R.id.test_title3,
            R.id.test_indicator3, R.id.test_text3, R.id.test_title4,
            R.id.test_indicator4, R.id.test_text4};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3, test_title4, test_indicator4, test_text4};

    private final Map<Integer, String> ANSWERCHOISES = new HashMap<Integer, String>() {{
        put(1, "Абсолютно не согласен");
        put(2, "Не согласен");
        put(3, "Скорее не согласен");
        put(4, "Скорее согласен");
        put(5, "Согласен");
        put(6, "Абсолютно согласен");
    }};
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_implicit_theories_learning);

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
        transcoding = new HashMap<>();


        for (int i = 1, j = 6; i < 7; i++, j--) {
            transcoding.put(i, j);
        }

        Functions.minusIndex(plusIncreasingIntelligencePoints);
        Functions.minusIndex(plusIncreasingIntelligencePointsR);
        Functions.minusIndex(plusEnrichedPersonalityPoints);
        Functions.minusIndex(plusEnrichedPersonalityPointsR);
        Functions.minusIndex(plusAcceptanceLearningPoints);
        Functions.minusIndex(plusAcceptanceLearningPointsR);
        Functions.minusIndex(plusAssessmentLearningPoints);
        Functions.minusIndex(plusAssessmentLearningPointsR);

        getTestFromDB();

        radioGroups = new ArrayList<>();
        for (int i = 0; i < countOfQuestions; i++) {
            int id = getResources().getIdentifier("radioGroup" + i, "id", getPackageName());
            radioGroups.add((RadioGroup) findViewById(id));
        }


        for (int j = 0, radioGroupsSize = radioGroups.size(); j < radioGroupsSize; j++) {
            RadioGroup rg = radioGroups.get(j);
            if (j == 3){
                continue;
            }

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

            if(i == 3){
                String str1 = "Хорошую отметку", str2 = "Дискуссию";

                if(str1.contentEquals(radioButton.getText())){
                    points.add(6);
                }else{
                    points.add(1);
                }
                //points.add(0);
            }

            for (Map.Entry<Integer, String> pair : ANSWERCHOISES.entrySet()) {
                if(pair.getValue().contentEquals(radioButton.getText())){
                    points.add(pair.getKey());
                }
            }
        }

        // Подсчет: Шкала «Принятие теории «наращиваемого» интеллекта»
        for (int i = 0; i < plusIncreasingIntelligencePoints.length; i++) {
            sumIncreasingIntelligence += points.get(plusIncreasingIntelligencePoints[i]);
        }
        for (int i = 0; i < plusIncreasingIntelligencePointsR.length; i++) {
            sumIncreasingIntelligence += transcoding.get(points.get(plusIncreasingIntelligencePointsR[i]));
        }

        // Подсчет: Шкала «Принятие теории «обогащаемой» личности»
        for (int i = 0; i < plusEnrichedPersonalityPoints.length; i++) {
            sumEnrichedPersonality += points.get(plusEnrichedPersonalityPoints[i]);
        }
        for (int i = 0; i < plusEnrichedPersonalityPointsR.length; i++) {
            sumEnrichedPersonality += transcoding.get(points.get(plusEnrichedPersonalityPointsR[i]));
        }

        // Шкала «Принятие целей обучения»
        for (int i = 0; i < plusAcceptanceLearningPoints.length; i++) {
            sumAcceptanceLearning += points.get(plusAcceptanceLearningPoints[i]);
        }
        for (int i = 0; i < plusAcceptanceLearningPointsR.length; i++) {
            sumAcceptanceLearning += transcoding.get(points.get(plusAcceptanceLearningPointsR[i]));
        }
        // Шкала «Самооценка обучения»
        for (int i = 0; i < plusAssessmentLearningPoints.length; i++) {
            sumAssessmentLearning += points.get(plusAssessmentLearningPoints[i]);
        }
        for (int i = 0; i < plusAssessmentLearningPointsR.length; i++) {
            sumAssessmentLearning += transcoding.get(points.get(plusAssessmentLearningPointsR[i]));
        }

        textViews[0].setText("• \tШкала «Принятие теории «наращиваемого» интеллекта»: " + sumIncreasingIntelligence + " балла (ов)");
        if(sumIncreasingIntelligence >= 38){
            textViews[1].setText("Высокие показатели (38+) балла (ов)");
            textViews[2].setText(R.string.ITLIncreasingIntelligenceHighRes);
        }else if(sumIncreasingIntelligence >= 20){
            textViews[1].setText("Средние показатели (20 - 37) балла (ов)");
            textViews[2].setText(R.string.ITLIncreasingIntelligenceAverageRes);
        }else {
            textViews[1].setText("Низкие показатели (8 - 19) балла (ов)");
            textViews[2].setText(R.string.ITLIncreasingIntelligenceLowRes);
        }

        textViews[3].setText("• \tШкала «Принятие теории «обогащаемой» личности»: " + sumEnrichedPersonality + " балла (ов)");
        if(sumEnrichedPersonality >= 38){
            textViews[4].setText("Высокие показатели (38+) балла (ов)");
            textViews[5].setText(R.string.ITLEnrichedPersonalityHighRes);
        }else if(sumEnrichedPersonality >= 20){
            textViews[4].setText("Средние показатели (20 - 37) балла (ов)");
            textViews[5].setText(R.string.ITLEnrichedPersonalityAverageRes);
        }else {
            textViews[4].setText("Низкие показатели (8 - 19) балла (ов)");
            textViews[5].setText(R.string.ITLEnrichedPersonalityLowRes);
        }

        textViews[6].setText("• \tШкала «Принятие целей обучения»: " + sumAcceptanceLearning + " балла (ов)");
        if(sumAcceptanceLearning >= 28){
            textViews[7].setText("Высокие показатели (28+) балла (ов)");
            textViews[8].setText(R.string.ITLAcceptanceLearningHighRes);
        }else if(sumAcceptanceLearning >= 15){
            textViews[7].setText("Средние показатели (15 - 27) балла (ов)");
            textViews[8].setText(R.string.ITLAcceptanceLearningAverageRes);
        }else {
            textViews[7].setText("Низкие показатели (6 - 14) балла (ов)");
            textViews[8].setText(R.string.ITLAcceptanceLearningLowRes);
        }

        textViews[9].setText("• \tШкала «Самооценка обучения»: " + sumAssessmentLearning + " балла (ов)");
        if(sumAssessmentLearning >= 33){
            textViews[10].setText("Высокие показатели (33+) балла (ов)");
            textViews[11].setText(R.string.ITLAssessmentLearningHighRes);
        }else if(sumAssessmentLearning >= 15){
            textViews[10].setText("Средние показатели (17 - 32) балла (ов)");
            textViews[11].setText(R.string.ITLAssessmentLearningAverageRes);
        }else {
            textViews[10].setText("Низкие показатели (7 - 16) балла (ов)");
            textViews[11].setText(R.string.ITLAssessmentLearningLowRes);
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