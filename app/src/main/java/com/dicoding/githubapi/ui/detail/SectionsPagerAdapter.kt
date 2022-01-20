package com.dicoding.githubapi.ui.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubapi.ui.detail.FragmentFollowers
import com.dicoding.githubapi.ui.detail.FragmentFollowing

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FragmentFollowers.newInstance(position + 1, username)
            1 -> fragment = FragmentFollowing.newInstance(position + 1, username)
        }
        return fragment as Fragment
    }

}