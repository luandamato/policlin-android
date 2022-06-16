package com.policlinsaude.newfeature.features.tickets.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.FragmentTicketsBinding


class TicketsFragment : Fragment() {

    private lateinit var binding: FragmentTicketsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }



}