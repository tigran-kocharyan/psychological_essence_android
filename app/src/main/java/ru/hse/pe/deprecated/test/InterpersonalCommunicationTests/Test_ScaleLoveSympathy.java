package ru.hse.pe.deprecated.test.InterpersonalCommunicationTests;

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

public class Test_ScaleLoveSympathy extends AppCompatActivity {

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

    private int countOfQuestions = 14, counter = 1, counterQuestion = 0;
    private int[] progressBarChecked = new int[countOfQuestions];
    private int currentProgress = 0;
    private int sumScoringTest = 0;
    private TextView textView, test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3;

    private int sumLovePoints = 0, sumSympathyPoints = 0, sumCommonpoints =  0;
    private int[] plusLovePoints = new int[]{1, 3, 5, 7, 9, 11, 13};
    private int[] plusSympathyPoints = new int[]{2, 4, 6, 8, 10, 12, 14};
    private int[] plusCommonPoints = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};




    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1,
            R.id.test_title2, R.id.test_indicator2, R.id.test_text2,
            R.id.test_title3, R.id.test_indicator3, R.id.test_text3};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3};

    private final Map<Integer, String> ANSWERCHOISES = new HashMap<Integer, String>() {{
        put(1, "Это совсем не так");
        put(2, "Вряд ли это так");
        put(3, "Вероятно это так");
        put(4, "Да, это так");
    }};
    private HashMap<Integer, Integer> transcoding;
    private int transcodingvar = 3;
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scale_love_sympathy);

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

        Functions.minusIndex(plusLovePoints);
        Functions.minusIndex(plusSympathyPoints);
        Functions.minusIndex(plusCommonPoints);

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

        //Подсчет: Любовь
        countingLove(pressedRB);
        //Подсчет: Симпатия
        countingSympathy(pressedRB);
        //Подсчет: Общий балл
        countingCommon(pressedRB);

        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
    }

    private void countingLove(List<RadioButton> pressedRB) {
        for (int i = 0; i < plusLovePoints.length; i++) {
            sumLovePoints += points.get(plusLovePoints[i]);
        }

        textViews[0].setText("Ваш балл по шкале любви: " + sumLovePoints);
        if(sumLovePoints >= 25){
            textViews[1].setText("Высокие показатели: (25 - 28) балла (ов)");
            textViews[2].setText(R.string.SLSLoveHighRes);
        }else if(sumLovePoints >= 21){
            textViews[1].setText("Повышенный показатель: (21 - 24) балла (ов)");
            textViews[2].setText(R.string.SLSLoveHighRes);
        }else if(sumLovePoints >= 15){
            textViews[1].setText("Средний показатель: (15 - 20) балла (ов)");
            textViews[2].setText(R.string.SLSLoveAverageRes);
        }else if(sumLovePoints >= 11){
            textViews[1].setText("Пониженный показатель: (11 - 14) балла (ов)");
            textViews[2].setText(R.string.SLSLoveLowRes);
        }else{
            textViews[1].setText("Низкий показатель: (7 - 10) балла (ов)");
            textViews[2].setText(R.string.SLSLoveLowRes);
        }

    }

    private void countingSympathy(List<RadioButton> pressedRB) {
        for (int i = 0; i < plusSympathyPoints.length; i++) {
            sumSympathyPoints += points.get(plusSympathyPoints[i]);
        }


        textViews[3].setText("Ваш балл по шкале симпатии: " + sumSympathyPoints);
        if(sumSympathyPoints >= 25){
            textViews[4].setText("Высокие показатели: (25 - 28) балла (ов)");
            textViews[5].setText(R.string.SLSSympathyHighRes);
        }else if(sumSympathyPoints >= 21){
            textViews[4].setText("Повышенный показатель: (21 - 24) балла (ов)");
            textViews[5].setText(R.string.SLSSympathyHighRes);
        }else if(sumSympathyPoints >= 15){
            textViews[4].setText("Средний показатель: (15 - 20) балла (ов)");
            textViews[5].setText(R.string.SLSSympathyAverageRes);
        }else if(sumSympathyPoints >= 11){
            textViews[4].setText("Пониженный показатель: (11 - 14) балла (ов)");
            textViews[5].setText(R.string.SLSSympathyLowRes);
        }else{
            textViews[4].setText("Низкий показатель: (7 - 10) балла (ов)");
            textViews[5].setText(R.string.SLSSympathyLowRes);
        }
    }

    private void countingCommon(List<RadioButton> pressedRB) {
        for (int i = 0; i < plusCommonPoints.length; i++) {
            sumCommonpoints += points.get(plusCommonPoints[i]);
        }

        textViews[6].setText("Ваш балл по шкале симпатии: " + sumCommonpoints);
        if(sumCommonpoints >= 50){
            textViews[7].setText("Высокие показатели: (50 - 56) балла (ов)");
            textViews[8].setText(R.string.SLSСommonHighRes);
        }else if(sumCommonpoints >= 42){
            textViews[7].setText("Повышенный показатель: (42 - 49) балла (ов)");
            textViews[8].setText(R.string.SLSСommonHighRes);
        }else if(sumCommonpoints >= 30){
            textViews[7].setText("Средний показатель: (30 - 41) балла (ов)");
            textViews[8].setText(R.string.SLSСommonAverageRes);
        }else if(sumCommonpoints >= 22){
            textViews[7].setText("Пониженный показатель: (22 - 29) балла (ов)");
            textViews[8].setText(R.string.SLSСommonLowRes);
        }else{
            textViews[7].setText("Низкий показатель: (14 - 21) балла (ов)");
            textViews[8].setText(R.string.SLSСommonLowRes);
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