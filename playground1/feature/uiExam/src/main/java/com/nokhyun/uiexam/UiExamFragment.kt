package com.nokhyun.uiexam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.nokhyun.uiexam.exams.GroupNode
import com.nokhyun.uiexam.exams.runApp
import com.nokhyun.uiexam.immutableExam.Person
import kotlinx.coroutines.Dispatchers

class UiExamFragment : Fragment() {

    @OptIn(InternalComposeApi::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val decorView = requireActivity().window?.decorView
        val rootView = decorView?.findViewById<ViewGroup>(android.R.id.content)!!

        return ComposeView(requireContext()).apply {

            setContent {
//                UiExamContent()
//                BlurContents(
//                    parent = rootView
//                )

                val person1 = Person("Hi1", "1234")
                val person2 = Person("Hi2", "5678")
                val list1 = mutableListOf<Person>(
                    person1,
                    person2,
                )


//                PeopleView(people = list1.apply { add(Person("1123", "123")) })

//                Column {
//                    App()
//                    App(User("Park"))
//                }

//                SaveableStateHolderExam()

//                runApp(GroupNode(), Recomposer(Dispatchers.Main))
            }
        }
    }
}

fun logger(block: () -> Any) {
    Log.e("UiExam", block().toString())
}