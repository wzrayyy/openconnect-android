package com.wzray.openconnect

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.wzray.openconnect.core.data.Database
import com.wzray.openconnect.tunnel.VpnManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference

@HiltAndroidApp
class Application : android.app.Application() {
    private lateinit var prefDataStore: DataStore<Preferences>
    private lateinit var db: Database
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main.immediate)
    private var vpnManager: VpnManager = VpnManager(this)

    override fun onCreate() {
        super.onCreate()
        prefDataStore = PreferenceDataStoreFactory.create {
            applicationContext.preferencesDataStoreFile("settings")
        }
        db = Room.databaseBuilder(this, Database::class.java, "database.db").build()
        Log.i(TAG, "Starting application...")
    }

    companion object {
        private const val TAG = "OpenConnect/Application"

        private lateinit var thisWeakRef: WeakReference<Application>
        fun get() = thisWeakRef.get()!!

        val prefDataStore
            get() = get().prefDataStore
        val db: Database
            get() = get().db
        val coroutineScope
            get() = get().coroutineScope
        val vpnManager
            get() = get().vpnManager
    }

    init {
        thisWeakRef = WeakReference(this)
        System.loadLibrary("openconnect")
        System.loadLibrary("stoken")
    }
}