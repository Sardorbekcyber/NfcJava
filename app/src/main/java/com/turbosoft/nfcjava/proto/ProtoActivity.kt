package com.turbosoft.nfcjava.proto

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.turbosoft.nfcjava.Organization
import com.turbosoft.nfcjava.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
private const val SORT_ORDER_KEY = "sort_order"

class ProtoActivity : AppCompatActivity() {

    private val TAG: String = "====MobilApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proto)
        val tvOrg = findViewById<TextView>(R.id.tv_org_proto)
        val id = findViewById<EditText>(R.id.et_id_proto)
        val name = findViewById<EditText>(R.id.et_name_proto)
        val btn = findViewById<Button>(R.id.btn_save_proto)

        btn.setOnClickListener {
            val orgId = id.text.toString().toInt()
            val orgName = name.text.toString()
            CoroutineScope(Dispatchers.Default).launch {
                updateShowCompleted(orgId, orgName)
            }
        }

        val organizationFlow: Flow<Organization> = organization.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    Log.e(TAG, "Error reading sort order preferences.", exception)
                    emit(Organization.getDefaultInstance())
                } else {
                    throw exception
                }
            }

        CoroutineScope(Dispatchers.Main).launch {
            organizationFlow
                .catch { cause: Throwable ->
                    Log.d(TAG, "error: $cause")
                }
                .collect {
                    tvOrg.text = it.toString()
                }
        }
    }

    private suspend fun updateShowCompleted(id: Int, name: String) {
        organization.updateData { preferences ->
            preferences.toBuilder().setId(id)
                .setUsername(name)
                .build()
        }
    }
}

private val Context.organization: DataStore<Organization> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserPreferencesSerializer
)

