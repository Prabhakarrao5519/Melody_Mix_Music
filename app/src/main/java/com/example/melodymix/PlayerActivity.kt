package com.example.melodymix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.melodymix.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlayerBinding
    lateinit var exoPlayer: ExoPlayer

    var playerListner =object:Player.Listener{
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            showGif(isPlaying)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyExoplayer.getCurrentSong()?.apply {
            binding.songTitleTextView.text=title
            binding.songSubtitleTextView.text=subtitle

            Glide.with(binding.songCoverItemView)
                .load(coverUrl)
                .circleCrop()
                .into(binding.songCoverItemView)

            Glide.with(binding.songGifImage)
                .load(R.drawable.music)
                .circleCrop()
                .into(binding.songGifImage)

            exoPlayer=MyExoplayer.getInstance()!!
            binding.playerView.player=exoPlayer
            binding.playerView.showController()
            exoPlayer.addListener(playerListner)


        }
    }
    fun onDestroyed(){
        super.onDestroy()
        exoPlayer?.removeListener(playerListner)
    }
    fun showGif(show : Boolean){
        if (show)
            binding.songGifImage.visibility=View.VISIBLE
        else
            binding.songGifImage.visibility=View.INVISIBLE

    }
}