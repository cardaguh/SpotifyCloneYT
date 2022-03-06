package com.pragma.mymusicapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pragma.mymusicapp.R
import com.pragma.mymusicapp.data.entities.FavoriteSong
import kotlinx.android.synthetic.main.row_favorite_song.view.*

//import kotlinx.android.synthetic.main.row_tourisitc_site.view.*

class FavoriteSongAdapter(private var touristicsites: MutableList<FavoriteSong>,
                          private val onFavoritesClick: (Int,FavoriteSong) -> Unit = { _,_ -> },
                          private val onItemClick: (Int,FavoriteSong) -> Unit = { _,_ -> }) : RecyclerView.Adapter<FavoriteSongAdapter.MViewHolder>() {

    val context: Context
        get() {
            TODO()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_favorite_song, parent, false)
        return MViewHolder(view)
    }

    override fun getItemCount(): Int {
        return touristicsites.size
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        holder.bind(touristicsites[position])
    }

    fun update(data: MutableList<FavoriteSong>) {
        touristicsites = data
        notifyDataSetChanged()
    }

    inner class MViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val textViewName : TextView = view.textViewName
        private val imageView : ImageView = view.imageView
        private val imageViewFavorites : ImageView = view.imageViewFavorites
        private val textViewLink: TextView =view.textViewLink

        fun bind(touristicsite: FavoriteSong) {
            textViewName.text = touristicsite.name
            Glide.with(imageView.context).load(touristicsite.photo).into(imageView)
            imageViewFavorites.setOnClickListener {
                onFavoritesClick(adapterPosition,touristicsite)
                Toast.makeText(context, "click favorite adapter", Toast.LENGTH_SHORT).show()
                Log.d("Carlos Daniel", "Click en button favorite imageView")
            }

            textViewLink.setOnClickListener {
                onItemClick(adapterPosition,touristicsite)
            }

        }
    }

}
