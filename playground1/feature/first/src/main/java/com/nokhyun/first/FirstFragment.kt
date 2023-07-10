package com.nokhyun.first

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.nokhyun.first.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding: FragmentFirstBinding get() = _binding!!

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia by lazy {
        requireActivity().registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFirstBinding.inflate(layoutInflater)
        return binding.run {
            lifecycleOwner = this@FirstFragment.viewLifecycleOwner

            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCard?.setOnClickListener {
            startActivity(CardPaymentActivity::class.java)
        }

//        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startActivity(klass: Class<*>) {
        startActivity(Intent(requireContext(), klass))
    }
}