package com.example.pe.PsychologicalState;

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
import java.util.Objects;

public class Test_BecksDepressionScale extends AppCompatActivity {
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

    private int countOfQuestions = 21, counter = 1, counterQuestion = 0;
    private int[] progressBarChecked = new int[countOfQuestions];
    private int currentProgress = 0;
    private int sumScoringTest = 0;
    private TextView textView, test_title1, test_indicator1, test_text1;

    private int sumCommonPoints = 0;
    private int[] plusCommonPoints = new int[countOfQuestions];


    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1};

    private Map<Integer, Map<Integer, String>> answerChoises = new HashMap<>();
    private String typeOfTest;

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
        mDataBase = FirebaseDatabase.getInstance("https://zeta-turbine-297107-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/Tests/" + typeOfTest + "/" + TEST_KEY);


        test_main = findViewById(R.id.test_main);
        test_answers = findViewById(R.id.test_answers);
        for (int i = 0; i < ids.length; i++) {
            textViews[i] = findViewById(ids[i]);
        }

        for (int i = 0; i < countOfQuestions; i++) {
            plusCommonPoints[i] = i;
        }

        btnSkip = findViewById(R.id.btnSkip);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressTTU);
        progressBar.setMax(countOfQuestions);
        questions = new ArrayList<>();
        points = new ArrayList<>();
        countOfProgress = findViewById(R.id.countOfProgress);
        question = findViewById(R.id.question);


        getAnswersFromDB();
        getQuestionFromDB();

        radioGroups = new ArrayList<>();
        for (int i = 0; i < countOfQuestions; i++) {
            int id = getResources().getIdentifier("radioGroup" + i, "id", getPackageName());
            radioGroups.add((RadioGroup) findViewById(id));
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
            countOfProgress.setText(counter + "/" + countOfQuestions);
        }
    }


    public void btnGo(View view){
        if (radioGroups.get(counterQuestion).getCheckedRadioButtonId() == -1) {
            Snackbar.make(root, "Выберите вариант ответа или нажмите кнопку пропустить", Snackbar.LENGTH_SHORT).show();
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
        countOfProgress.setText(counter + "/" + countOfQuestions);
    }

    public void btnFinish(View view){
        List<RadioButton> pressedRB = new ArrayList<>();
        for (int i = 0; i < countOfQuestions; i++) {
            radioButton = (RadioButton) findViewById(radioGroups.get(i).getCheckedRadioButtonId());
            pressedRB.add(radioButton);
        }

        // Подсчет: Депрессию (общий)
        countingCommon(pressedRB);

        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
    }

    private void countingCommon(List<RadioButton> pressedRB) {
        for (int i = 0; i < countOfQuestions; i++) {
            radioButton = (RadioButton) findViewById(radioGroups.get(i).getCheckedRadioButtonId());

            HashMap<Integer, String> map = (HashMap<Integer, String>) answerChoises.get(i);

            assert map != null;
            for (Map.Entry<Integer, String> pair : map.entrySet()) {
                if(pair.getValue().contentEquals(radioButton.getText())){
                    points.add(pair.getKey());
                }
            }
        }

        for (int i = 0; i < points.size(); i++) {
            sumCommonPoints += points.get(i);
        }

        textViews[0].setText("Ваш балл выраженности: " + sumCommonPoints + " (из 63 возможных)");
        if(sumCommonPoints >= 23){
            textViews[1].setText("Тяжелая депрессия");
        }else if(sumCommonPoints >= 20){
            textViews[1].setText("Умеренное депрессивное состояние");
        }else if(sumCommonPoints >= 14){
            textViews[1].setText("Легкое депрессивное состояние");
        }else{
            textViews[1].setText("Норма");
        }
        textViews[2].setText(R.string.BDSRes);
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

    private void getAnswersFromDB(){
        commandsRef = mDataBase.child("Answers");
        ValueEventListener valueEventListener  = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str;
                for(DataSnapshot ds : snapshot.getChildren()){
                    HashMap<Integer, String> hm = new HashMap<>();
                    for (int j = 0; j < 4; j++) {
                        str = ds.child(String.valueOf(j)).getValue(String.class);
                        questions.add(str);
                        hm.put(j, str);
                        answerChoises.put(Integer.valueOf(Objects.requireNonNull(ds.getKey())), hm);
                    }
                }
                countOfProgress.setText(counter + "/" + countOfQuestions);

                int k = 0;
                for (int j = 0; j < radioGroups.size(); j++) {
                    RadioGroup rg = radioGroups.get(j);
                    for (int i = 0; i < rg.getChildCount(); i++, k++) {
                        RadioButton radioButton = (RadioButton) rg.getChildAt(i);
                        radioButton.setText(Objects.requireNonNull(answerChoises.get(j)).get(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }

        };
        commandsRef.addValueEventListener(valueEventListener);
    };

    private void getQuestionFromDB(){
        commandsRef = mDataBase.child("Question");
        ValueEventListener valueEventListener  = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str = snapshot.getValue(String.class);
                question.setText(str);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }

        };
        commandsRef.addValueEventListener(valueEventListener);
    };
}