package com.example.itunesalbum

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.itunesalbum.utils.getStringFromResponse
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var albumListView: ListView
    private lateinit var searchButton: Button
    private lateinit var albumEdit: EditText
    private lateinit var errorTextView: TextView
    private lateinit var loadingBar: ProgressBar

    private lateinit var albumList:ArrayList<Album>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews() //sets all findViewById() Views
        findAlbumAndFillList() //searches for album name provided by search string and fills list of albums with data
        showDetailedInfo() //shows new activity with detailed info about album
    }


    internal inner class getITunesAlbum: AsyncTask<String,Void,String>(){

        override fun onPreExecute(){
            loadingBar.visibility = View.VISIBLE //sets loading screensaver visible
        }

        override fun doInBackground(vararg params: String?): String? {
            return getStringFromResponse(params[0].toString())
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null && result != "") {
                albumList = Album.getAlbumsFromResponse(result, this@MainActivity) //initialize ArrayList with Album objects
                albumList.sort() //sort ArrayList using custom compare
                albumListView.adapter = AlbumAdapter(this@MainActivity, albumList) //shows sorted albums with pictures and names of artists
                thereIsError(false)
            }else thereIsError(true)

            loadingBar.visibility = View.INVISIBLE //sets loading screensaver invisible
        }
    }


    private fun findViews(){//sets all findViewById() Views
        albumEdit = findViewById(R.id.editText_search)
        searchButton = findViewById(R.id.button_search)
        albumListView = findViewById(R.id.album_list)
        errorTextView = findViewById(R.id.errorTV)
        loadingBar = findViewById(R.id.progress_circular)
    }

    private fun findAlbumAndFillList(){
        searchButton.setOnClickListener {
            val albumName = albumEdit.text.toString()
            albumName.replace(' ', '+')//replace because of ITunes Search API URL request specification
            getITunesAlbum().execute(albumName)
        }
    }

    private fun showDetailedInfo(){ //starts new activity with detailed information about album by clicking on it in ListView
        albumListView.setOnItemClickListener { _, _, position, _ ->
            val selectedAlbum = albumList[position]
            val detailIntent = AlbumDetailActivity.newIntent(this, selectedAlbum)
            startActivity(detailIntent)
        }
    }

    private fun thereIsError(flag:Boolean){  //shows error message if something went wrong
        if (flag){
            albumListView.visibility = View.INVISIBLE
            errorTextView.visibility = View.VISIBLE
        } else {
            albumListView.visibility = View.VISIBLE
            errorTextView.visibility = View.INVISIBLE
        }
    }

}