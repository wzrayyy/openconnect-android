package com.wzray.openconnect.tunnel

import android.util.Log
import com.wzray.openconnect.core.data.model.Connection
import org.infradead.libopenconnect.LibOpenConnect
import java.io.IOException

class OpenConnect(private val connection: Connection) : Runnable {
    private val vpnService: VpnService = VpnService()
    private lateinit var backend: Backend

    private fun createBackend() {
        backend = Backend()

        backend.setProtocol(connection.protocol.name)
        backend.setXMLPost(connection.xmlPost)
        backend.setPFS(connection.pfs)
        if (!connection.useDTLS)
            backend.disableDTLS()
        backend.setReportedOS(connection.reportedOs)

        backend.setMobileInfo(
            "1.0",
            connection.reportedOs,
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        )

        val retVal = backend.parseURL(connection.url)
        Log.e(TAG, "call to oc.parseURL: $retVal")
        if (retVal != 0)
            throw Exception("Invalid url")
    }

    override fun run() {
        createBackend()

        var retVal = backend.obtainCookie()
        Log.e(TAG, "obtainCookie ret: $retVal")

        retVal = backend.makeCSTPConnection()
        Log.e(TAG, "makeCSTPConnection ret: $retVal")

        val b = vpnService.getBuilder()
        b.setSession(connection.name)
        val ip = backend.ipInfo

        Log.e(TAG, "call to svc.getBuilder()")

        val normalizedIP = NormalizedIP(ip.addr, ip.netmask)
        b.addAddress(ip.addr, normalizedIP.cidr)
        b.setMtu(1280)
        b.addRoute("0.0.0.0", 0)
        b.addDnsServer("1.1.1.1")

        val pfd = b.establish()
        Log.e(TAG, "call to b.establish(): ${pfd != null}")

        backend.setupTunFD(pfd!!.fd)
        Log.e(TAG, "call to oc.setupTunFD(${pfd.fd})")

        while (true)
            if (backend.mainloop(300, LibOpenConnect.RECONNECT_INTERVAL_MIN) < 0)
                break

        try {
            pfd.close()
        } catch (_: IOException) {
        }
    }

    fun cancel() {
        backend.cancel()
    }

    inner class Backend : LibOpenConnect() {
        override fun onProcessAuthForm(authForm: AuthForm): Int {
            val opt = authForm.opts[0]

            Log.d(TAG, "Auth form ${authForm.message}")
            when (opt.type) {
                OC_FORM_OPT_TEXT -> opt.value = connection.username
                OC_FORM_OPT_PASSWORD -> opt.value = connection.password
                else -> Log.e(TAG, "FAIL AUTH FORM ${authForm.message}")
            }
            return 0
        }

        override fun onProgress(level: Int, msg: String?) {
            if (msg == null) return

            Log.println(
                when (level) {
                    PRG_ERR -> Log.ERROR
                    PRG_INFO -> Log.INFO
                    PRG_DEBUG -> Log.DEBUG
                    else -> Log.VERBOSE
                }, TAG, msg
            )
        }

        override fun onProtectSocket(fd: Int) {
            val x = vpnService.protect(fd)
            Log.d(TAG, "onProtectSocket, result: $x, fd: $fd")
        }
    }

    class VpnService : android.net.VpnService() {
        fun getBuilder(): android.net.VpnService.Builder {
            return Builder()
        }

        override fun onRevoke() {
            super.onRevoke()
        }
    }

    companion object {
        private const val TAG = "OpenConnect/Backend"
    }
}

