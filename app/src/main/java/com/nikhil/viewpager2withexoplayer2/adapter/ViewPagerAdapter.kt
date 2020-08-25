package com.nikhil.viewpager2withexoplayer2.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nikhil.viewpager2withexoplayer2.activity.MainActivity
import com.nikhil.viewpager2withexoplayer2.fragment.ExoPlayerFragment

class ViewPagerAdapter(
    private val activity: MainActivity,
    private val videoList: ArrayList<String>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun createFragment(position: Int): Fragment {
        return ExoPlayerFragment(activity, videoList[position], position)
    }
}