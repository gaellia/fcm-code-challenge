package com.example.fcmexample.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fcmexample.NotificationListAdapter
import com.example.fcmexample.db.Notification

@BindingAdapter("notificationList")
fun RecyclerView.notificationList(cards: List<Notification>?) {
    if (cards != null && cards.isNotEmpty()) {
        (adapter as NotificationListAdapter).submitList(cards)
        smoothScrollToPosition(cards.size-1)
    }
}