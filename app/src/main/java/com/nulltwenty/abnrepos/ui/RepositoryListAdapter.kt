package com.nulltwenty.abnrepos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nulltwenty.abnrepos.databinding.RepositoryItemRowBinding
import com.nulltwenty.abnrepos.domain.model.AbnRepo

class RepositoryListAdapter(private val onClickListener: (AbnRepo) -> Unit) :
    PagingDataAdapter<AbnRepo, RepositoryListAdapter.RepositoryViewHolder>(
        RepositoryDiffCallback
    ) {
    private lateinit var binding: RepositoryItemRowBinding

    inner class RepositoryViewHolder(private val binding: RepositoryItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: AbnRepo) {
            with(binding) {
                repositoryName.text = repo.name
                repositoryVisibility.text = repo.visibility
                repositoryOwnerAvatar.load(repo.avatarUrl) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        binding =
            RepositoryItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryViewHolder(binding)
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