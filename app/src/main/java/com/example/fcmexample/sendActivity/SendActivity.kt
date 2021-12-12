package com.example.fcmexample.sendActivity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class SendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SendActivityComposable()
            }
        }
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

@Composable
private fun SendActivityContent(
    viewState: SendActivityViewState = SendActivityViewState(),
    actioner: (SendActivityAction) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Welcome to Send Activity")
    }
}

@Preview (showBackground = true)
@Composable
private fun SendActivityPreview() {
    SendActivityContent(viewState = SendActivityViewState())

}