package ru.hse.pe.test.InterpersonalCommunicationTests;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import ru.hse.pe.utils.FireBaseSuccessListener;

public class Test_LevelEmpathy extends AppCompatActivity {

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
    private boolean isMale;


    private int countOfQuestions = 60, counter = 1, counterQuestion = 0;
    private int[] progressBarChecked = new int[countOfQuestions];
    private int currentProgress = 0;
    private int sumScoringTest = 0;
    private TextView textView, test_title1, test_indicator1, test_text1;
    private int[] plusAgreePoints = new int[]{1, 6, 19, 22, 25, 26, 35, 36, 37, 38, 41, 42, 43,
            44, 52, 54, 55, 57, 58, 59, 60};
    private int[] plusDisagreePoints = new int[]{4, 8, 10, 11, 12, 14, 15, 18, 21,
            27, 28, 29, 32, 34, 39, 46, 48, 49, 50};

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_text1};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_text1};
    private final String[] ANSWERCHOISES = new String[]{"Совершенно не согласен", "Скорее не согласен",
            "Скорее согласен", "Совершенно согласен"};
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_level_empathy);

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

        Functions.minusIndex(plusAgreePoints);
        Functions.minusIndex(plusDisagreePoints);

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

        Functions.getSexFromDB(new FireBaseSuccessListener() {
            @Override
            public void onDataFound(boolean isDataFetched) {
                userIsOnFirebase(isDataFetched);
            }
        });
    }


    private boolean userIsOnFirebase(boolean isOnFirebase){
        isMale = isOnFirebase;
        Log.e("Test_Como", "isMale" + isMale);
        return isOnFirebase;
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

        // Считаем уровень сопереживания (мужчины/женщины)
        countingLevelEmpathy(pressedRB);

        test_answers.setVisibility(LinearLayout.VISIBLE);
        test_main.setVisibility(LinearLayout.GONE);
    }

    private void countingLevelEmpathy(List<RadioButton> pressedRB) {
        String txt;
        for (int i = 0; i < plusAgreePoints.length; i++) {
            txt = (String) pressedRB.get(plusAgreePoints[i]).getText();
            if(txt.equals(ANSWERCHOISES[3])){
                sumScoringTest += 2;
            }else if(txt.equals(ANSWERCHOISES[2]) ){
                sumScoringTest++;
            }
        }

        for (int i = 0; i < plusDisagreePoints.length; i++) {
            txt = (String) pressedRB.get(plusDisagreePoints[i]).getText();
            if(txt.equals(ANSWERCHOISES[0])){
                sumScoringTest += 2;
            }else if(txt.equals(ANSWERCHOISES[1])){
                sumScoringTest++;
            }
        }



        if(!isMale){
            textViews[0].setText("Ваш итоговый балл сопереживания (для женщин): " + sumScoringTest);
            if(sumScoringTest >= 66){
                textViews[1].setText("Больше 63 баллов");
                textViews[2].setText(R.string.LEVeryHighRes);
            }else if(sumScoringTest >= 55){
                textViews[1].setText("55 - 66 балла (ов)");
                textViews[2].setText(R.string.LEHighRes);
            }else if(sumScoringTest >= 32){
                textViews[1].setText("32 - 54 балла (ов)");
                textViews[2].setText(R.string.LEAverageRes);
            }else if(sumScoringTest >= 20){
                textViews[1].setText("20 - 31 балла (ов)");
                textViews[2].setText(R.string.LELowRes);
            }else{
                textViews[1].setText("Меньше 20 баллов");
                textViews[2].setText(R.string.LEVeryLowRes);
            }
        }else {
            textViews[0].setText("Ваш итоговый балл сопереживания (для мужчин): " + sumScoringTest);
            if (sumScoringTest >= 63) {
                textViews[1].setText("Больше 63 баллов");
                textViews[2].setText(R.string.LEVeryHighRes);
            } else if (sumScoringTest >= 52) {
                textViews[1].setText("52 - 63 балла (ов)");
                textViews[2].setText(R.string.LEHighRes);
            } else if (sumScoringTest >= 29) {
                textViews[1].setText("29 - 51 балла (ов)");
                textViews[2].setText(R.string.LEAverageRes);
            } else if (sumScoringTest >= 17) {
                textViews[1].setText("17 - 28 балла (ов)");
                textViews[2].setText(R.string.LELowRes);
            } else {
                textViews[1].setText("Меньше 17 (ов)");
                textViews[2].setText(R.string.LEVeryLowRes);
            }
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