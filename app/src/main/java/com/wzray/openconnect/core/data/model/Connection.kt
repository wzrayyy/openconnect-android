package com.wzray.openconnect.core.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

enum class Protocols(name: String) {
    ANYCONNECT("anyconnect")
}

@Entity(
    tableName = "connections",
    indices = [
        Index("uuid", unique = true)
    ]
)
@Serializable
data class Connection(
    val name: String,
    val url: String,
    val username: String,
    val password: String,

    val protocol: Protocols = Protocols.ANYCONNECT,
    val xmlPost: Boolean = true,
    val pfs: Boolean = false,
    val useDTLS: Boolean = true,
    val reportedOs: String = "android",
    val mtu: Int = 1280,
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
)