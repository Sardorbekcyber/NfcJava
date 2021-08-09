package com.turbosoft.nfcjava

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.turbosoft.nfcjava.proto.UserPreferencesSerializer

val Context.settings: DataStore<Preferences> by preferencesDataStore(name = "settings")

