package com.nikhil.viewpager2withexoplayer2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.nikhil.viewpager2withexoplayer2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    lateinit var mainViewBinding: ActivityMainBinding

    private val RECORD_REQUEST_CODE: Int = 101

    lateinit var pagerAdapter: FragmentStateAdapter

    lateinit var videoList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

        setupPermissions()

        mainViewBinding.btnReqPermission.setOnClickListener { makeRequest() }
    }

    private fun initViewPagerView() {

        initializeList()
        pagerAdapter = ViewPagerAdapter(this, videoList)
        mainViewBinding.viewPager.adapter = pagerAdapter
        mainViewBinding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to read storage denied")
            makeRequest()
        } else {
            initViewPagerView()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        getString(R.string.request_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                    mainViewBinding.btnReqPermission.visibility = View.VISIBLE
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                    mainViewBinding.btnReqPermission.visibility = View.GONE
                    initViewPagerView()
                }
            }
        }
    }

    private fun initializeList() {
        videoList = ArrayList()

        videoList.add("asset:///dummy_video.mp4")
        videoList.add("/storage/emulated/0/snaptube/download/SnapTube Video/OpMs_ Corazon on Instagram_ _please fallow me if y(MP4).mp4")
        videoList.add("/storage/emulated/0/snaptube/download/SnapTube Video/Facebook undefined(480p)_1.mp4")
        videoList.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
        videoList.add("/storage/emulated/0/snaptube/download/SnapTube Video/THE INDIAN SARCASM®️ on Instagram_ _------------__(MP4).mp4")
        videoList.add("/storage/emulated/0/snaptube/download/SnapTube Video/Facebook 1743407759166692(270p).mp4")
    }
}