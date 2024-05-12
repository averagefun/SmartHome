package ru.ifmo.se.log

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import ch.qos.logback.core.encoder.Encoder
import org.apache.commons.io.IOUtils
import org.apache.commons.io.IOUtils.write
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class HttpAppender: AppenderBase<ILoggingEvent>() {

    private val protocol = "http"
    private val path = "/log"
    private val port = 8080
    private var body: String? = null
    private val url = "localhost"

    private var encoder: Encoder<ILoggingEvent>? = null

    override fun append(event: ILoggingEvent?) {
        var conn: HttpURLConnection? = null

        try {
            val urlObj = URL(protocol, url, port, path);
            addInfo("URL: $url")
            conn = urlObj.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            val objEncoded = encoder!!.encode(event)
            val isOk = sendBodyRequest(objEncoded, conn)
            if (!isOk) {
                addError("Not OK")
                return
            }
        } catch (e: java.lang.Exception) {
            addError("Exception", e)
            return
        } finally {
            try {
                conn?.disconnect()
            } catch (e: java.lang.Exception) {
                addError("Exception", e)
            }
        }
    }

    private fun sendBodyRequest(objEncoded: ByteArray?, conn: HttpURLConnection?): Boolean {
        conn!!.doOutput = true
        write(objEncoded, conn.outputStream)
        return showResponse(conn)
    }

    private fun showResponse(conn: HttpURLConnection): Boolean {
        val responseCode = conn.responseCode

        if (responseCode != HttpURLConnection.HTTP_OK) {
            addError(String.format("Error to send logs: %s", conn))
            return false
        }

        val response = IOUtils.toString(conn.inputStream, Charset.defaultCharset())
        addInfo(String.format("Response result: %s", response))
        return true
    }

    fun getBody(): String {
        return body!!
    }

    fun setBody(body: String?) {
        this.body = body
    }

    fun getEncoder(): Encoder<ILoggingEvent> {
        return encoder!!
    }

    fun setEncoder(encoder: Encoder<ILoggingEvent>?) {
        this.encoder = encoder
    }
}