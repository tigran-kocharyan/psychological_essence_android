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
import java.util.List;

import ru.hse.pe.Functions;
import ru.hse.pe.Menu;
import ru.hse.pe.R;
import ru.hse.pe.Test;

public class Test_PEA extends AppCompatActivity {
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


    private int countOfQuestions = 45, counter = 1, counterQuestion = 0;
    private int[] progressBarChecked = new int[countOfQuestions];
    private int currentProgress = 0;
    private int sumScoringTest = 0;
    private TextView textView, test_title1, test_indicator1, test_text1,
        test_title2, test_indicator2, test_text2,
        test_title3, test_indicator3, test_text3;
    private int[] plusUnderstanding = new int[]{1, 3, 5, 7, 10, 12, 13};
    private int[] minusUnderstanding = new int[]{2, 4, 6, 7, 9, 11, 14, 15};
    private float sumUnderstanding = 0;

    private int[] plusEmotionalAt = new int[]{16, 17, 20, 22, 23, 25, 26, 28};
    private int[] minusEmotionalAt = new int[]{18, 19, 21, 24, 27, 29, 30};
    private float sumEmotionalAt = 0;

    private int[] plusAuthority = new int[]{31, 32, 33, 34, 35, 36, 37, 39, 41, 42, 45};
    private int[] minusAuthority = new int[]{38, 40, 43, 44};
    private float sumAuthority = 0;

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1,
            R.id.test_title2, R.id.test_indicator2, R.id.test_text2,
            R.id.test_title3, R.id.test_indicator3, R.id.test_text3};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1,
            test_title2, test_indicator2, test_text2,
            test_title3, test_indicator3, test_text3};
    private final String[] ANSWERCHOISES = new String[]{"Не согласен", "Затрудняюсь ответить",
            "Согласен"};
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pea);

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
            for (int i = 0, k = 0; i < rg.getChildCount(); i++, k++) {
                RadioButton radioButton = (RadioButton) rg.getChildAt(i);
                radioButton.setText(ANSWERCHOISES[k]);
            }
        }

        Functions.minusIndex(plusEmotionalAt);
        Functions.minusIndex(minusEmotionalAt);
        Functions.minusIndex(plusAuthority);
        Functions.minusIndex(minusAuthority);
        Functions.minusIndex(plusUnderstanding);
        Functions.minusIndex(minusUnderstanding);

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
            pressedRB.add(radioButton);
        }


        // Считаем Деструктивная сверхзависимость (мужчины/женщины)
        countingUnderstanding(pressedRB);
        // Считаем Дисфункциональное отделение
        countingEmotionalAt(pressedRB);
        // Считаем Здоровая зависимость
        countingAuthority(pressedRB);



        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
    }

    private void countingUnderstanding(List<RadioButton> pressedRB) {
        //Подсчет: Понимание
        String txt;
        for (int i = 0; i < plusUnderstanding.length; i++) {
            txt = (String) pressedRB.get(plusUnderstanding[i]).getText();
            if(txt.equals(ANSWERCHOISES[2])){
                sumUnderstanding += 2;
            }else if(txt.equals(ANSWERCHOISES[1])){
                sumUnderstanding++;
            }
        }

        for (int i = 0; i < minusUnderstanding.length; i++) {
            txt = (String) pressedRB.get(minusUnderstanding[i]).getText();
            if(txt.equals(ANSWERCHOISES[0])){
                sumUnderstanding += 2;
            }else if(txt.equals(ANSWERCHOISES[1])){
                sumUnderstanding++;
            }
        }

        sumUnderstanding = Math.round((sumUnderstanding / 30) * 100);

        textViews[0].setText("Ваш итоговый балл понимания: " + sumUnderstanding + " %");
        if(sumUnderstanding > 61){
            textViews[1].setText("Высокий показатель (61% - 100%)");
            textViews[2].setText(R.string.PEAUnderstandingHighRes);
        }else if(sumUnderstanding > 41){
            textViews[1].setText("Средний показатель (41% - 61%)");
            textViews[2].setText(R.string.PEAUnderstandingAverageRes);
        }else{
            textViews[1].setText("Низкий показатель (0% - 41%)");
            textViews[2].setText(R.string.PEAUnderstandingLowRes);
        }
    }

    private void countingEmotionalAt(List<RadioButton> pressedRB) {
        //Подсчет: Эмоциональное притяжение
        String txt;
        for (int i = 0; i < plusEmotionalAt.length; i++) {
            txt = (String) pressedRB.get(plusEmotionalAt[i]).getText();
            if(txt.equals(ANSWERCHOISES[2])){
                sumEmotionalAt += 2;
            }else if(txt.equals(ANSWERCHOISES[1])){
                sumEmotionalAt++;
            }
        }

        for (int i = 0; i < minusEmotionalAt.length; i++) {
            txt = (String) pressedRB.get(minusEmotionalAt[i]).getText();
            if(txt.equals(ANSWERCHOISES[0])){
                sumEmotionalAt += 2;
            }else if(txt.equals(ANSWERCHOISES[1])){
                sumEmotionalAt++;
            }
        }

        sumEmotionalAt = Math.round((sumEmotionalAt / 30) * 100);

        textViews[3].setText("Ваш итоговый балл эмоционального притяжения: " + sumEmotionalAt + " %");
        if(sumEmotionalAt > 61){
            textViews[4].setText("Высокий показатель (61% - 100%)");
            textViews[5].setText(R.string.PEAEmotionalAtHighRes);
        }else if(sumEmotionalAt > 41){
            textViews[4].setText("Средний показатель (41% - 61%)");
            textViews[5].setText(R.string.PEAEmotionalAtAverageRes);
        }else{
            textViews[4].setText("Низкий показатель (0% - 41%)");
            textViews[5].setText(R.string.PEAEmotionalAtLowRes);
        }
    }

    private void countingAuthority(List<RadioButton> pressedRB) {
        //Подсчет: Эмоциональное притяжение
        String txt;
        for (int i = 0; i < plusAuthority.length; i++) {
            txt = (String) pressedRB.get(plusAuthority[i]).getText();
            if(txt.equals(ANSWERCHOISES[2])){
                sumAuthority += 2;
            }else if(txt.equals(ANSWERCHOISES[1])){
                sumAuthority++;
            }
        }

        for (int i = 0; i < minusAuthority.length; i++) {
            txt = (String) pressedRB.get(minusAuthority[i]).getText();
            if(txt.equals(ANSWERCHOISES[0])){
                sumAuthority += 2;
            }else if(txt.equals(ANSWERCHOISES[1])){
                sumAuthority++;
            }
        }

        sumAuthority = Math.round((sumAuthority / 30) * 100);

        textViews[6].setText("Ваш итоговый балл эмоционального притяжения: " + sumAuthority + " %");
        if(sumAuthority > 61){
            textViews[7].setText("Высокий показатель (61% - 100%)");
            textViews[8].setText(R.string.PEAAuthorityHighRes);
        }else if(sumAuthority > 41){
            textViews[7].setText("Средний показатель (41% - 61%)");
            textViews[8].setText(R.string.PEAAuthorityAverageRes);
        }else{
            textViews[7].setText("Низкий показатель (0% - 41%)");
            textViews[8].setText(R.string.PEAAuthorityLowRes);
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