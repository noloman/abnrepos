package com.nulltwenty.abnrepos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nulltwenty.abnrepos.R
import com.nulltwenty.abnrepos.domain.model.AbnRepo

class RepositoryListAdapter(private val onClickListener: (AbnRepo) -> Unit) :
    PagingDataAdapter<AbnRepo, RepositoryListAdapter.RepositoryViewHolder>(
        RepositoryDiffCallback
    ) {
    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val repositoryName: TextView = itemView.findViewById(R.id.repositoryName)
        private val repositoryVisibility: TextView =
            itemView.findViewById(R.id.repositoryVisibility)
        private val ownerAvatar: ImageView = itemView.findViewById(R.id.repositoryOwnerAvatar)

        fun bind(repo: AbnRepo) {
            repositoryName.text = repo.name
            repositoryVisibility.text = repo.visibility.name
            ownerAvatar.load(repo.avatarUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.repository_item_row, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val abnRepo = getItem(position)
        holder.itemView.setOnClickListener {
            if (abnRepo != null) {
                onClickListener.invoke(abnRepo)
            }
        }
        if (abnRepo != null) {
            holder.bind(abnRepo)
        }
    }
}

object RepositoryDiffCallback : DiffUtil.ItemCallback<AbnRepo>() {
    override fun areItemsTheSame(
        oldItem: AbnRepo, newItem: AbnRepo
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: AbnRepo, newItem: AbnRepo
    ): Boolean = oldItem == newItem

}