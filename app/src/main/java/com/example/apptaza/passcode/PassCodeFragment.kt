package com.example.apptaza.passcode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.apptaza.R
import com.example.apptaza.databinding.FragmentPassCodeBinding


class PassCodeFragment : Fragment() {
    private lateinit var binding: FragmentPassCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPassCodeBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val phone = requireArguments().getString("phone").orEmpty()
        view.findViewById<TextView>(R.id.tvNotification).text =
            getString(R.string.code_sent_to, phone)
    }
}