package com.wzray.openconnect.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wzray.openconnect.Application
import com.wzray.openconnect.core.viewmodels.LogLevel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class PreferenceFieldBase<StorageType, ValueType>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<StorageType>,
    protected open val default: ValueType,
) {
    abstract fun toStorage(v: ValueType): StorageType
    abstract fun fromStorage(v: StorageType?): ValueType?

    @Composable
    fun asState(): State<ValueType> =
        remember { dataStore.data.map { fromStorage(it[key]) ?: default } }.collectAsState(default)

    fun set(value: ValueType?) = applicationScope.launch {
        dataStore.edit { if (value != null) it[key] = toStorage(value) else it.remove(key) }
    }
}


class PreferenceField<T>(
    dataStore: DataStore<Preferences>,
    key: Preferences.Key<T>,
    default: T,
) : PreferenceFieldBase<T, T>(dataStore, key, default) {
    override fun toStorage(v: T): T = v
    override fun fromStorage(v: T?): T? = v
}


class EnumPreferenceField<E : Enum<E>>(
    dataStore: DataStore<Preferences>,
    key: Preferences.Key<String>,
    default: E,
) : PreferenceFieldBase<String, E>(dataStore, key, default) {
    override fun toStorage(v: E): String = v.name
    override fun fromStorage(v: String?): E? =
        v?.let { default::class.java.enumConstants!!.firstOrNull { it.name == v } }
}

enum class UiMode {
    AUTO, DARK, LIGHT
}

object Settings {
    private val dataStore = Application.prefDataStore

    private val KEY_FOLLOW_LOGS = booleanPreferencesKey("follow_logs")
    private val KEY_LAST_CONNECTION = stringPreferencesKey("last_connection")
    private val KEY_UI_MODE = stringPreferencesKey("ui_mode")
    private val KEY_LOG_LEVEL = stringPreferencesKey("log_level")
    private val KEY_USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")


    val followLogs = PreferenceField(dataStore, KEY_FOLLOW_LOGS, true)
    val lastConnection = PreferenceField(dataStore, KEY_LAST_CONNECTION, "")
    val uiMode = EnumPreferenceField(dataStore, KEY_UI_MODE, UiMode.AUTO)
    val logLevel = EnumPreferenceField(dataStore, KEY_LOG_LEVEL, LogLevel.INFO)
    val useDynamicColor = PreferenceField(dataStore, KEY_USE_DYNAMIC_COLOR, true)
}