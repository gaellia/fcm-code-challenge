package com.example.fcmexample.sendActivity

import com.example.fcmsender.MessageType

data class SendActivityViewState(
    val type: MessageType = MessageType.DATA,
    val title: String = "",
    val body: String = "",
    val topic: String = ""
)