package com.nulltwenty.abnrepos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nulltwenty.abnrepos.R
import com.nulltwenty.abnrepos.domain.model.AbnRepo

class RepositoryListAdapter : ListAdapter<AbnRepo, RepositoryListAdapter.RepositoryViewHolder>(
    RepositoryDiffCallback
) {
    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val repositoryName: TextView = itemView.findViewById(R.id.repositoryName)

        fun bind(repo: AbnRepo) {
            repositoryName.text = repo.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.repository_item_row, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val abnRepo = getItem(position)
        holder.bind(abnRepo)
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