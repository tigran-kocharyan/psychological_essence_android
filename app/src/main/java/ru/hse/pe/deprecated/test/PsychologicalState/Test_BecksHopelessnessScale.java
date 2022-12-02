package ru.hse.pe.deprecated.test.PsychologicalState;

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

import ru.hse.pe.R;
import ru.hse.pe.deprecated.Functions;
import ru.hse.pe.deprecated.Menu;
import ru.hse.pe.deprecated.Test;

public class Test_BecksHopelessnessScale extends AppCompatActivity {


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

    private int countOfQuestions = 20, counter = 1, counterQuestion = 0;
    private int[] progressBarChecked = new int[countOfQuestions];
    private int currentProgress = 0;
    private int sumScoringTest = 0;
    private TextView textView, test_title1, test_indicator1, test_text1;

    private int sumCommonPoints = 0;
    private int[] plusCommonPoints = new int[]{2, 4, 7, 9, 11, 12, 14, 16, 17, 18, 20};
    private int[] minusCommonPoints = new int[]{1, 3, 5, 6, 8, 10, 13, 15, 19};


    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1};

    private final String[] ANSWERCHOISES = {"Совсем неверно", "Скорее неверно",
            "Скорее верно", "Очень верно"};
    private int transcodingvar = 4;
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_becks_hopelessness_scale);

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

        Functions.minusIndex(minusCommonPoints);
        Functions.minusIndex(plusCommonPoints);

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
            pressedRB.add(radioButton);
        }

        //Подсчет: Безнадежность (общий)
        countingLove(pressedRB);

        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
    }

    private void countingLove(List<RadioButton> pressedRB) {
        String txt;
        for (int i = 0; i < plusCommonPoints.length; i++) {
            txt = (String) pressedRB.get(plusCommonPoints[i]).getText();
            if(txt.equals(ANSWERCHOISES[2]) || txt.equals(ANSWERCHOISES[3])){
                sumCommonPoints++;
            }
        }

        for (int i = 0; i < minusCommonPoints.length; i++) {
            txt = (String) pressedRB.get(minusCommonPoints[i]).getText();
            if(txt.equals(ANSWERCHOISES[0]) || txt.equals(ANSWERCHOISES[1])){
                sumCommonPoints++;
            }
        }


        textViews[0].setText("Ваш балл безнадежности: " + sumCommonPoints);
        if(sumCommonPoints >= 15){
            textViews[1].setText("Безнадежность тяжёлая: (15 - 20) балла (ов)");
            textViews[2].setText(R.string.BHSHighRes);
        }else if(sumCommonPoints >= 9){
            textViews[1].setText("Безнадежность умеренная: (9 - 14) балла (ов)");
            textViews[2].setText(R.string.BHSAverageRes);
        }else if(sumCommonPoints >= 4){
            textViews[1].setText("Безнадежность легкая: (4 - 8) балла (ов)");
            textViews[2].setText(R.string.BHSLowRes);
        }else{
            textViews[1].setText("Безнадежность не выявлена: (1 - 3) балла (ов)");
            textViews[2].setText(R.string.BHSVeryLowRes);
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