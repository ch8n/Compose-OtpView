package io.github.ch8n.otpviewsample

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.ch8n.otpviewsample.ui.theme.OtpViewSampleTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@Composable
fun OtpBugView() {
    val (editValue, setEditValue) = remember { mutableStateOf("") }
    val otpLength = remember { 4 }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    TextField(
        value = editValue,
        onValueChange = {
            if (it.length <= otpLength) {
                setEditValue(it)
            }
        },
        modifier = Modifier
            .size(0.dp)
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        (0 until otpLength).map { index ->
            OtpCell(
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        focusRequester.requestFocus()
                        keyboard?.show()
                    }
                    .border(1.dp, Color.DarkGray),
                value = editValue.getOrNull(index)?.toString() ?: "",
                isCursorVisible = editValue.length == index
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }

}

@Composable
fun OtpCell(
    modifier: Modifier,
    value: String,
    isCursorVisible: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    LaunchedEffect(key1 = cursorSymbol,isCursorVisible) {
        if (isCursorVisible){
            scope.launch {
                delay(350)
                setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else "")
            }
        }
    }

    Box(
        modifier = modifier
    ) {

        Text(
            text = if (isCursorVisible) cursorSymbol else value,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun OtpViewPreview() {
    OtpViewSampleTheme {
        Surface(modifier = Modifier.padding(24.dp)) {
            OtpBugView()
        }
    }
}