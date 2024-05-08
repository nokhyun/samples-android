package com.nokhyun.first

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.nokhyun.common.DefaultSnackBar
import com.nokhyun.first.databinding.FragmentFirstBinding
import com.nokhyun.network.FakeService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text
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

        initTab()

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000L)
            DefaultSnackBar.make(binding.root, "aaa", ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_foreground)!!).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startActivity(klass: Class<*>) {
        startActivity(Intent(requireContext(), klass))
    }

    private fun initTab() {
        with(binding) {
            if (tlTab == null) return@with
            mutableListOf<TabLayout.Tab>().apply {
                repeat(7) {
                    if (it == 0) {
                        add(tlTab.newTab().apply { customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_chip, null, true) })
                    }
//                    add(tlTab.newTab().apply { text = it.plus(1).toString() })
                    add(tlTab.newTab().apply { customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_circle, null, true) })
//                    add(tlTab.newTab().apply { customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_circle_chip, null, true) })
                }
                add(tlTab.newTab().apply { customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_chip_sub, null, true) })
            }.forEach {
                tlTab.addTab(it)
            }

            (tlTab.getChildAt(0) as LinearLayout)
                .children
                .filterIsInstance<TabLayout.TabView>()
                .forEachIndexed { index, tab ->
                    if (tab.tab?.customView is Chip) {
                        (tab.tab?.customView as Chip).setOnClickListener {
                            clearTab()
                            selectTab(tab.tab)
                            tab.tab?.select()
                        }
                    }

                    tab.setOnClickListener {
                        clearTab()
                        selectTab(tab.tab)
                        tab.tab?.select()
                    }
                }
        }
    }

    private fun selectTab(tab: TabLayout.Tab?) {
        if (tab == null) return
        if(tab.customView == null){
            tab.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        }else {
            val customView = tab.customView

            when(customView){
                is Chip -> {
                    if((customView as? Chip)?.id == R.id.chip_two) return
                    (customView as? Chip)?.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), R.color.purple_200)
                }
                is TextView -> {
                    (customView as? TextView)?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple_200)
                }
            }
        }
    }

    private fun clearTab() {
        (binding.tlTab?.getChildAt(0) as LinearLayout)
            .children
            .filterIsInstance<TabLayout.TabView>()
            .forEach { tab ->
                if (tab.tab?.customView == null) {
                    tab.tab?.view?.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
                } else {
                    val customView = tab.tab?.customView

                    when(customView){
                        is Chip -> {
                            if((customView as? Chip)?.id == R.id.chip_two) return
                            (customView as? Chip)?.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
                        }
                        is TextView -> {
                            (customView as? TextView)?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
                        }
                    }
                }
            }
    }
}

fun logger(log: () -> String) {
    Log.e("logger", log())
}

suspend fun coroutineLogger(log: suspend () -> String) {
    Log.e("logger", log())
}
