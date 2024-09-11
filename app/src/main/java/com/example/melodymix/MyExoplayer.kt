package com.example.melodymix

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.melodymix.models.SongModel

object MyExoplayer {
    private var exoPlayer:ExoPlayer?=null
    private var currentsong:SongModel?=null

    fun getCurrentSong():SongModel?{
        return currentsong


    }

    fun getInstance(): ExoPlayer? {
        return exoPlayer

    }
    @SuppressLint("SuspiciousIndentation")
    fun startPlaying(context: Context, song: SongModel){
        if (exoPlayer==null)
        exoPlayer=ExoPlayer.Builder(context).build()

        if(currentsong!=song){
            currentsong=song
            currentsong?.url?.apply {
                val mediaItem=MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()

            }

        }


    }

}