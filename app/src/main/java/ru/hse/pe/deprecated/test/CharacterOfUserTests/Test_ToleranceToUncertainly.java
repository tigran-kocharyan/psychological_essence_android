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

import ru.hse.pe.Menu;
import ru.hse.pe.R;
import ru.hse.pe.Test;

public class Test_ToleranceToUncertainly extends AppCompatActivity {
    private List<String> questions;

    private List<Integer> points, scoringPointsTolToUn, scoringPointsIntolToUn, scoringPointsIntertolToUn;
    private int sumScoringPointsTolToUn = 0, sumScoringPointsIntolToUn = 0, sumScoringPointsIntertolToUn= 0;
    private String strScoringPointsTolToUn = "", strScoringPointsIntolToUn = "", strScoringPointsIntertolToUn= "";
    private Map<Integer, Integer> transcoding;

    private int countOfQuestions = 33, counter = 1, counterQuestion = 0;
    private int currentProgress = 0;
    private int[] progressBarChecked = new int[countOfQuestions];


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
        setContentView(R.layout.activity_test_tolerance_to_uncertainly);

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
        scoringPointsTolToUn = new ArrayList<>();
        scoringPointsIntolToUn = new ArrayList<>();
        scoringPointsIntertolToUn = new ArrayList<>();
        transcoding = new HashMap<>();


        for (int i = 1, j = 7; i < 8; i++, j--) {
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

        countOfProgress = findViewById(R.id.countOfProgress);
        question = findViewById(R.id.question);

        for (int i = 0; i < progressBarChecked.length; i++) {
            progressBarChecked[i] = -1;
        }
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

         //Подсчет: Толерантность к неопределенности
        scoringPointsTolToUn.add(points.get(5));
        scoringPointsTolToUn.add(points.get(15));
        scoringPointsTolToUn.add(points.get(16));
        for (int i = 27; i <= 32; i++) {
            scoringPointsTolToUn.add(points.get(i));
        }

        for (int i = 0; i < scoringPointsTolToUn.size(); i++) {
            sumScoringPointsTolToUn += scoringPointsTolToUn.get(i);
        }

        sumScoringPointsTolToUn += transcoding.get(points.get(14));
        sumScoringPointsTolToUn += transcoding.get(points.get(23));
        sumScoringPointsTolToUn += transcoding.get(points.get(24));
        // Первый посчитали


        // Подсчет: Интолерантность к неопределенности
        for (int i = 0; i <= 4; i++) {
            scoringPointsIntolToUn.add(points.get(i));
        }
        scoringPointsIntolToUn.add(points.get(9));
        scoringPointsIntolToUn.add(points.get(11));
        scoringPointsIntolToUn.add(points.get(13));
        scoringPointsIntolToUn.add(points.get(17));
        scoringPointsIntolToUn.add(points.get(20));
        scoringPointsIntolToUn.add(points.get(21));
        scoringPointsIntolToUn.add(points.get(22));
        scoringPointsIntolToUn.add(points.get(26));
        for (int i = 0; i < scoringPointsIntolToUn.size(); i++) {
            sumScoringPointsIntolToUn += scoringPointsIntolToUn.get(i);
        }
        // Второй посчитали

        //Подсчет: Межличностная интолерантность к неопределенности
        scoringPointsIntertolToUn.add(points.get(6));
        scoringPointsIntertolToUn.add(points.get(7));
        scoringPointsIntertolToUn.add(points.get(8));
        scoringPointsIntertolToUn.add(points.get(10));
        scoringPointsIntertolToUn.add(points.get(12));
        scoringPointsIntertolToUn.add(points.get(18));
        scoringPointsIntertolToUn.add(points.get(19));
        scoringPointsIntertolToUn.add(points.get(25));
        for (int i = 0; i < scoringPointsIntertolToUn.size(); i++) {
            sumScoringPointsIntertolToUn += points.get(i);
        }
        // Третий посчитали

        textViews[0].setText("• \tТолерантность к неопределенности: " + sumScoringPointsTolToUn + " балла (ов)");
        if(sumScoringPointsTolToUn >= 61){
            textViews[1].setText("Высокие показатели (61 - 84) балла");
            textViews[2].setText(R.string.ttuHighDes);
        }else if(sumScoringPointsTolToUn >= 36){
            textViews[1].setText("Средние показатели (36–60 баллов)");
            textViews[2].setText(R.string.ttuAverageDes);
        }else {
            textViews[1].setText("Низкие показатели (12 - 35)");
            textViews[2].setText(R.string.ttuLowDes);
        }

        textViews[3].setText("• \tИнтолерантность к неопределенности: " + sumScoringPointsIntolToUn + " балла (ов)");
        if(sumScoringPointsIntolToUn >= 66){
            textViews[4].setText("Высокие показатели (66–91 балла)");
            textViews[5].setText(R.string.ttunHighDes);
        }else if(sumScoringPointsIntolToUn >= 39){
            textViews[4].setText("Средние показатели (39–65 баллов)");
            textViews[5].setText(R.string.ttunAverageDes);
        }else {
            textViews[4].setText("Низкие показатели (13 - 38)");
            textViews[5].setText(R.string.ttunLowDes);
        }

        textViews[6].setText("• \tМежличностная интолерантность к неопределенности: " + sumScoringPointsIntertolToUn + " балла (ов)");
        if(sumScoringPointsIntertolToUn >= 41){
            textViews[7].setText("Низкие показатели (41 - 56)");
            textViews[8].setText(R.string.ttuinterPersLowDes);
        }else if(sumScoringPointsIntertolToUn >= 24){
            textViews[7].setText("Средние показатели (24–40 баллов)");
            textViews[8].setText(R.string.ttuinterPersAverageDes);
        }else {
            textViews[7].setText("Высокие показатели (8–23 балла)");
            textViews[8].setText(R.string.ttuinterPersHighDes);
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