package com.nulltwenty.abnrepos.ui

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.nulltwenty.abnrepos.R

class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val abnRepo = args.abnRepo
        view.findViewById<TextView>(R.id.nameTextView).text = abnRepo.name
        view.findViewById<TextView>(R.id.fullNameTextView).text = abnRepo.fullName
        view.findViewById<TextView>(R.id.descriptionTextView).text = abnRepo.description
        view.findViewById<TextView>(R.id.visibilityTextView).text = abnRepo.visibility
        view.findViewById<ImageView>(R.id.repositoryOwnerAvatar).load(abnRepo.avatarUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        view.findViewById<Button>(R.id.openRepoUrlButton).setOnClickListener {
            startActivity(Intent(ACTION_VIEW, Uri.parse(abnRepo.htmlUrl)))
        }
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = abnRepo.name
    }
}