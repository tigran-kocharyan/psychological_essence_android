import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import ru.hse.pe.R
import ru.hse.pe.domain.model.QuizAnswerEntity
import ru.hse.pe.domain.model.QuizResultEntity
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.utils.Utils


@Composable
fun Results(viewModel: ContentViewModel, viewLifecycleOwner: LifecycleOwner) {
    Column {
        Utils.SystemBarsNotVisible()
        Utils.MyTopAppBar("Тест", false)
        Test.userAnswers.removeAt(0)

        viewModel.getQuizResult(QuizAnswerEntity(answers = Test.userAnswers))
        val res by viewModel.getQuizResultLiveData().observeAsState()
        Log.d("quizItems", res.toString())



        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(0.87f)
                .background(Color.White),
        ) {
            LazyColumn {
                items(2) {
                    Column {
                        Text(
                            text = "Ваши результаты: ",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 32.dp, bottom = 13.dp),
                            style = MaterialTheme.typography.h3,
                        )

                        Text(
                            text = "130/130",
                            modifier = Modifier.padding(bottom = 24.dp),
                            style = MaterialTheme.typography.h4,
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                TODO()
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .padding(start = 25.dp, end = 25.dp)
                .fillMaxWidth()
                .height(45.dp),
            colors = ButtonDefaults
                .buttonColors(
                    backgroundColor = colorResource(id = R.color.purple),
                    contentColor = Color.White
                )
        ) {
            Text(text = stringResource(id = R.string.backToTests))
        }
    }
}
