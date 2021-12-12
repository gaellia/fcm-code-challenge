package com.example.fcmexample

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fcmexample.databinding.ActivityMainBinding
import com.example.fcmexample.db.FCMExampleDB
import com.example.fcmexample.utils.PREFS_NAME
import com.example.fcmexample.utils.TOKEN
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import com.example.fcmexample.sendActivity.SendActivity


class MainActivity : AppCompatActivity(), HasDefaultViewModelProviderFactory {

    private lateinit var binding: ActivityMainBinding
    private val fcmViewModel: FCMViewModel by viewModels()
    private val clipboardManager by lazy { getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
    private val sharedPreferences by lazy { getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(FCMViewModel::class.java)) {
                    return FCMViewModel(FCMExampleDB.getDatabase(this@MainActivity)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = fcmViewModel
            copyClick = View.OnClickListener {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("token", sharedPreferences.getString(
                    TOKEN, "")))
                Snackbar.make(binding.root, "Copied to clipboard", Snackbar.LENGTH_SHORT).show()
            }
            recycler.adapter = NotificationListAdapter(ItemClickListener {

            })
            fab_sendActivity.setOnClickListener {
                startActivity(Intent(this@MainActivity, SendActivity::class.java))
            }
        }
    }
}