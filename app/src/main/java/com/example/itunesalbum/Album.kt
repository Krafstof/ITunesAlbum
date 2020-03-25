package com.example.itunesalbum

import android.content.Context
import android.widget.ImageView
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class Album(val albumName:String, val artistName:String, val cover:String, val id:String) :
    Comparable<Album> {



    companion object {

        fun getAlbumsFromResponse(response:String, context:Context): ArrayList<Album>{  //initialize ArrayList with Album objects from JSON string response
            val albumList = ArrayList<Album>()
            try {
                val albums = JSONObject(response).getJSONArray("results")

                (0 until albums.length()).mapTo(albumList){
                    Album(albums.getJSONObject(it).getString("collectionName"),
                        albums.getJSONObject(it).getString("artistName"),
                        albums.getJSONObject(it).getString("artworkUrl100"),
                        albums.getJSONObject(it).getString("collectionId"))
                }


            }catch (e:JSONException){
                e.printStackTrace()
            }
            return albumList
        }
    }

    override fun compareTo(other: Album): Int {  //custom comparison for sorting album alphabetically comparing album names
        return this.albumName.compareTo(other.albumName)
    }

}