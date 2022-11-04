package ru.hse.pe;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.hse.pe.utils.FireBaseSuccessListener;


public class Functions {
    private static DatabaseReference mDataBase, commandsRef;
    private static String server
            = "https://zeta-turbine-297107-default-rtdb.europe-west1.firebasedatabase.app/";


    public static int[] minusIndex(int[] ints) {
        for (int i = 0; i < ints.length; i++) {
            ints[i] -= 1;
        }
        return ints;
    }

    public static void getSexFromDB(FireBaseSuccessListener dataFetched){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mDataBase = FirebaseDatabase.getInstance("https://zeta-turbine-297107-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/User/" + currentFirebaseUser.getUid());
        commandsRef = mDataBase.child("sex");


        ValueEventListener val = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sex = "";
                sex = snapshot.getValue(String.class);

                if(sex.equals("Мужской")){
                    dataFetched.onDataFound(true);
                }else{
                    dataFetched.onDataFound(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Executive Order", "The read failed: " + error.getDetails());
            }
        };
        commandsRef.addValueEventListener(val);
    }


}
