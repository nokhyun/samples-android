package com.nokhyun.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nokhyun.common.databinding.DialogSampleBottomSheetBinding

class SampleBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: DialogSampleBottomSheetBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogSampleBottomSheetBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.run {
            btnSnack.setOnClickListener {
                DefaultSnackBar(
                    view = dialog?.window?.decorView!!,
                    message = "Hello World!",
                    drawable = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_lock_lock)!!
                ).show()
            }
        }
    }
}