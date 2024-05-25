package com.example.githubapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.data.remote.response.UserResponseItem
import com.example.githubapp.databinding.FragmentItemBinding
import com.example.githubapp.ui.utill.loadImageUrl

class RecyclerViewAdapter(
    private val userList: List<UserResponseItem>,
    private val navDirections: (String) -> NavDirections
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(userList[position])

    override fun getItemCount(): Int = userList.size

    inner class ViewHolder(private val binding: FragmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserResponseItem) {
            binding.ivAvatar.loadImageUrl(binding.root.context, data.avatarUrl)
            data.login?.let { username ->
                binding.tvUsername.text = username
                binding.cardUserItem.setOnClickListener {
                    binding.root.findNavController().navigate(
                        navDirections(username)
                    )
                }
            }
        }
    }
}