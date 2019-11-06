package ru.krikun.commonmark.kotlinx.html.ins

import kotlinx.html.ins
import org.commonmark.ext.ins.Ins
import org.commonmark.node.Node
import org.commonmark.renderer.NodeRenderer
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlNodeRendererContext

class InsKotlinxHtmlNodeRenderer(private val context: KotlinxHtmlNodeRendererContext) : NodeRenderer {
    private val output = context.output

    override fun getNodeTypes() = setOf<Class<out Node>>(Ins::class.java)

    override fun render(node: Node) = output.ins { renderChildren(node) }

    private fun renderChildren(parent: Node) {
        var node = parent.firstChild
        while (node != null) {
            val next = node.next
            context.render(node)
            node = next
        }
    }
}