package com.example.fcmexample.sendActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fcmexample.MainActivity
import com.example.fcmexample.utils.PREFS_NAME
import com.example.fcmexample.utils.TOKEN
import com.example.fcmsender.MessageType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SendActivityComposable()
            }
        }
        supportActionBar?.title = "Send a Message"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Handle back button
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

@Composable
fun SendActivityComposable() {
    SendActivityState()
}

@Composable
private fun SendActivityState(
    viewModel: SendActivityViewModel = viewModel()
) {
    val viewState by viewModel.state.collectAsState()
    SendActivityContent(
        viewState = viewState,
        actioner = { action ->
            when (action) {
                else -> viewModel.submitAction(action)
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun SendActivityContent(
    viewState: SendActivityViewState = SendActivityViewState(),
    actioner: (SendActivityAction) -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val token = prefs.getString(TOKEN, "")

    Box() {
        Column(modifier = Modifier
            .fillMaxSize()
        ) {

            val bringIntoViewRequester = remember { BringIntoViewRequester() }
            val coroutineScope = rememberCoroutineScope()
            val keyboardController = LocalSoftwareKeyboardController.current

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(modifier = Modifier,
                    text = "Type:  ${viewState.type}",
                    style = MaterialTheme.typography.h6
                )
                Switch(
                    checked = viewState.type == MessageType.DATA,
                    onCheckedChange = {
                        if (viewState.type == MessageType.DATA) {
                            actioner(SendActivityAction.UpdateMessageType(MessageType.NOTIFICATION))
                        } else {
                            actioner(SendActivityAction.UpdateMessageType(MessageType.DATA))
                        }
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFF6200EE),
                        uncheckedTrackColor = Color(0xFFA277E0)
                    )
                )

            }
            Text(modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 5.dp),
                text = "Title",
                style = MaterialTheme.typography.h6
            )
            TextField(modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
                value = viewState.title,
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()}
                ),
                onValueChange = { newTitle -> actioner(SendActivityAction.UpdateTitle(newTitle)) }
            )

            Text(modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 5.dp),
                text = "Topic",
                style = MaterialTheme.typography.h6
            )
            TextField(modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
                value = viewState.topic,
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()}
                ),
                onValueChange = { newTopic -> actioner(SendActivityAction.UpdateTopic(newTopic)) }
            )

            Text(modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 5.dp),
                text = "Body",
                style = MaterialTheme.typography.h6
            )
            TextField(modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .heightIn(min = 150.dp, Dp.Infinity)
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusEvent {
                    if (it.isFocused) coroutineScope.launch {
                        delay(100)
                        bringIntoViewRequester.bringIntoView()
                    }
                },
                value = viewState.body,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()}
                ),
                maxLines = 10,
                onValueChange = {
                        newBody -> actioner(SendActivityAction.UpdateBody(newBody))
                }
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
            onClick = {
                actioner(SendActivityAction.SendMessage(
                    type = viewState.type,
                    title = viewState.title,
                    body = viewState.body,
                    topic = viewState.topic,
                    token = token?:""
                ))

                Toast.makeText(context, "Message Sent!", Toast.LENGTH_LONG).show()

                // Go back to main activity
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
            },
            contentColor = Color.White,
            backgroundColor = Color(0xFF6200EE)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
        }
    }
}

@Preview (showBackground = true)
@Composable
private fun SendActivityPreview() {
    SendActivityContent(viewState = SendActivityViewState())

}