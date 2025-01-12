package com.wzray.openconnect.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wzray.openconnect.core.data.dao.ConnectionsDao
import com.wzray.openconnect.core.data.model.Connection

@Database(
    entities = [
        Connection::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun connectionsDao(): ConnectionsDao
}
