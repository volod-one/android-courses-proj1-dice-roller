package one.volod.dice_roller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import one.volod.dice_roller.ui.theme.DicerollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DicerollerTheme {
                DiceRollerApp()
            }
        }
    }
}


class DiceViewModel : ViewModel() {
    private val _result = MutableLiveData(1)
    val result: LiveData<Int> = _result

    fun onRollClick() {
        _result.value = (1..6).random()
    }

}

@Preview(showBackground = true)
@Composable
fun DiceRollerApp(diceViewModel: DiceViewModel = viewModel()) {

    // Hoisting result to composable
    // best approach to remove all business logic from ui
    // easy to test
    val result by diceViewModel.result.observeAsState(1)


    DiceWithButtonAndImage(
        modifier = Modifier
            // 1/2 Alignments // this one centering content in center of the screen
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        result = result,
        onClick = { diceViewModel.onRollClick() }
    )
}

@Composable
fun DiceWithButtonAndImage(
    result: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    /*// remember prevents of loosing variables value on ui update
    // in other words, it keeps value of variable in a Composition memory
    // and then take this value from the memory if ui was updated
    // cons: value will be lost if bundle or config change (rotate device)
    //       composable have state
    //       difficult to test
    var result by remember {
        // mutableState updating ui if value was changed // RE-RENDER or RE-COMPOSITION
        mutableStateOf(1)
    }*/

    /* // this state is fine with configuration changes
     // but still has problems with stateless
     // also is difficult to test
     var result by rememberSaveable { mutableStateOf(1) }*/

    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
        // 2/2 Alignments // this one centering horizontally in center
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = "$result"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onClick) {
            Text(text = stringResource(R.string.roll))
        }
    }
}
