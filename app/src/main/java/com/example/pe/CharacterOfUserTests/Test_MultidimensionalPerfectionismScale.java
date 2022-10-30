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

public class Test_MultidimensionalPerfectionismScale extends AppCompatActivity {

    private List<String> questions;

    private int countOfQuestions = 45, counter = 1, counterQuestion = 0;
    private int currentProgress = 0;
    private int[] progressBarChecked = new int[countOfQuestions];

    private int sumSelfCenPerfectionism = 0, sumOthersPerfectionism = 0,
            sumSocialPerfectionism = 0, sumIntegralScale = 0;
    private List<Integer> points;
    
    private int[] plusSelfCenPerfectionism = new int[]{1, 6, 14, 15, 17, 20, 23, 28, 32, 40, 42};
    private int[] plusSelfCenPerfectionismR = new int[]{8, 12, 34, 36};

    private int[] plusOthersPerfectionism = new int[]{7, 16, 22, 26, 27, 29};
    private int[] plusOthersPerfectionismR = new int[]{2, 3, 4, 10, 19, 24, 38, 43, 45};


    private int[] plusSocialPerfectionism = new int[]{5, 11, 13, 18, 25, 31, 33, 35, 39, 41};
    private int[] plusSocialPerfectionismR = new int[]{9, 21, 30, 37, 44};

    private int[] plusIntegralScale = new int[]{1, 5, 6, 7, 11, 13, 14, 15, 16, 17, 18, 20, 22, 23,
            25, 26, 27, 28, 29, 31, 32, 33, 35, 38, 29, 39, 40, 41, 42};
    private int[] plusIntegralScaleR = new int[]{2, 3, 4, 8, 9, 10, 12, 19, 21, 24, 30, 34, 36, 37,
            38, 43, 44, 45};

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

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1, R.id.test_title2,
            R.id.test_indicator2, R.id.test_text2, R.id.test_title3, R.id.test_indicator3, R.id.test_text3,
    R.id.test_title4, R.id.test_indicator4, R.id.test_text4};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1, test_title2,
            test_indicator2, test_text2, test_title3, test_indicator3,
            test_text3, test_title4, test_indicator4, test_text4};

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
        setContentView(R.layout.activity_test_multidimensional_perfectionism_scale);

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


        for (int i = 1, j = 7; i < 8; i++, j--) {
            transcoding.put(i, j);
        }

        Functions.minusIndex(plusIntegralScale);
        Functions.minusIndex(plusIntegralScaleR);

        Functions.minusIndex(plusSelfCenPerfectionism);
        Functions.minusIndex(plusSelfCenPerfectionismR);

        Functions.minusIndex(plusOthersPerfectionism);
        Functions.minusIndex(plusOthersPerfectionismR);

        Functions.minusIndex(plusSocialPerfectionism);
        Functions.minusIndex(plusSocialPerfectionismR);

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

        //Подсчет: Интегральная шкала перфекционизма
        for (int i = 0; i < plusIntegralScale.length; i++) {
            sumIntegralScale += points.get(plusIntegralScale[i]);
        }
        for (int i = 0; i < plusIntegralScaleR.length; i++) {
            sumIntegralScale += transcoding.get(points.get(plusIntegralScaleR[i]));
        }
        textViews[0].setText("•\tИнтегральная шкала перфекционизма: " + sumIntegralScale + " балла (ов)");
        if(sumIntegralScale >= 205){
            textViews[1].setText("Высокие показатели: (205-322) балла (ов)");
            textViews[2].setText(R.string.MPSISHighRes);
        }else if(sumIntegralScale >= 160){
            textViews[1].setText("Средние показатели: (160-204) балла (ов)");
            textViews[2].setText(R.string.MPSISAverageRes);
        }else{
            textViews[1].setText("Низкие показатели: (0-159) балла (ов)");
            textViews[2].setText(R.string.MPSISLowRes);
        }

        //Подсчет: Ориентированный на себя:
        for (int i = 0; i < plusSelfCenPerfectionism.length; i++) {
            sumSelfCenPerfectionism += points.get(plusSelfCenPerfectionism[i]);
        }
        for (int i = 0; i < plusSelfCenPerfectionismR.length; i++) {
            sumSelfCenPerfectionism += transcoding.get(points.get(plusSelfCenPerfectionismR[i]));
        }
        textViews[3].setText("•\tПерфекционизм, ориентированный на себя: " + sumSelfCenPerfectionism + " балла (ов)");
        if(sumSelfCenPerfectionism >= 84){
            textViews[4].setText("Высокие показатели: (84-105) балла (ов)");
            textViews[5].setText(R.string.MPSSCPHighRes);
        }else if(sumSelfCenPerfectionism >= 49){
            textViews[4].setText("Средние показатели: (49-83) балла (ов)");
            textViews[5].setText(R.string.MPSSCPAverageRes);
        }else{
            textViews[4].setText("Низкие показатели: (0-48) балла (ов)");
            textViews[5].setText(R.string.MPSSCPLowRes);
        }

        //Подсчет: Ориентированный на других:
        for (int i = 0; i < plusOthersPerfectionism.length; i++) {
            sumOthersPerfectionism += points.get(plusOthersPerfectionism[i]);
        }
        for (int i = 0; i < plusOthersPerfectionismR.length; i++) {
            sumOthersPerfectionism += transcoding.get(points.get(plusOthersPerfectionismR[i]));
        }
        textViews[6].setText("•\tПерфекционизм, ориентированный на других: " + sumOthersPerfectionism + " балла (ов)");
        if(sumOthersPerfectionism >= 69){
            textViews[7].setText("Высокие показатели: (69-105) балла (ов)");
            textViews[8].setText(R.string.MPSOthersHighRes);
        }else if(sumOthersPerfectionism >= 43){
            textViews[7].setText("Средние показатели: (43-69) балла (ов)");
            textViews[8].setText(R.string.MPSOthersAverageRes);
        }else{
            textViews[7].setText("Низкие показатели: (0-42) балла (ов)");
            textViews[8].setText(R.string.MPSOthersLowRes);
        }

        //Подсчет: Социально предписанный:
        for (int i = 0; i < plusSocialPerfectionism.length; i++) {
            sumSocialPerfectionism += points.get(plusSocialPerfectionism[i]);
        }
        for (int i = 0; i < plusSocialPerfectionismR.length; i++) {
            sumSocialPerfectionism += transcoding.get(points.get(plusSocialPerfectionismR[i]));
        }

        textViews[9].setText("•\tСоциально предписанный перфекционизм: " + sumSocialPerfectionism + " балла (ов)");
        if(sumSocialPerfectionism >= 66){
            textViews[10].setText("Высокие показатели: (66-105) балла (ов)");
            textViews[11].setText(R.string.MPSSCENHighRes);
        }else if(sumSocialPerfectionism >= 35){
            textViews[10].setText("Средние показатели: (35-65) балла (ов)");
            textViews[11].setText(R.string.MPSSCENOthersAverageRes);
        }else{
            textViews[10].setText("Низкие показатели: (0-34) балла (ов)");
            textViews[11].setText(R.string.MPSSCENOthersLowRes);
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