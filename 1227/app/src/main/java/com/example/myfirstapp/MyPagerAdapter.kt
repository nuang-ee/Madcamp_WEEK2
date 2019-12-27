package com.example.myfirstapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter

class MyPagerAdapter (fm : androidx.fragment.app.FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) { //switch()문과 동일하다.
            0 -> {FirstFragment()}
            1 -> {SecondFragment()}
            else -> {return ThirdFragment()}
        }
    }

    override fun getCount(): Int {
        return 3 //3개니깐
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "탭메뉴1"
            1 -> "탭메뉴2"
            else -> {return "탭메뉴3"}
        }
    }
}