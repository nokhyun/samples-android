package com.nokhyun.first

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.doOnAttach
import androidx.core.view.doOnPreDraw
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
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

    private val firstTopAdapter by lazy {
        FirstTopAdapter()
    }

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
    ): View {
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

        val list: List<Top> = mutableListOf<Top>().apply {
            add(Top("BubbleUp"))
            add(Top("MON"))
            add(Top("WEN"))
            add(Top("WEN"))
            add(Top("WEN"))
            add(Top("WEN"))
            add(Top("WEN"))
            add(Top("WEN"))
            add(Top("END"))
        }

        binding.rvFirst?.apply {
            setHasFixedSize(true)
            adapter = firstTopAdapter
            layoutManager = GridLayoutManager(requireContext(), list.size).apply {
            }
            addItemDecoration(SpacingItemDecorator(-14))
        }

        firstTopAdapter.submitList(list)
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
//                        add(tlTab.newTab().apply { customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_chip, null, true) })
                    }
//                    add(tlTab.newTab().apply { customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_circle, null, true) })
//                    add(tlTab.newTab().apply { customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_circle_chip, null, true) })
                }
//                add(tlTab.newTab().apply { customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_chip_sub, null, true) })

                add(tlTab.newTab().apply { text = "BBBBBBB" })
                add(tlTab.newTab().apply { text = "Mon" })
                add(tlTab.newTab().apply { text = "Tue" })
                add(tlTab.newTab().apply { text = "Wed" })
                add(tlTab.newTab().apply { text = "Thu" })
                add(tlTab.newTab().apply { text = "Fri" })
                add(tlTab.newTab().apply { text = "Sat" })
                add(tlTab.newTab().apply { text = "Sun" })
                add(tlTab.newTab().apply { text = "End" })

//                add(tlTab.newTab().apply { text = "버버버" })
//                add(tlTab.newTab().apply { text = "월" })
//                add(tlTab.newTab().apply { text = "화" })
//                add(tlTab.newTab().apply { text = "수" })
//                add(tlTab.newTab().apply { text = "목" })
//                add(tlTab.newTab().apply { text = "금" })
//                add(tlTab.newTab().apply { text = "토" })
//                add(tlTab.newTab().apply { text = "일" })
//                add(tlTab.newTab().apply { text = "막막" })
            }.forEach {
                tlTab.addTab(it)
            }

            // customView 사용 시
//            (tlTab.getChildAt(0) as LinearLayout)
//                .children
//                .filterIsInstance<TabLayout.TabView>()
//                .forEachIndexed { index, tab ->
//                    if (tab.tab?.customView is Chip) {
//                        (tab.tab?.customView as Chip).setOnClickListener {
//                            clearTab()
//                            selectTab(tab.tab)
//                            tab.tab?.select()
//                        }
//                    }
//
//                    tab.setOnClickListener {
//                        clearTab()
//                        selectTab(tab.tab)
//                        tab.tab?.select()
//                    }
//                }
        }

        binding.tlTab?.doOnAttach {
            binding.tlTab?.forEachIndexed { index, _ ->
//                if(index == 0){
//                    setTabwidth(index, 0.15f)
//                }
//                setTabwidth(index, 0.1f)
//                setTabWidth(index, if(index == 0) 0.15f else 0.1f)
                setTabWidth(index, if(index == 0) 1f else 0.7f)
            }

//            (binding.tlTab!!.getChildAt(0) as ViewGroup).let { tabView ->
//                tabView.children.forEach {
//                    val paddingStart = it.paddingStart
//                    val paddingEnd = it.paddingEnd
//                }
//            }
        }
    }

    private fun selectTab(tab: TabLayout.Tab?) {
        if (tab == null) return
        if (tab.customView == null) {
            tab.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        } else {
            when (val customView = tab.customView) {
                is Chip -> {
                    if ((customView as? Chip)?.id == R.id.chip_two) return
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
                    when (val customView = tab.tab?.customView) {
                        is Chip -> {
                            if ((customView as? Chip)?.id == R.id.chip_two) return
                            (customView as? Chip)?.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
                        }

                        is TextView -> {
                            (customView as? TextView)?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
                        }
                    }
                }
            }
    }

    private fun setTabWidth(position: Int, weight: Float) {
        val tabLayout = binding.tlTab
        val layout: LinearLayout = (tabLayout?.getChildAt(0) as LinearLayout).getChildAt(position) as LinearLayout
        val layoutParams: LinearLayout.LayoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = weight
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layout.setLayoutParams(layoutParams)

        val tablayoutParams: ViewGroup.LayoutParams? = tabLayout.layoutParams
        tablayoutParams?.width=ViewGroup.LayoutParams.WRAP_CONTENT
        tabLayout.layoutParams =tablayoutParams
    }
}

fun logger(log: () -> String) {
    Log.e("logger", log())
}

suspend fun coroutineLogger(log: suspend () -> String) {
    Log.e("logger", log())
}

class SpacingItemDecorator(private val padding: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
//        outRect.top = padding
//        outRect.bottom = padding
        outRect.left = padding
        outRect.right = padding
    }
}