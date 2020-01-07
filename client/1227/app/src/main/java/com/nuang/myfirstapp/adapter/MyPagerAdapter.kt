package com.nuang.myfirstapp.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.myfirstapp.FirstFragment
import com.nuang.myfirstapp.SecondFragment
import com.nuang.myfirstapp.ThirdFragment

class MyPagerAdapter(fm: FragmentManager, uid: String) : FragmentPagerAdapter(fm) {
    val userid = uid

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString("uid", userid)
        Log.d("uid>>", userid)
        Log.d("shit", "shit")
        val fragment = when (position) { //switch()문과 동일하다.
            0 -> {
                FirstFragment()
            }
            1 -> {
                SecondFragment()
            }
            else -> {
                ThirdFragment()
            }
        }
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "CONTACTS"
            1 -> "GALLERY"
            else -> {return "WEATHERS"}
        }
    }
}