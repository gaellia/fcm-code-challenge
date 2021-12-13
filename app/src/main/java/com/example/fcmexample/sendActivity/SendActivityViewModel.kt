package com.example.fcmexample.sendActivity

import androidx.lifecycle.viewModelScope
import com.example.fcmexample.core.ActionViewModel
import com.example.fcmsender.FCMSender
import com.example.fcmsender.MessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SendActivityViewModel :
    ActionViewModel<SendActivityViewState, SendActivityAction>(SendActivityViewState()) {
    override fun collectAction(action: SendActivityAction) {
        when (action) {
            is SendActivityAction.SendMessage -> sendMessage(
                type = action.type,
                title = action.title,
                body = action.body,
                topic = action.topic,
                token = action.token
            )
            is SendActivityAction.UpdateBody -> updateBody(action.body)
            is SendActivityAction.UpdateMessageType -> updateType(action.type)
            is SendActivityAction.UpdateTitle -> updateTitle(action.title)
            is SendActivityAction.UpdateTopic -> updateTopic(action.topic)
        }
    }

    private fun updateType(t: MessageType){
        viewModelScope.launchSetState { copy(type = t) }
    }
    private fun updateTitle(t: String){
        viewModelScope.launchSetState { copy(title = t) }
    }
    private fun updateTopic(t: String){
        viewModelScope.launchSetState { copy(topic = t) }
    }
    private fun updateBody(b: String){
        viewModelScope.launchSetState { copy(body = b) }
    }

    private fun sendMessage(
        type: MessageType,
        title: String,
        body: String,
        topic: String,
        token: String
    ) {

        CoroutineScope(Dispatchers.IO).launch {
            val response = FCMSender.FCMMessageBuilder()
                .setMessageType(type)
                .setTitle(title)
                .setBody(body)
                .setTopic(topic)
                .build()
                .sendTo(token)
            //Timber.i(response.toString())
        }
    }
}