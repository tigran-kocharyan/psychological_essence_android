package ru.hse.pe.presentation.test.CharacterOfUserTests;

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

public class Test_BigFive extends AppCompatActivity {
    private List<String> questions;
    private HashMap<Integer, Integer> transcoding;


    private List<Integer> points;

    private int[] plusExtraversion = new int[]{1, 6, 21, 41, 46, 56};
    private int[] plusExtraversionR = new int[]{11, 16, 26, 31, 36, 51};
    private int sumExtraversion = 0;

    private int[] plusBenevolence = new int[]{2, 7, 27, 32, 52, 57};
    private int[] plusBenevolenceR = new int[]{12, 17, 22, 37, 42, 47};
    private int sumBenevolence = 0;

    private int[] plusFairness = new int[]{13, 18, 33, 38, 43, 53};
    private int[] plusFairnessR = new int[]{3, 8, 23, 28, 48, 58};
    private int sumFairness = 0;

    private int[] plusNeuroticism = new int[]{14, 19, 34, 39, 54, 59};
    private int[] plusNeuroticismR = new int[]{4, 9, 24, 29, 44, 49};
    private int sumNeuroticism = 0;

    private int[] plusExperience = new int[]{10, 15, 20, 35, 40, 60};
    private int[] plusExperienceR = new int[]{5, 25, 30, 45, 50, 55};
    private int sumExperience = 0;

    private short sumSociability, sumPersistence, sumEnergy, sumSympathy, sumRespectability, sumConfidence,
    sumOrganization, sumProductivity, sumResponsibility, sumAnxiety, sumDepression, sumEmotional,
    sumCuriosity, sumAesthetics, sumImagination;

    private int countOfQuestions = 60, counter = 1, counterQuestion = 0,  transcodingvar = 5;
    private int currentProgress = 0;
    private int[] progressBarChecked = new int[countOfQuestions];

    private ProgressBar progressBar;
    private RadioButton radioButton;
    private TextView nameTest;
    private Button btnSkip, btnFinish;
    private List<RadioGroup> radioGroups;
    private TextView test_title1, test_indicator1, test_points1, test_points2, test_points3,
            test_text1, test_title2, test_indicator2, test_points4,
            test_points5, test_points6, test_text2, test_title3, test_indicator3, test_points7,
            test_points8, test_points9, test_text3, test_title4, test_indicator4, test_points10,
            test_points11, test_points12, test_text4, test_title5, test_indicator5, test_points13,
            test_points14, test_points15, test_text5;
    private TextView countOfProgress, question;
    private LinearLayout root, test_main, test_answers;
    private DatabaseReference mDataBase, commandsRef;
    private String TEST_KEY;

    private final int[] ids = new int[]{R.id.test_title1, R.id.test_indicator1, R.id.test_points1,
            R.id.test_points2, R.id.test_points3, R.id.test_text1, R.id.test_title2,
            R.id.test_indicator2, R.id.test_points4, R.id.test_points5, R.id.test_points6,
            R.id.test_text2, R.id.test_title3, R.id.test_indicator3, R.id.test_points7,
            R.id.test_points8, R.id.test_points9, R.id.test_text3, R.id.test_title4,
            R.id.test_indicator4, R.id.test_points10, R.id.test_points11, R.id.test_points12,
            R.id.test_text4,  R.id.test_title5,  R.id.test_indicator5,  R.id.test_points13,
            R.id.test_points14,  R.id.test_points15,  R.id.test_text5};
    private final TextView[] textViews = new TextView[]{test_title1, test_indicator1, test_points1,
            test_points2, test_points3, test_text1, test_title2, test_indicator2, test_points4,
            test_points5, test_points6, test_text2, test_title3, test_indicator3, test_points7,
            test_points8, test_points9, test_text3, test_title4, test_indicator4, test_points10,
            test_points11, test_points12, test_text4, test_title5, test_indicator5, test_points13,
            test_points14, test_points15, test_text5};

    private final Map<Integer, String> ANSWERCHOISES = new HashMap<Integer, String>() {{
        put(1, "Cовершенно не согласен");
        put(2, "Немного не согласен");
        put(3, "Нейтрально; нет мнения");
        put(4, "Немного согласен");
        put(5, "Cовершенно согласен");
    }};
    private String typeOfTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_big_five);

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

        for (int i = 0; i < plusExtraversion.length; i++) {
            plusExtraversion[i] -= 1;
            plusExtraversionR[i] -= 1;
            plusBenevolence[i] -= 1;
            plusBenevolenceR[i] -= 1;
            plusFairness[i] -= 1;
            plusFairnessR[i] -= 1;
            plusNeuroticism[i] -= 1;
            plusNeuroticismR[i] -= 1;
            plusExperience[i] -= 1;
            plusExperienceR[i] -= 1;
        }

        btnSkip = findViewById(R.id.btnSkip);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressTTU);
        progressBar.setMax(countOfQuestions);
        countOfProgress = findViewById(R.id.countOfProgress);
        question = findViewById(R.id.question);
        questions = new ArrayList<>();
        points = new ArrayList<>();
        transcoding = new HashMap<>();
        for (int i = 1, j = transcodingvar; i < transcodingvar + 1; i++, j--) {
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


        // Считаем Экстраверсия (Общительность, настойчивость, Энергичность)
        countingExtraversion();
        // Считаем Доброжелательность (Сочувствие, Уважительность, Доверие)
        countingBenevolence();
        // Считаем Доброжелательность (Организованность, Продуктивность, Ответственность )
        countingFairness();
        // Считаем Нейротизм (Тревожность, Депрессивность, Эмоциональная изменчивость )
        countingNeuroticism();
        // Считаем Открытость опыту (Любознательность, Эстетичность, Творческое воображение )
        countingExperience();

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

    private void countingExtraversion(){
        for (int i = 0; i < plusExtraversion.length; i++) {
            sumExtraversion += points.get(plusExtraversion[i]);
            sumExtraversion += transcoding.get(points.get(plusExtraversionR[i]));
        }

        sumSociability += points.get(0);
        sumSociability += points.get(45);
        sumSociability += transcoding.get(points.get(15));
        sumSociability += transcoding.get(points.get(30));


        sumPersistence += points.get(5);
        sumPersistence += points.get(20);
        sumPersistence += transcoding.get(points.get(35));
        sumPersistence += transcoding.get(points.get(50));

        sumEnergy += points.get(40);
        sumEnergy += points.get(55);
        sumEnergy += transcoding.get(points.get(10));
        sumEnergy += transcoding.get(points.get(25));

        textViews[0].setText("1. Экстраверсия");
        textViews[1].setText("Ваш общий балл экстраверсии: " +  sumExtraversion + " (из 60 возможных)");

        textViews[2].setText("• \tОбщительность: " +  sumSociability + " (из 20 возможных)");
        textViews[3].setText("• \tНастойчивость: " +  sumPersistence + " (из 20 возможных)");
        textViews[4].setText("• \tЭнергичность: " +  sumEnergy + " (из 20 возможных)");

        if(sumExtraversion >= 41) {
            textViews[5].setText(R.string.BFExtraversionHighRes);
        }else if (sumExtraversion >= 21){
            textViews[5].setText(R.string.BFExtraversionAverageRes);
        }else{
            textViews[5].setText(R.string.BFExtraversionLowRes);
        }
    }

    private void countingBenevolence() {
        for (int i = 0; i < plusBenevolence.length; i++) {
            sumBenevolence += points.get(plusBenevolence[i]);
            sumBenevolence += transcoding.get(points.get(plusBenevolenceR[i]));
        }

        sumSympathy += points.get(1);
        sumSympathy += points.get(31);
        sumSympathy += transcoding.get(points.get(16));
        sumSympathy += transcoding.get(points.get(46));


        sumRespectability += points.get(6);
        sumRespectability += points.get(51);
        sumRespectability += transcoding.get(points.get(21));
        sumRespectability += transcoding.get(points.get(36));

        sumConfidence += points.get(26);
        sumConfidence += points.get(56);
        sumConfidence += transcoding.get(points.get(11));
        sumConfidence += transcoding.get(points.get(41));

        textViews[6].setText("2. Доброжелательность");
        textViews[7].setText("Ваш общий балл доброжелательности: " +  sumBenevolence + " (из 60 возможных)");

        textViews[8].setText("• \tСочувствие: " +  sumSympathy + " (из 20 возможных)");
        textViews[9].setText("• \tУважительность: " +  sumRespectability + " (из 20 возможных)");
        textViews[10].setText("• \tДоверие: " +  sumConfidence + " (из 20 возможных)");

        if(sumBenevolence >= 41) {
            textViews[11].setText(R.string.BFBenevolenceHighRes);
        }else if (sumBenevolence >= 21){
            textViews[11].setText(R.string.BFBenevolenceAverageRes);
        }else{
            textViews[11].setText(R.string.BFBenevolenceLowRes);
        }
    }


    private void countingFairness() {
        for (int i = 0; i < plusFairness.length; i++) {
            sumFairness += points.get(plusFairness[i]);
            sumFairness += transcoding.get(points.get(plusFairnessR[i]));
        }

        sumOrganization += points.get(17);
        sumOrganization += points.get(32);
        sumOrganization += transcoding.get(points.get(2));
        sumOrganization += transcoding.get(points.get(47));

        sumProductivity += points.get(37);
        sumProductivity += points.get(52);
        sumProductivity += transcoding.get(points.get(7));
        sumProductivity += transcoding.get(points.get(22));

        sumResponsibility += points.get(12);
        sumResponsibility += points.get(42);
        sumResponsibility += transcoding.get(points.get(27));
        sumResponsibility += transcoding.get(points.get(57));

        textViews[12].setText("3. Добросовестность");
        textViews[13].setText("Ваш общий балл добросовестности: " +  sumFairness + " (из 60 возможных)");

        textViews[14].setText("• \tОрганизованность: " +  sumOrganization + " (из 20 возможных)");
        textViews[15].setText("• \tПродуктивность: " +  sumProductivity + " (из 20 возможных)");
        textViews[16].setText("• \tОтветственность: " +  sumResponsibility + " (из 20 возможных)");

        if(sumFairness >= 41) {
            textViews[17].setText(R.string.BFFairnesHighRes);
        }else if (sumExtraversion >= 21){
            textViews[17].setText(R.string.BFFairnesAverageRes);
        }else{
            textViews[17].setText(R.string.BFFairnesLowRes);
        }
    }

    private void countingNeuroticism() {
        for (int i = 0; i < plusNeuroticism.length; i++) {
            sumNeuroticism += points.get(plusNeuroticism[i]);
            sumNeuroticism += transcoding.get(points.get(plusNeuroticismR[i]));
        }

        sumAnxiety += points.get(18);
        sumAnxiety += points.get(33);
        sumAnxiety += transcoding.get(points.get(3));
        sumAnxiety += transcoding.get(points.get(48));

        sumDepression += points.get(38);
        sumDepression += points.get(53);
        sumDepression += transcoding.get(points.get(8));
        sumDepression += transcoding.get(points.get(23));

        sumEmotional += points.get(13);
        sumEmotional += points.get(58);
        sumEmotional += transcoding.get(points.get(28));
        sumEmotional += transcoding.get(points.get(43));

        textViews[18].setText("4. Нейротизм");
        textViews[19].setText("Ваш общий балл нейротизма: " +  sumNeuroticism + " (из 60 возможных)");

        textViews[20].setText("• \tТревожность : " +  sumAnxiety + " (из 20 возможных)");
        textViews[21].setText("• \tДепрессивность: " +  sumDepression + " (из 20 возможных)");
        textViews[22].setText("• \tЭмоциональная изменчивость: " +  sumEmotional + " (из 20 возможных)");

        if(sumNeuroticism >= 41) {
            textViews[23].setText(R.string.BFNeuroticismHighRes);
        }else if (sumNeuroticism >= 21){
            textViews[23].setText(R.string.BFNeuroticismAverageRes);
        }else{
            textViews[23].setText(R.string.BFNeuroticismLowRes);
        }
    }

    private void countingExperience() {
        for (int i = 0; i < plusExperience.length; i++) {
            sumExperience += points.get(plusExperience[i]);
            sumExperience += transcoding.get(points.get(plusExperienceR[i]));
        }

        sumCuriosity += points.get(9);
        sumCuriosity += points.get(39);
        sumCuriosity += transcoding.get(points.get(24));
        sumCuriosity += transcoding.get(points.get(54));

        sumAesthetics += points.get(19);
        sumAesthetics += points.get(34);
        sumAesthetics += transcoding.get(points.get(4));
        sumAesthetics += transcoding.get(points.get(49));

        sumImagination += points.get(14);
        sumImagination += points.get(59);
        sumImagination += transcoding.get(points.get(29));
        sumImagination += transcoding.get(points.get(44));

        textViews[24].setText("5. Открытость опыту:");
        textViews[25].setText("Ваш общий балл открытости опыту: " +  sumExperience + " (из 60 возможных)");

        textViews[26].setText("• \tЛюбознательность : " +  sumCuriosity + " (из 20 возможных)");
        textViews[27].setText("• \tЭстетичность: " +  sumAesthetics + " (из 20 возможных)");
        textViews[28].setText("• \tТворческое воображение: " +  sumImagination + " (из 20 возможных)");

        if(sumExperience >= 41) {
            textViews[29].setText(R.string.BFExperienceHighRes);
        }else if (sumExperience >= 21){
            textViews[29].setText(R.string.BFExperienceAverageRes);
        }else{
            textViews[29].setText(R.string.BFExperienceLowRes);
        }
    }
}