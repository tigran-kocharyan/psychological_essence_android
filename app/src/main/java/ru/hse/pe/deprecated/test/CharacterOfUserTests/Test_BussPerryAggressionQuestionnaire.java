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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.hse.pe.R;
import ru.hse.pe.deprecated.Menu;
import ru.hse.pe.deprecated.Test;

public class Test_BussPerryAggressionQuestionnaire extends AppCompatActivity {
    private List<String> questions;
    private HashMap<Integer, Integer>  transcoding;

    private int sumPhysicalScoringTest = 0, sumAngerScoringTest = 0, sumHostilityScoringTest = 0;
    private List<Integer> points;
    private int[] plusPhysicalAgreePoints = new int[]{1, 4, 7, 10, 13, 16, 22, 24};
    private int[] plusAngerAgreePoints = new int[]{2, 5, 8, 14, 17, 20};
    private int[] plusHostilityAgreePoints = new int[]{3, 6, 9, 12, 15, 18, 21, 23};


    private int countOfQuestions = 24, counter = 1, counterQuestion = 0,  transcodingvar = 5;
    private int currentProgress = 0;
    private int[] progressBarChecked = new int[countOfQuestions];

    private ProgressBar progressBar;
    private RadioButton radioButton;
    private TextView nameTest;
    private Button btnSkip, btnFinish;
    private List<RadioGroup> radioGroups;
    private TextView test_title1, test_title2, test_title3,
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
        put(2, "Скорее не согласен");
        put(3, "Затрудняюсь ответить");
        put(4, "Скорее согласен");
        put(5, "Полностью согласен");
    }};
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_buss_perry_aggression_questionnaire);

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

        for (int i = 0; i < plusPhysicalAgreePoints.length; i++) {
            plusPhysicalAgreePoints[i] -= 1;
        }
        for (int i = 0; i < plusAngerAgreePoints.length; i++) {
            plusAngerAgreePoints[i] -= 1;
        }
        for (int i = 0; i < plusHostilityAgreePoints.length; i++) {
            plusHostilityAgreePoints[i] -= 1;
        }

        btnSkip = findViewById(R.id.btnSkip);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressTTU);
        countOfProgress = findViewById(R.id.countOfProgress);
        question = findViewById(R.id.question);
        questions = new ArrayList<>();
        points = new ArrayList<>();
        transcoding = new HashMap<>();
        for (int i = 1, j = transcodingvar; i < transcodingvar; i++, j--) {
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


        // Считаем физическую агрессию
        for (int i = 0; i < plusPhysicalAgreePoints.length; i++) {
            sumPhysicalScoringTest += points.get(plusPhysicalAgreePoints[i]);
        }
        sumPhysicalScoringTest += transcoding.get(points.get(19 - 1));


        textViews[0].setText("Физическая агрессия: " + sumPhysicalScoringTest + " балла (ов)");
        if(sumPhysicalScoringTest >= 29){
            textViews[1].setText("Высокие показатели (29-45) баллов");
            textViews[2].setText(R.string.BPAQPhysicalHighRes);
        }else if(sumPhysicalScoringTest >= 15){
            textViews[1].setText("Средние показатели (15-28) баллов");
            textViews[2].setText(R.string.BPAQPhysicalAverageRes);
        }else{
            textViews[1].setText("Низкие показатели (9-14) баллов");
            textViews[2].setText(R.string.BPAQPhysicalLowRes);
        }

        // Считаем гнев
        for (int i = 0; i < plusAngerAgreePoints.length; i++) {
            sumAngerScoringTest += points.get(plusAngerAgreePoints[i]);
        }
        sumAngerScoringTest += transcoding.get(points.get(11 - 1));

        textViews[3].setText("Гнев: " + sumAngerScoringTest + " балла (ов)");
        if(sumAngerScoringTest >= 29){
            textViews[4].setText("Высокие показатели (29-35) баллов");
            textViews[5].setText(R.string.BPAQAngerHighRes);
        }else if(sumAngerScoringTest >= 17){
            textViews[4].setText("Средние показатели (17-28) баллов");
            textViews[5].setText(R.string.BPAQAngerAverageRes);
        }else{
            textViews[4].setText("Низкие показатели (7-16) баллов");
            textViews[5].setText(R.string.BPAQAngerLowRes);
        }

        // Считаем враждебность
        for (int i = 0; i < plusHostilityAgreePoints.length; i++) {
            sumHostilityScoringTest += points.get(plusHostilityAgreePoints[i]);
        }
        textViews[6].setText("Враждебность: " + sumHostilityScoringTest + " балла (ов)");
        if(sumHostilityScoringTest >= 29){
            textViews[7].setText("Высокие показатели (29-40) баллов");
            textViews[8].setText(R.string.BPAQHostilityHighRes);
        }else if(sumHostilityScoringTest >= 17){
            textViews[7].setText("Средние показатели (17-28) баллов");
            textViews[8].setText(R.string.BPAQHostilityAverageRes);
        }else{
            textViews[7].setText("Низкие показатели (8-16) баллов");
            textViews[8].setText(R.string.BPAQHostilityLowRes);
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


