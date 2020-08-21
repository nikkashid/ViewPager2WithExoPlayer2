package com.nikhil.viewpager2withexoplayer2

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.nikhil.viewpager2withexoplayer2.databinding.FragmentExoPlayerBinding

class ExoPlayerFragment(
    private val activity: MainActivity,
    private val videoPath: String,
    position: Int
) : Fragment(R.layout.fragment_exo_player) {

    private val TAG = "ExoPlayerFragment"

    private var viewBinding: FragmentExoPlayerBinding? = null

    private lateinit var simpleExoPlayer: SimpleExoPlayer

    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private val positionOfFragment = position

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentExoPlayerBinding.bind(view)
        Log.d(TAG, "onViewCreated: Fragment Position : $positionOfFragment")
        initializePlayer()
    }

    private fun initializePlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(activity).build()
        val userAgent = Util.getUserAgent(activity, activity.getPackageName())
        mediaDataSourceFactory =
            DefaultDataSourceFactory(
                this.context,
                Util.getUserAgent(activity, userAgent)
            )
        val mediaSource = buildMediaSource(
            Uri.parse(videoPath),
            mediaDataSourceFactory as DefaultDataSourceFactory
        )

        simpleExoPlayer.prepare(mediaSource)

        viewBinding!!.playerView.player = simpleExoPlayer
        viewBinding!!.playerView.useController = true
        viewBinding!!.playerView.setRepeatToggleModes(Player.REPEAT_MODE_ONE)
    }

    private fun buildMediaSource(
        uri: Uri,
        mediaDataSourceFactory: DefaultDataSourceFactory
    ): MediaSource {

        // This is the MediaSource representing the media to be played.
        val extension: String = uri.toString().substring(uri.toString().lastIndexOf("."))
        if (extension.contains("mp4")) {
            return ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(uri)
        } else {
            return HlsMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(uri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    override fun onPause() {
        super.onPause()
        if (viewBinding != null) {
            viewBinding!!.playerView.player!!.playWhenReady =
                !viewBinding!!.playerView.player!!.isPlaying
        }
    }
}