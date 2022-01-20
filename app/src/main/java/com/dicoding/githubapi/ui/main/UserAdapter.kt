package com.dicoding.githubapi.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.dicoding.githubapi.User
import com.dicoding.githubapi.databinding.ItemUserBinding
import java.util.*

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private val listUser = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                tvItemName.text = user.login
                tvHtmlurl.text = user.htmlUrl
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .transform(CircleCrop())
                    .into(imgItemAvatar)
            }

        }

    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(listUser[position])
        }
    }

    override fun getItemCount(): Int = listUser.size


    interface OnItemClickCallback {
        fun onItemClicked(data: User)

    }
}