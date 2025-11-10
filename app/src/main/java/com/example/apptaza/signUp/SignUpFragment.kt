package com.example.apptaza.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.example.apptaza.R
import com.example.apptaza.databinding.FragmentSignUpBinding
import com.example.apptaza.di.Network
import com.example.apptaza.network.SignUpRepositoryImpl
import java.lang.String.format


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels {
        viewModelFactory {
            initializer {
                val handle = createSavedStateHandle()
                // подставь свою реализацию репозитория
                val repo: SignUpRepository = SignUpRepositoryImpl(Network.api)
                SignUpViewModel(repo, handle)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val phoneEt = binding.PhoneNumber
        val btn = binding.btnNext
        val progress = ProgressBar(requireContext()).apply { isIndeterminate = true }

        phoneEt.doOnTextChanged { text, _, _, _ ->
            viewModel.onPhoneChanged(
                text?.toString().orEmpty()
            )
        }

        btn.setOnClickListener { viewModel.onContinue() }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                btn.isEnabled = state.isValid && !state.isLoading
                btn.text =
                    if (state.isLoading) getString(R.string.loading) else getString(R.string.signup_continue)
                if (state.error != null) {

                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                if (phoneEt.hasFocus().not()) {
                    phoneEt.setText(format(state.phone))     // "+7 (999) 123-45-67"
                    phoneEt.setSelection(phoneEt.text?.length ?: 0)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.effects.collect { effect ->
                if (effect is SignUpViewModel.Effect.GoToOtp) {
                    findNavController().navigate(
                        R.id.action_signUpFragment_to_passCodeFragment,
                        bundleOf("phone" to effect.phone)
                    )
                }
            }
        }
    }

    private fun formatRu(digits: String): String {
        return when {
            digits.isEmpty() -> ""
            else -> buildString {
                val d = digits.padEnd(11, '_')
                append("+7 (").append(d[0])
                append(" (").append(d.substring(1, 4)).append(" (")
                append(d.substring(4, 7)).append("-")
                append(d.substring(7, 9)).append("-")
                append(d.substring(9, 11))
            }
        }
    }
}
