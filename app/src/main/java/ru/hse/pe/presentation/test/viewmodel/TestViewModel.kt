package com.example.test.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.hse.pe.domain.model.Test
import ru.hse.pe.presentation.test.utils.sealed.DataState

class TestViewModel : ViewModel() {
    private var SERVER =
        "https://zeta-turbine-297107-default-rtdb.europe-west1.firebasedatabase.app/"
    val responce: MutableState<DataState> = mutableStateOf(DataState.Empty)


    init {
        fetchDataFromFireBase()
    }

    private fun fetchDataFromFireBase() {
        responce.value = DataState.Loading
        FirebaseDatabase.getInstance(SERVER)
            .getReference("Tests/CharacterOfUser/MultidimensionalPerfectionismScale")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val testItem = snapshot.getValue(Test::class.java)
                    if (testItem != null) {
                        responce.value = DataState.Success(testItem)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    responce.value = DataState.Failure(error.message)
                }
            })
    }
}

