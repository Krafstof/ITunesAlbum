
package com.example.itunesalbum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class AlbumAdapter(var context: Context, var dataSource: ArrayList<Album>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Album? {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {  //custom array adapter for ListView in MainActivity
        val rowView = inflater.inflate(R.layout.album_list_item, parent, false)
        val artistTextView = rowView.findViewById(R.id.textView_artist) as TextView
        val albumTextView = rowView.findViewById(R.id.textView_album) as TextView
        val coverImageView = rowView.findViewById(R.id.imageView_cover) as ImageView

        val album = getItem(position) as Album
        artistTextView.text = album.artistName
        albumTextView.text = album.albumName
        Picasso.get().load(album.cover).into(coverImageView)

        return rowView
    }
}
