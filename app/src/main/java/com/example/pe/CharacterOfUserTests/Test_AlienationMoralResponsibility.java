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

public class Test_AlienationMoralResponsibility extends AppCompatActivity {
    private List<String> questions;

    private int countOfQuestions = 24, counter = 1, counterQuestion = 0;
    private int currentProgress = 0;
    private int[] progressBarChecked = new int[countOfQuestions];

    private int sumMoralJustScoringTest = 0, sumEupLabelScoringTest = 0, sumAdvCompScoringTest = 0,
            sumShiftingRespScoringTest = 0, sumDispResScoringTest = 0, sumDiscConseqScoringTest = 0,
            sumDehumanizationScoringTest = 0, sumAttGuiltScoringTest = 0,  sumCommanPoints = 0;
    private List<Integer> points;
    private int[] plusCommanPoints = new int[countOfQuestions];
    private Map<Integer, Integer> transcoding;

    private ProgressBar progressBar;
    private RadioButton radioButton;
    private TextView nameTest;
    private Button btnSkip, btnFinish;
    private List<RadioGroup> radioGroups;
    private TextView textView, test_title1, test_title2, test_title3, test_title4, test_title5,
            test_title6, test_concrete1, test_concrete2, test_title7, test_title8, test_title9,
            test_text1, test_text2, test_text3, test_text4, test_text5, test_text6, test_text7,
            test_text8, test_text9;
    private TextView countOfProgress, question;
    private LinearLayout root, test_main, test_answers;
    private DatabaseReference mDataBase, commandsRef;
    private String TEST_KEY, typeOfTest;

    private final int[] ids = new int[]{R.id.test_title1,  R.id.test_text1, R.id.test_title2,
            R.id.test_text2, R.id.test_title3, R.id.test_text3, R.id.test_title4,  R.id.test_text4,
            R.id.test_title5, R.id.test_text5, R.id.test_title6, R.id.test_text6, R.id.test_title7,
            R.id.test_text7, R.id.test_title8,  R.id.test_text8, R.id.test_title9, R.id.test_text9};
    private final TextView[] textViews = new TextView[]{test_title1,  test_text1, test_title2,
            test_text2, test_title3, test_text3, test_title4,  test_text4, test_title5, test_text5,
            test_title6, test_text6, test_title7, test_text7, test_title8,  test_text8, test_title9,
            test_text9};

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
        setContentView(R.layout.activity_test_alienation_moral_responsibility);

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
        test_concrete1 = findViewById(R.id.test_concrete1);
        test_concrete2 = findViewById(R.id.test_concrete2);

        btnSkip = findViewById(R.id.btnSkip);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressTTU);
        questions = new ArrayList<>();
        points = new ArrayList<>();
        transcoding = new HashMap<>();


        for (int i = 1, j = 7; i < 8; i++, j--) {
            transcoding.put(i, j);
        }

        for (int i = 0; i < plusCommanPoints.length; i++) {
            plusCommanPoints[i] = i;
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

        //Подсчет: 	Весь тест
        for (int i = 0; i < plusCommanPoints.length; i++) {
            sumCommanPoints += points.get(plusCommanPoints[i]);
        }
        textViews[0].setText("Ваш итоговый балл склонности к отчуждению моральной ответственности: " + sumCommanPoints + " (из 168 возможных)");
        textViews[1].setText(R.string.AMRCommon);

        test_concrete1.setText("Ваши баллы по конкретным механизмам отчуждения моральной ответственности: ");
        test_concrete2.setText("Чем выше балл по определенному механизму, тем выраженнее использование именно этого механизма, тем чаще вы применяли/будете склонны применять его для объяснения своих поступков (или других людей).");
        //Подсчет: 	Моральное оправдание
        sumMoralJustScoringTest += points.get(0);
        sumMoralJustScoringTest += points.get(1);
        sumMoralJustScoringTest += points.get(2);
        textViews[2].setText("•\t Моральное оправдание: " + sumMoralJustScoringTest + " (из 21 возможного)");
        textViews[3].setText(R.string.AMRMoralJust);

        //Подсчет: 	Эвфимистический ярлык
        sumEupLabelScoringTest += points.get(3);
        sumEupLabelScoringTest += points.get(4);
        sumEupLabelScoringTest += points.get(5);
        textViews[4].setText("•\t Эвфимистический ярлык: " + sumEupLabelScoringTest + " (из 21 возможного)");
        textViews[5].setText(R.string.AMREupLabel);
        //Подсчет: 	Выгодное сравнение
        sumAdvCompScoringTest += points.get(6);
        sumAdvCompScoringTest += points.get(7);
        sumAdvCompScoringTest += points.get(8);
        textViews[6].setText("•\tВыгодное сравнение: " + sumAdvCompScoringTest + " (из 21 возможного)");
        textViews[7].setText(R.string.AMRAdvComp);
        //Подсчет: Смещение ответственности
        sumShiftingRespScoringTest += points.get(9);
        sumShiftingRespScoringTest += points.get(10);
        sumShiftingRespScoringTest += points.get(11);
        textViews[8].setText("•\t Смещение  ответственности" + sumShiftingRespScoringTest + " (из 21 возможного)");
        textViews[9].setText(R.string.AMRShiftingResp);
        //Подсчет: Рассеивание ответственности
        sumDispResScoringTest += points.get(12);
        sumDispResScoringTest += points.get(13);
        sumDispResScoringTest += points.get(14);
        textViews[10].setText("•\t Рассеивание  ответственности:" + sumDispResScoringTest + " (из 21 возможного)");
        textViews[11].setText(R.string.AMRDispRes);
        //Подсчет: Рассеивание ответственности
        sumDiscConseqScoringTest += points.get(15);
        sumDiscConseqScoringTest += points.get(16);
        sumDiscConseqScoringTest += points.get(17);
        textViews[12].setText("•\t Искажение последствий: " + sumDiscConseqScoringTest + " (из 21 возможного)");
        textViews[13].setText(R.string.AMRDiscConseq);
        //Подсчет: Дегуманизация
        sumDehumanizationScoringTest += points.get(18);
        sumDehumanizationScoringTest += points.get(19);
        sumDehumanizationScoringTest += points.get(20);
        textViews[14].setText("•\t Дегуманизация " + sumDehumanizationScoringTest + " (из 21 возможного)");
        textViews[15].setText(R.string.AMRDehumanization);
        //Подсчет: Атрибуция вины
        sumAttGuiltScoringTest += points.get(21);
        sumAttGuiltScoringTest += points.get(22);
        sumAttGuiltScoringTest += points.get(23);
        textViews[14].setText("•\t Атрибуция вины " + sumAttGuiltScoringTest + " (из 21 возможного)");
        textViews[15].setText(R.string.AMRAttGuilt);


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