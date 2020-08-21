package com.nikhil.viewpager2withexoplayer2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.nikhil.viewpager2withexoplayer2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainViewBinding: ActivityMainBinding

    lateinit var pagerAdapter: FragmentStateAdapter

    lateinit var videoList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

        initView()
    }

    private fun initView() {

        initializeList()
        pagerAdapter = ViewPagerAdapter(this, videoList)
        mainViewBinding.viewPager.adapter = pagerAdapter
        mainViewBinding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

    }

    private fun initializeList() {
        videoList = ArrayList()
        //videoList.add("https://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4")
        videoList.add("asset:///dummy_video.mp4")
        //videoList.add("https://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4")
        videoList.add("asset:///dummy_video.mp4")
        //videoList.add("https://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4")
    }
}