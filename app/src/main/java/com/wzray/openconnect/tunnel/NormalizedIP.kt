package com.wzray.openconnect.tunnel

import java.util.Locale

// TODO: single interface, v4 and v6 versions

@Suppress("MemberVisibilityCanBePrivate", "unused")
data class NormalizedIP(val ip: String, val cidr: Int) {
    constructor(ip: String, mask: String) : this(
        ip,
        if(mask.contains(".")) maskToCidr(mask) else mask.toInt()
    )

    constructor(addrWithNetMask: String) : this(
        addrWithNetMask.split("/")[0],
        addrWithNetMask.split("/")[1],
    )

    override fun toString(): String {
        return String.format(Locale.ROOT, "%s/%d", ip, cidr)
    }

    companion object {
        fun maskToCidr(mask: String) = 32 - ((ipToInt(mask).inv()).countOneBits())

        fun cidrToMask(cidr: Int) = intToIp(((1 shl cidr) - 1) shl (32 - cidr))

        fun ipToInt(ip: String) = ip.split(".").map { it.toInt() }.reversed()
            .reduceIndexed { idx, acc, it -> acc + (it shl (idx * 8)) }

        fun intToIp(ip: Int) = String.format(
            Locale.ROOT, "%d.%d.%d.%d",
            ip and (0xff shl 24) shr 24,
            ip and (0xff shl 16) shr 16,
            ip and (0xff shl 8) shr 8,
            ip and 0xff
        )
    }
}

