package com.example.fcmexample.sendActivity

import com.example.fcmexample.core.ActionViewModel

class SendActivityViewModel :
    ActionViewModel<SendActivityViewState, SendActivityAction>(SendActivityViewState()) {
    override fun collectAction(action: SendActivityAction) {
        when (action) {

        }
    }
}