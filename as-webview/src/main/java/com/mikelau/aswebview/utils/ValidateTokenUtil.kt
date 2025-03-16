package com.mikelau.aswebview.utils

import android.content.Context
import android.webkit.CookieManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

object ValidateTokenUtil {

    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    private const val TARGET_URL = "targetUrl"
    private const val ENDPOINT = "/token/validatejwt/"
    private const val BASE_URL = "https://www.alaskaair.com"

    interface Callbacks {
        fun onSuccess()
        fun onFailure()
    }

    fun validateJwt(
        jwt: String,
        value: String,
        callbacks: Callbacks,
    ) {
        val url = BASE_URL + ENDPOINT
        val formBody = FormBody.Builder()
            .add(TARGET_URL, value)
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $jwt")
            .post(formBody)
            .build()

        try {
            val client = OkHttpClient.Builder()
            client.cookieJar(object : CookieJar {
                private val cookieStore: MutableMap<HttpUrl, List<Cookie>> = HashMap()
                val cookieManager = CookieManager.getInstance()

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    for (c in cookies) {
                        cookieManager.setCookie(url.toString(), c.toString())
                    }
                    cookieManager.flush()
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return cookieStore[url] ?: ArrayList()
                }
            })
            client.readTimeout(60, TimeUnit.SECONDS)
            client.writeTimeout(60, TimeUnit.SECONDS)
            client.build().newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        callbacks.onSuccess()
                    } else {
                        callbacks.onFailure()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callbacks.onFailure()
                }
            })
        } catch (e: Exception) {
            callbacks.onFailure()
        }
    }
}