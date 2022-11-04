package ru.hse.pe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ru.hse.pe.model.TestItem;
import ru.hse.pe.test.CharacterOfUserTests.Test_AlienationMoralResponsibility;
import ru.hse.pe.test.CharacterOfUserTests.Test_AutismSpectrumCoefficient;
import ru.hse.pe.test.CharacterOfUserTests.Test_BarretImpulsivityScale;
import ru.hse.pe.test.CharacterOfUserTests.Test_BigFive;
import ru.hse.pe.test.CharacterOfUserTests.Test_BussPerryAggressionQuestionnaire;
import ru.hse.pe.test.CharacterOfUserTests.Test_CognitiveFlexibilityQuestionnaire;
import ru.hse.pe.test.CharacterOfUserTests.Test_DiagnosisTemperamentEysenck;
import ru.hse.pe.test.CharacterOfUserTests.Test_HardinessSurvey;
import ru.hse.pe.test.CharacterOfUserTests.Test_ImplicitTheoriesLearning;
import ru.hse.pe.test.CharacterOfUserTests.Test_MultidimensionalPerfectionismScale;
import ru.hse.pe.test.CharacterOfUserTests.Test_SandraBoehmGenderRoleQuestionnaire;
import ru.hse.pe.test.CharacterOfUserTests.Test_ToleranceToUncertainly;
import ru.hse.pe.test.InterpersonalCommunicationTests.Test_AssessmentNeedApproval;
import ru.hse.pe.test.InterpersonalCommunicationTests.Test_BornsteinRelationshipProfile;
import ru.hse.pe.test.InterpersonalCommunicationTests.Test_Como;
import ru.hse.pe.test.InterpersonalCommunicationTests.Test_DiagnosticControlAssessment;
import ru.hse.pe.test.InterpersonalCommunicationTests.Test_InterpersonalDependencyInventory;
import ru.hse.pe.test.InterpersonalCommunicationTests.Test_LevelEmpathy;
import ru.hse.pe.test.InterpersonalCommunicationTests.Test_PEA;
import ru.hse.pe.test.InterpersonalCommunicationTests.Test_ScaleLoveSympathy;
import ru.hse.pe.test.PsychologicalState.Test_BecksAlarmScale;
import ru.hse.pe.test.PsychologicalState.Test_BecksDepressionScale;
import ru.hse.pe.test.PsychologicalState.Test_BecksHopelessnessScale;
import ru.hse.pe.test.PsychologicalState.Test_DepressionAnxietStress;
import ru.hse.pe.test.PsychologicalState.Test_MethodsDiagnosisNeurosis;
import ru.hse.pe.test.PsychologicalState.Test_OxfordHappinessQuestionnaire;

public class Test extends AppCompatActivity {
    private DatabaseReference mDataBase, commandsRef;
    private String TEST_KEY = "Tests";
    private TextView testTitle, testDesc, testTime, testCountQuestion, mainTitle;
    private HashMap<String, String> putExtras;
    private List<String> keys;
    private LinearLayout mainLL;
    private List<TestItem> tests;
    private LinearLayout ll;
    private String typeOfTest = "";
    private List<Button> buttons;

    private HashMap<String, HashMap<Integer, Runnable>> testsTypes;
    private HashMap<Integer, Runnable> charaterOfUserMap, InterpersonalCommunicationMap,
            PsychologicalStateMap, IntelligenceMap, ProfessionalOrientationMap;
    private final int countTests = 12;
    private int countVisibleTests = 0;

    private List<TextView> testTitles, testDescs, testTimes, testCountQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    private void init() {
        testTitles = new ArrayList<>();
        testDescs = new ArrayList<>();
        testTimes = new ArrayList<>();
        testCountQuestions = new ArrayList<>();
        putExtras = new HashMap<>();
        keys = new ArrayList<>();
        mainLL = findViewById(R.id.add_ll);
        mainTitle = findViewById(R.id.mainTitle);
        buttons = new ArrayList<>();

        Bundle arguments = getIntent().getExtras();
        mainTitle.setText(arguments.get("name").toString());
        countVisibleTests = (int) arguments.get("countVisibleTests");
        typeOfTest = arguments.getString("typeOfTest");

       // addTestItem();

        for (int i = 0; i < countTests; i++) {
            int idTitle = getResources().getIdentifier("test_title" + i, "id", getPackageName());
            int idDesc = getResources().getIdentifier("test_des" + i, "id", getPackageName());
            int idTime = getResources().getIdentifier("test_time" + i, "id", getPackageName());
            int idCountQ = getResources().getIdentifier("test_countQuestions" + i, "id", getPackageName());
            int idBtn = getResources().getIdentifier("test_button" + i, "id", getPackageName());
            testTitles.add((TextView) findViewById(idTitle));
            testDescs.add((TextView) findViewById(idDesc));
            testTimes.add((TextView) findViewById(idTime));
            testCountQuestions.add((TextView) findViewById(idCountQ));
            buttons.add((Button) findViewById(idBtn));
        }

        for (int i = countVisibleTests; i < countTests; i++) {
            ll = (LinearLayout)  findViewById(getResources().
                    getIdentifier("test_item" + i, "id", getPackageName()));
            ll.setVisibility(View.GONE);
        }


        mDataBase = FirebaseDatabase.getInstance("https://zeta-turbine-297107-default-rtdb.europe-west1.firebasedatabase.app").getReference(TEST_KEY);
        mainLL = findViewById(R.id.add_ll);
        commandsRef = mDataBase.child(typeOfTest);


        tests = new ArrayList<>();
        getTestsFromDB();

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = getId(view);
                    int intId = Integer.parseInt(id.substring(29));

                    dataOptimize(view);

                    switch (typeOfTest){
                        case "CharacterOfUser":
                            Objects.requireNonNull(Objects.requireNonNull(testsTypes.get("CharacterOfUser")).get(intId)).run();
                            break;
                        case "InterpersonalCommunication":
                            Objects.requireNonNull(Objects.requireNonNull(testsTypes.get("InterpersonalCommunication")).get(intId)).run();
                            break;
                        case "PsychologicalState":
                            Objects.requireNonNull(Objects.requireNonNull(testsTypes.get("PsychologicalState")).get(intId)).run();
                            break;
                        case "Intelligence":
                            Objects.requireNonNull(Objects.requireNonNull(testsTypes.get("Intelligence")).get(intId)).run();
                            break;
                        case "ProfessionalOrientation":
                            Objects.requireNonNull(Objects.requireNonNull(testsTypes.get("ProfessionalOrientation")).get(intId)).run();
                            break;
                    }
                }
            });
        }
    }

    public static String getId(View view) {
        if (view.getId() == View.NO_ID) return "no-id";
        else return view.getResources().getResourceName(view.getId());
    }


    private void dataOptimize(View view) {
        testsTypes = new HashMap<String, HashMap<Integer, Runnable>>();
        charaterOfUserMap = new HashMap<Integer, Runnable>(){{
            put(0, () -> solveAMR(view));
            put(1, () -> solveASC(view));
            put(2, () -> solveBIS(view));
            put(3, () -> solveBF(view));
            put(4, () -> solveBPAQ(view));
            put(5, () -> solveCFQ(view));
            put(6, () -> solveDTE(view));
            put(7, () -> solveHS(view));
            put(8, () -> solveITL(view));
            put(9, () -> solveMPS(view));
            put(10, () -> solveSBGRQ(view));
            put(11, () -> solveTTU(view));
        }};

        InterpersonalCommunicationMap = new HashMap<Integer, Runnable>(){{
            put(0, () -> solveAna());
            put(1, () -> solveBRP());
            put(2, () -> solveDCA());
            put(3, () -> solveIDI());
            put(4, () -> solveLE());
            put(5, () -> solvePEA());
            put(6, () -> solveSLS());
            put(7, () -> solveComo());
        }};

        PsychologicalStateMap = new HashMap<Integer, Runnable>(){{
            put(0, () -> solveBAS());
            put(1, () -> solveBDS());
            put(2, () -> solveBHS());
            put(3, () -> solveDAS());
            put(4, () -> solveMDN());
            put(5, () -> solveOHQ());
        }};

        testsTypes.put("CharacterOfUser", charaterOfUserMap);
        testsTypes.put("InterpersonalCommunication", InterpersonalCommunicationMap);
        testsTypes.put("PsychologicalState", PsychologicalStateMap);
    }

    private void newActivity(int numKey, Class<?> clazz){
        Intent intent = new Intent(this, clazz);

        String key = keys.get(numKey);
        intent.putExtra("key", key);
        intent.putExtra("name", putExtras.get(key));
        intent.putExtra("typeOfTest", typeOfTest);
        startActivity(intent);
    }


    // PsychologicalStateMap
    private void solveBAS() {
        newActivity(0, Test_BecksAlarmScale.class);
    }

    private void solveBDS() {
        newActivity(1, Test_BecksDepressionScale.class);
    }

    private void solveBHS() {
        newActivity(2, Test_BecksHopelessnessScale.class);
    }

    private void solveDAS() {
        newActivity(3, Test_DepressionAnxietStress.class);
    }

    private void solveMDN() {
        newActivity(4, Test_MethodsDiagnosisNeurosis.class);
    }

    private void solveOHQ() {
        newActivity(5, Test_OxfordHappinessQuestionnaire.class);
    }


    // InterpersonalCommunicationMap
    private void solveAna(){
        newActivity(0, Test_AssessmentNeedApproval.class);
    }
    private void solveBRP(){
        newActivity(1, Test_BornsteinRelationshipProfile.class);
    }
    private void solveDCA() {
        newActivity(2, Test_DiagnosticControlAssessment.class);
    }
    private void solveIDI(){
        newActivity(3, Test_InterpersonalDependencyInventory.class);
    }
    private void solveLE() {
        newActivity(4, Test_LevelEmpathy.class);
    }
    private void solvePEA() {
        newActivity(5, Test_PEA.class);
    }
    private void solveSLS() {
        newActivity(6, Test_ScaleLoveSympathy.class);
    }
    private void solveComo(){
        newActivity(7, Test_Como.class);
    }


    // CharacterOfUser
    public void solveAMR(View view){
        newActivity(0, Test_AlienationMoralResponsibility.class);
    }

    public void solveASC(View view){
        newActivity(1, Test_AutismSpectrumCoefficient.class);
    }
    public void solveBIS(View view){
        newActivity(2, Test_BarretImpulsivityScale.class);
    }
    public void solveBF(View view){
        newActivity(3, Test_BigFive.class);
    }
    public void solveBPAQ(View view){
        newActivity(4, Test_BussPerryAggressionQuestionnaire.class);
    }

    public void solveCFQ(View view){
        newActivity(5, Test_CognitiveFlexibilityQuestionnaire.class);
    }

    public void solveDTE(View view){
        newActivity(6, Test_DiagnosisTemperamentEysenck.class);
    }

    public void solveHS(View view){
        newActivity(7, Test_HardinessSurvey.class);
    }

    public void solveITL(View view) {
        newActivity(8, Test_ImplicitTheoriesLearning.class);
    }
    public void solveMPS(View view){
        newActivity(9, Test_MultidimensionalPerfectionismScale.class);
    }
    public void solveSBGRQ(View view){
        newActivity(10, Test_SandraBoehmGenderRoleQuestionnaire.class);
    }
    public void solveTTU(View view){
        newActivity(11, Test_ToleranceToUncertainly.class);
    }


    private void getTestsFromDB(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String desc = Objects.requireNonNull(ds.child("desc").getValue(String.class));
                    if(desc.length() > 100){
                        desc = desc.substring(0, 100) + "...";
                    }

                    String name = ds.child("name").getValue(String.class);

                    String value = ds.getKey();
                    putExtras.put(value, name);
                    keys.add(value);

                    int countQuestions = ds.child("countQuestions").getValue(Integer.class);
                    int time = ds.child("time").getValue(Integer.class);
                    StringBuilder timeStr = new StringBuilder();
                    if(time == 1){
                        timeStr.append(time).append(" минута");
                    }else if(time <= 4){
                        timeStr.append(time).append(" минуты");
                    }else {
                        timeStr.append(time).append(" минут");
                    }

                    TestItem testItem = new TestItem(name, desc, countQuestions, timeStr.toString());
                    tests.add(testItem);
                }

                Collections.sort(keys);
                
                for(int i = 0; i < tests.size(); ++i){
                    testTitles.get(i).setText(tests.get(i).getName());
                    testCountQuestions.get(i).setText(String.valueOf(tests.get(i).getCountQuestions()));
                    testTimes.get(i).setText(String.valueOf(tests.get(i).getTime()));
                    testDescs.get(i).setText(tests.get(i).getDesc());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }

        };
        commandsRef.addValueEventListener(valueEventListener);
    };


    public void openMenu(View view){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}



//    private void addTestItem() {
//        LinearLayout testItem_wrapper = new LinearLayout(this, null, R.style.testItem_wrapper);
//        TextView testTitle = new TextView(this, null, R.style.testItem_title);
//
//        testItem_wrapper.addView(testTitle);
//
//        LinearLayout testItem_wrapperHint = new LinearLayout(this, null, R.style.testItem_wrapperHint);
//
//
//        ImageView question_answer_grey = new ImageView(this, null, R.style.testItem_img);
//        question_answer_grey.setImageResource(R.drawable.question_answer_grey);
//        TextView test_countQuestions1 = new TextView(this, null, R.style.testItem_txtHint);
//
//        ImageView timer_grey = new ImageView(this, null, R.style.testItem_img);
//        question_answer_grey.setImageResource(R.drawable.timer_grey);
//        TextView test_time = new TextView(this, null, R.style.testItem_txtHint);
//
//        testItem_wrapperHint.addView(question_answer_grey);
//        testItem_wrapperHint.addView(test_countQuestions1);
//        testItem_wrapperHint.addView(timer_grey);
//        testItem_wrapperHint.addView(test_time);
//        testItem_wrapper.addView(testItem_wrapperHint);
//
//
//        TextView test_des = new TextView(this, null, R.style.testItem_des);
//        Button test_button = new Button(this, null, R.style.testItem_btn);
//
//
//        testItem_wrapper.addView(test_des);
//        testItem_wrapper.addView(test_button);
//
//        mainLL.addView(testItem_wrapper);
//    }

