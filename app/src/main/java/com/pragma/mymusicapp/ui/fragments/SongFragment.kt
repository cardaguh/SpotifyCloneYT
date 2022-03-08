package com.pragma.mymusicapp.ui.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.pragma.mymusicapp.R
import com.pragma.mymusicapp.data.entities.FavoriteSong
import com.pragma.mymusicapp.data.entities.Song
import com.pragma.mymusicapp.data.remote.FavoriteSongsDatabase
import com.pragma.mymusicapp.data.remote.FavoritesViewModel
import com.pragma.mymusicapp.exoplayer.isPlaying
import com.pragma.mymusicapp.exoplayer.toSong
import com.pragma.mymusicapp.other.Status.SUCCESS
import com.pragma.mymusicapp.ui.MainActivity
import com.pragma.mymusicapp.ui.viewmodels.MainViewModel
import com.pragma.mymusicapp.ui.viewmodels.SongViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_song.*
import kotlinx.android.synthetic.main.fragment_song.imageViewFavorites
import kotlinx.android.synthetic.main.row_favorite_song.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Observer

@AndroidEntryPoint
class SongFragment : Fragment(R.layout.fragment_song) {

    @Inject
    lateinit var glide: RequestManager

    private lateinit var mainViewModel: MainViewModel
    private val songViewModel: SongViewModel by viewModels()

    private var curPlayingSong: Song? = null
    set(value) {
        field = value
        favoritesViewModel.checkIsFavourite(value)
    }

    private var playbackState: PlaybackStateCompat? = null

    private var shouldUpdateSeekbar = true

    private var favoriteSong:FavoriteSong ?= null

    private val favoritesViewModel: FavoritesViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val dataBaseInstance = FavoriteSongsDatabase.getDatabasenIstance(requireContext())
        favoritesViewModel.setInstanceOfDb(dataBaseInstance)
        subscribeToObservers()

        ivPlayPauseDetail.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }
        })

        ivSkipPrevious.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }

        ivSkip.setOnClickListener {
            mainViewModel.skipToNextSong()
        }

        /*val dataBaseInstance = FavoriteSongsDatabase.getDatabasenIstance(requireContext())
        favoritesViewModel.setInstanceOfDb(dataBaseInstance)*/

        /*favoriteSong?.apply {
            textViewName.text=this.name
            Glide.with(imageView.context).load(this.photo).into(imageView)
            imageViewFavorites.setOnClickListener {
                favoritesViewModel.checkRecordExist(this)
            }
        }*/
        //observerViewModel()

        imageViewFavorites.setOnClickListener {
            favoritesViewModel.onFavouriteClick(curPlayingSong)
        }
        observerViewModel()
    }

    private fun observerViewModel() {
        favoritesViewModel?.checkRecord?.observe(viewLifecycleOwner, Observer {
            if (!it.second) {
                favoritesViewModel!!.insert(it.first)
                Toast.makeText(requireContext(), "Item added in favorites list", Toast.LENGTH_SHORT)
                    .show()
            } else {
                favoritesViewModel!!.delete(it.first)
                Toast.makeText(requireContext(), "Item remove from favorites", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        favoritesViewModel.isFavourite.observe(viewLifecycleOwner){
            if(it==true)
            imageViewFavorites.setImageResource(R.drawable.ic_favorite_selected)
            else
                imageViewFavorites.setImageResource(R.drawable.ic_favorite)
        }
    }

    private fun updateTitleAndSongImage(song: Song) {
        val title = "${song.title} - ${song.subtitle}"
        tvSongName.text = title
        glide.load(song.imageUrl).into(ivSongImage)
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                when(result.status) {
                    SUCCESS -> {
                        result.data?.let { songs ->
                            if(curPlayingSong == null && songs.isNotEmpty()) {
                                curPlayingSong = songs[0]
                                updateTitleAndSongImage(songs[0])
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if(it == null) return@observe
            curPlayingSong = it.toSong()
            updateTitleAndSongImage(curPlayingSong!!)
        }
        mainViewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            ivPlayPauseDetail.setImageResource(
                if(playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
            )
            seekBar.progress = it?.position?.toInt() ?: 0
        }
        songViewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if(shouldUpdateSeekbar) {
                seekBar.progress = it.toInt()
                setCurPlayerTimeToTextView(it)
            }
        }
        songViewModel.curSongDuration.observe(viewLifecycleOwner) {
            seekBar.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            tvSongDuration.text = dateFormat.format(it)
        }
    }

    private fun setCurPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        tvCurTime.text = dateFormat.format(ms)
    }
}





















