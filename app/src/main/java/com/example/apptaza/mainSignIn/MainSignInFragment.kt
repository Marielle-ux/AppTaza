package com.example.apptaza.mainSignIn

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.example.apptaza.R
import com.example.apptaza.databinding.FragmentMainSignInBinding
import androidx.core.content.edit

class MainSignInFragment : Fragment() {

    private lateinit var binding: FragmentMainSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AAA", "App started")
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Log.e("AAA", "Uncaught in ${t.name}", e)
        }
        binding = FragmentMainSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSwitchLang.setOnLongClickListener { v ->
            val popup = PopupMenu(v.context, v).apply {
                menu.add(0, 1, 0, "Русский")
                menu.add(0, 2, 1, "English")
                menu.add(0, 3, 2, "Қазақша")
                setOnMenuItemClickListener { item ->
                    val tag = when (item.itemId) {
                        1 -> "ru"
                        2 -> "en"
                        3 -> "kk"
                        else -> "ru"
                    }
                    val locales = androidx.core.os.LocaleListCompat.forLanguageTags(tag)
                    androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(locales)
                    val prefs =
                        requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
                    prefs.edit { putString("app_language", tag) }

                    true
                }
            }
            popup.show()
            true
        }

        binding.btnClient.setOnClickListener {
            findNavController().navigate(R.id.action_MainSignInFragment_to_signUpFragment)
        }

        binding.btnCleaner.setOnClickListener {
            findNavController().navigate(R.id.action_MainSignInFragment_to_signUpFragment)
        }

        binding.tvSignInText.setOnClickListener {
            findNavController().navigate(R.id.action_MainSignInFragment_to_signUpFragment)
        }

        binding.tvSignUpText.setOnClickListener {
            findNavController().navigate(R.id.action_MainSignInFragment_to_signUpFragment)
        }

    }
}



