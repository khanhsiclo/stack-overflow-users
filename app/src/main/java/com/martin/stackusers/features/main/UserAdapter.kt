package com.martin.stackusers.features.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.martin.stackusers.R
import com.martin.stackusers.databinding.ItemBottomLoadingBinding
import com.martin.stackusers.databinding.ItemUserBinding
import com.martin.stackusers.models.User

class UserAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ITEM = 1
        const val TYPE_BOTTOM_LOADING = 2
    }

    interface Listener {
        fun onUserSelected(user: User)
    }

    private val users = ArrayList<User>()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding: ItemUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_user, parent, false)
            return UserViewHolder(binding)
        }

        val binding: ItemBottomLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_bottom_loading, parent, false)
        return LoadingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!users[position].isFooter) {
            (holder as UserViewHolder).bind(users[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (users[position].isFooter) TYPE_BOTTOM_LOADING else TYPE_ITEM
    }

    fun swapData(
        users: List<User>,
        hasMore: Boolean
    ) {
        this.users.clear()
        this.users.addAll(users)
        if (hasMore && users.isNotEmpty()) addLoadingFooter()

        notifyDataSetChanged()
    }

    private fun addLoadingFooter() {
        val footer = User()
        footer.isFooter = true
        this.users.add(footer)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvName.text = user.displayName
            binding.tvLocation.text = user.location

            val reputation = "${user.reputation} ${itemView.context.getString(R.string.reputation)}"
            binding.tvReputation.text = reputation

            val location = if (user.location.isEmpty()) itemView.context.getString(R.string.unknown_location) else user.location
            binding.tvLocation.text = location

            Glide.with(itemView.context).load(user.profileImage).into(binding.ivAvatar)
            binding.root.setOnClickListener { listener?.onUserSelected(user) }
        }
    }

    inner class LoadingViewHolder(binding: ItemBottomLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}