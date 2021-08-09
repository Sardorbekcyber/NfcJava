package com.turbosoft.nfcjava

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.turbosoft.nfcjava.proto.ProtoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val TAG: String = "====MobilApp"

class MainActivity : AppCompatActivity() {

    private lateinit var EXAMPLE_COUNTER: Preferences.Key<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.tv_number)
        val btn = findViewById<Button>(R.id.btn_incre)
        val et = findViewById<EditText>(R.id.et_num)
        val btnNex = findViewById<Button>(R.id.btn_next)

        EXAMPLE_COUNTER = stringPreferencesKey("edit_string")
        val exampleCounterFlow = applicationContext.settings.data
            .map { preferences ->
                preferences[EXAMPLE_COUNTER] ?: "Default String"
            }

        CoroutineScope(Dispatchers.Main).launch {
            Log.d(TAG, "exampleCounterFlow: ${exampleCounterFlow.first()}")
            exampleCounterFlow
                .catch { cause: Throwable ->
                    Log.d(TAG, "readFromDataStore: ${cause.message}")
                }
                .collect {
                    tv.text = it
                }
        }

        btn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                incrementCounter(applicationContext, EXAMPLE_COUNTER, et.text.toString())
            }
        }
        btnNex.setOnClickListener {
            startActivity(Intent(applicationContext, ProtoActivity::class.java))
        }
    }
}

suspend fun incrementCounter(
    context: Context,
    EXAMPLE_COUNTER: Preferences.Key<String>,
    newValue: String
) {
    context.settings.edit { settings ->
        val currentCounterValue = settings[EXAMPLE_COUNTER]
        Log.d(TAG, "newValue: $newValue")
        Log.d(TAG, "incrementCounter: $currentCounterValue")
        settings[EXAMPLE_COUNTER] = newValue
    }
}
