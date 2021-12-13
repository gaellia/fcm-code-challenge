package com.example.fcmexample.sendActivity

import com.example.fcmsender.MessageType

sealed class SendActivityAction {
    data class SendMessage(
        val type: MessageType,
        val title: String,
        val body: String,
        val topic: String,
        val token: String
    ): SendActivityAction()

    data class UpdateMessageType(val type: MessageType): SendActivityAction()
    data class UpdateTitle(val title: String): SendActivityAction()
    data class UpdateBody(val body: String): SendActivityAction()
    data class UpdateTopic(val topic: String): SendActivityAction()
}