package com.wzray.openconnect.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wzray.openconnect.core.data.model.Connection
import kotlinx.coroutines.flow.Flow


@Dao
abstract class ConnectionsDao {
    @Query("SELECT COUNT(*) FROM connections")
    abstract fun count(): Flow<Int>

    @Query("SELECT * FROM connections WHERE name = :name")
    abstract fun connection(name: String): Flow<Connection>

    @Query("SELECT * FROM connections")
    abstract fun getAll(): Flow<List<Connection>>

    @Delete
    abstract fun delete(connection: Connection): Int

    @Insert
    abstract suspend fun insert(connection: Connection)

    @Update
    abstract suspend fun update(connection: Connection)
}