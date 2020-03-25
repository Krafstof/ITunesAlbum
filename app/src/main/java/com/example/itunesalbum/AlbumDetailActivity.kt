package com.example.itunesalbum

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.itunesalbum.utils.generateUrlLookup
import com.example.itunesalbum.utils.getStringFromResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_album_detail.*
import okhttp3.OkHttpClient
import okhttp3.Request

class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var albumDetailed: AlbumDetailed

    private lateinit var coverImageView: ImageView
    private lateinit var songList:ListView
    private lateinit var textView_artist:TextView
    private lateinit var textView_album:TextView
    private lateinit var textView_genre:TextView
    private lateinit var textView_currency:TextView
    private lateinit var textView_price:TextView
    private lateinit var textView_copyright:TextView
    private lateinit var textView_country:TextView
    private lateinit var textView_date:TextView
    private lateinit var loadingBar:ProgressBar

    companion object {
        const val extra_id = "collectionId"

        fun newIntent(context: Context, album: Album): Intent {
            val detailIntent = Intent(context, AlbumDetailActivity::class.java)

            detailIntent.putExtra(extra_id, album.id)//gets album ID from MainActivity

            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_detail)

        findViews()//sets all findViewById() Views

        val id = intent.extras.getString(extra_id)
        getAlbumFromResponse().execute(id)

    }



    internal inner class getAlbumFromResponse :AsyncTask<String, Void, String>(){

        override fun onPreExecute() {
            loadingBar.visibility = View.VISIBLE//sets loading screensaver visible
        }

        override fun doInBackground(vararg params: String?): String? {
            val client = OkHttpClient()
            val url = generateUrlLookup(params[0].toString())
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            return response.body?.string().toString() //gets JSON string using OkHTTP3 library
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null && result != "") {
                albumDetailed = AlbumDetailed.getAlbumFromID(result, this@AlbumDetailActivity)!!
            }
            fillInfo()//sets text to TextViews, album cover to ImageView and initialize ListView with song names
            loadingBar.visibility = View.INVISIBLE
        }


    }

    private fun findViews(){ //sets all findViewById() Views
        songList = findViewById(R.id.song_list)
        coverImageView = findViewById(R.id.imageView_cover)
        loadingBar = findViewById(R.id.loadingBar)
        textView_date = findViewById(R.id.textView_date)
        textView_country = findViewById(R.id.textView_country)
        textView_copyright = findViewById(R.id.textView_copyright)
        textView_price = findViewById(R.id.textView_price)
        textView_currency = findViewById(R.id.textView_currency)
        textView_genre = findViewById(R.id.textView_genre)
        textView_album = findViewById(R.id.textView_album)
        textView_artist = findViewById(R.id.textView_artist)
    }

    private fun fillInfo(){//sets text to TextViews, album cover to ImageView and initialize ListView with song names
        textView_artist.text = albumDetailed.artistName
        textView_album.text = albumDetailed.albumName
        textView_genre.text = albumDetailed.genre
        textView_currency.text = albumDetailed.currency
        textView_price.text = albumDetailed.price
        textView_copyright.text = albumDetailed.copyright
        textView_country.text = albumDetailed.country
        textView_date.text = albumDetailed.date
        Picasso.get().load(albumDetailed.cover).into(coverImageView)

        val listItems = arrayOfNulls<String>(albumDetailed.songs.size)
        for (i in 0 until albumDetailed.songs.size){
            val song = albumDetailed.songs[i]
            listItems[i]=song
        }
        songList.adapter = ArrayAdapter(this@AlbumDetailActivity, android.R.layout.simple_list_item_1, listItems)
    }


}
