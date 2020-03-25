package com.example.itunesalbum.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

private val baseSearchUrl = "https://itunes.apple.com/search?term="
private val parameterAlbum = "&entity=album&attribute=albumTerm"
private val baseLookupUrl = "https://itunes.apple.com/lookup?id="
private val parameterSongs = "&entity=song"


fun generateUrlSearch(request: String): URL { //generates URL for ITunes API request for album in MainActivity
    var url = URL(baseSearchUrl + request + parameterAlbum)
    return url
}


fun generateUrlLookup(request: String): URL{ //generates URL for ITunes API request for album in AlbumDetailActivity
    var url = URL(baseLookupUrl + request + parameterSongs)
    return url
}

fun getStringFromResponse(request: String):String?{
    val client = OkHttpClient()
    val url = generateUrlSearch(request)
    val request = Request.Builder().url(url).build()
    val response = client.newCall(request).execute()
    return response.body?.string().toString()  //gets JSON string using OkHTTP3 library
}


