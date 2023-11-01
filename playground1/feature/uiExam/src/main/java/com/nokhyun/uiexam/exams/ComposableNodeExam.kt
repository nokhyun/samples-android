package com.nokhyun.uiexam.exams

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.nokhyun.uiexam.logger

// 다음과 같은 노드 기본 유형을 가진 트리가 있는 경우
abstract class Node {
    val children = mutableListOf<Node>()
}

// 다음과 같은 Applier 클래스를 구현하여 Compose를 작성하는 방법을 가르칩니다.
// 노드 트리를 관리합니다.
class NodeApplier(root: Node) : AbstractApplier<Node>(root) {
    override fun insertTopDown(index: Int, instance: Node) {
        current.children.add(index, instance)
    }

    override fun insertBottomUp(index: Int, instance: Node) {
        // 트리가 하향식으로 작성되므로 무시됩니다.
    }

    override fun move(from: Int, to: Int, count: Int) {
        current.children.move(from, to, count)
    }

    override fun onClear() {
        root.children.clear()
    }

    override fun remove(index: Int, count: Int) {
        root.children.remove(index, count)
    }
}

// 루트 노드가 제공되는 컴포지션을 생성하기 위해 다음과 같은 함수를 생성할 수 있습니다.
fun Node.setContent(
    parent: CompositionContext,
    content: @Composable () -> Unit,
): Composition = Composition(NodeApplier(this), parent).apply { setContent(content) }

// "TextNode" 및 "GroupNode"와 같은 노드 하위 클래스가 있다고 가정합니다.
class TextNode : Node() {
    var text: String = ""
    var onClick: () -> Unit = {}
}

class GroupNode : Node() {

}

// 구성 가능한 항목을 만들 수 있습니다.
@Composable
fun Text(text: String, onClick: () -> Unit = {}) {
    ComposeNode<TextNode, NodeApplier>(::TextNode) {
        set(text) { this.text = it }
        set(onClick) { this.onClick = it }
    }
}

@Composable
fun Group(
    content: @Composable () -> Unit,
) {
    ComposeNode<GroupNode, NodeApplier>(::GroupNode, update = {}, content)
}

// 그러면 샘플 트리가 구성될 수 있습니다.
fun runApp(root: GroupNode, parent: CompositionContext) {
    root.setContent(parent = parent) {
        var count by remember { mutableStateOf(0) }

        Group {
            Text(text = "Count: $count")
            Text(text = "Increment") { count++ }
        }
    }
}