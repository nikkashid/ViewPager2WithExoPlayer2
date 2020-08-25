package com.nikhil.viewpager2withexoplayer2.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.nikhil.viewpager2withexoplayer2.R
import com.nikhil.viewpager2withexoplayer2.activity.MainActivity
import com.nikhil.viewpager2withexoplayer2.application.ExoPlayerCaching
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

    private lateinit var simpleCache: SimpleCache

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentExoPlayerBinding.bind(view)
        Log.d(TAG, "onViewCreated: Fragment Position : $positionOfFragment")
        initializePlayer()
        initTouchEvent()
    }

    private fun initTouchEvent() {
        viewBinding!!.flMainLayout.setOnClickListener {
            viewBinding!!.playerView.player!!.playWhenReady =
                !viewBinding!!.playerView.player!!.isPlaying

            if (viewBinding!!.playerView.player!!.isPlaying) {
                viewBinding!!.ivPauseVideo.visibility = GONE
            } else {
                viewBinding!!.ivPauseVideo.visibility = VISIBLE
            }
        }
    }

    private fun initializePlayer() {

        simpleCache = ExoPlayerCaching.simpleCache!!

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

        val loopingSource = LoopingMediaSource(mediaSource)
        simpleExoPlayer.prepare(loopingSource)

        viewBinding!!.playerView.useController = false
        viewBinding!!.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
        viewBinding!!.playerView.setRepeatToggleModes(Player.REPEAT_MODE_ALL)
        viewBinding!!.playerView.player = simpleExoPlayer
    }

    private fun buildMediaSource(
        uri: Uri,
        mediaDataSourceFactory: DefaultDataSourceFactory
    ): MediaSource {

        //adding caching
        val cacheDataSourceFactory = CacheDataSourceFactory(
            simpleCache, mediaDataSourceFactory, CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
        )

        // This is the MediaSource representing the media to be played.
        val extension: String = uri.toString().substring(uri.toString().lastIndexOf("."))
        if (extension.contains("mp4")) {
            return ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(uri)
        } else {
            return HlsMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(uri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
        simpleExoPlayer.release()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: $positionOfFragment")
        if (viewBinding != null) {
            viewBinding!!.playerView.player!!.playWhenReady =
                !viewBinding!!.playerView.player!!.isPlaying
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: $positionOfFragment")
        if (viewBinding != null) {
            viewBinding!!.playerView.player!!.playWhenReady =
                !viewBinding!!.playerView.player!!.isPlaying
        }
    }
}