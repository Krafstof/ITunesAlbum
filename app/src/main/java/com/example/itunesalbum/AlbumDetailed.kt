package com.example.itunesalbum

import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class AlbumDetailed(val albumName:String, val artistName:String, val cover:String,
                    val copyright:String, val country:String, val genre:String,
                    val date: String, val price:String, val currency: String, val songs:ArrayList<String>) {
    companion object {
        fun getAlbumFromID(response: String, context: Context): AlbumDetailed? {
            val albumDetailed: AlbumDetailed
            try {
                val albumArray = JSONObject(response).getJSONArray("results")
                val songList = ArrayList<String>()

                (1 until albumArray.length()).mapTo(songList){
                    albumArray.getJSONObject(it).getString("trackName")
                }

                albumDetailed = AlbumDetailed(
                    albumArray.getJSONObject(0).getString("collectionName"),
                    albumArray.getJSONObject(0).getString("artistName"),
                    albumArray.getJSONObject(0).getString("artworkUrl100"),
                    "copyright: "+albumArray.getJSONObject(0).getString("copyright"),
                    "country: "+albumArray.getJSONObject(0).getString("country"),
                    "genre: "+albumArray.getJSONObject(0).getString("primaryGenreName"),
                    "release: "+albumArray.getJSONObject(0).getString("releaseDate").substring(0,10),
                    "price: "+albumArray.getJSONObject(0).getString("collectionPrice"),
                    albumArray.getJSONObject(0).getString("currency"),
                    songList
                )

                return albumDetailed
            } catch (e: JSONException) {
                e.printStackTrace()
                return null
            }
        }
    }
}
