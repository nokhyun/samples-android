package com.nokhyun.first

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nokhyun.first.databinding.FragmentFirstBinding
import com.nokhyun.network.FakeService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirstFragment : Fragment() {

    @Inject
    lateinit var fakeService: FakeService

    private var _binding: FragmentFirstBinding? = null
    private val binding: FragmentFirstBinding get() = _binding!!

    private val firstAdapter by lazy {
        FirstAdapter { view, transitionName ->
            findNavController().navigate(
                R.id.actionFirstFragmentToFristDetialFragment,
                bundleOf(
                    "firstModel" to FirstModel(name = transitionName)
                ),
                null,
                navigatorExtras = FragmentNavigatorExtras(
                    view to transitionName
                )
            )
        }
    }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(layoutInflater)
        sharedElementEnterTransition = android.transition.TransitionInflater.from(context)
            .inflateTransition(R.transition.transition_test)

        return binding.run {
            lifecycleOwner = this@FirstFragment.viewLifecycleOwner
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        (binding.root.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
//        super.onViewCreated(view, savedInstanceState)

        binding.btnCard?.setOnClickListener {
            startActivity(CardPaymentActivity::class.java)
        }

        binding.rvFirst?.apply {
            logger { "rv apply" }
            setHasFixedSize(true)
            adapter = firstAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
//        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        viewLifecycleOwner.lifecycleScope.launch {
            coroutineLogger { fakeService.todo().id.toString() }
            binding.rvFirst.run {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startActivity(klass: Class<*>) {
        startActivity(Intent(requireContext(), klass))
    }
}

fun logger(log: () -> String) {
    Log.e("logger", log())
}

suspend fun coroutineLogger(log: suspend () -> String) {
    Log.e("logger", log())
}
