package com.nulltwenty.abnrepos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nulltwenty.abnrepos.R
import data.api.model.RepositoryListResponseElement

class RepositoryListAdapter :
    ListAdapter<RepositoryListResponseElement, RepositoryListAdapter.RepositoryViewHolder>(
        RepositoryDiffCallback
    ) {
    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val repositoryName: TextView = itemView.findViewById(R.id.repositoryName)

        fun bind(repo: RepositoryListResponseElement) {
            repositoryName.text = repo.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.repository_item_row, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }
}

object RepositoryDiffCallback : DiffUtil.ItemCallback<RepositoryListResponseElement>() {
    override fun areItemsTheSame(
        oldItem: RepositoryListResponseElement, newItem: RepositoryListResponseElement
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: RepositoryListResponseElement, newItem: RepositoryListResponseElement
    ): Boolean = oldItem == newItem

}