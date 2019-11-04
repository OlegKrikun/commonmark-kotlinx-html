package ru.krikun.commonmark.kotlinx.html.ext.gfm.strikethrough

import kotlinx.html.del
import org.commonmark.ext.gfm.strikethrough.Strikethrough
import org.commonmark.node.Node
import org.commonmark.renderer.NodeRenderer
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlNodeRendererContext

class StrikethroughKotlinxHtmlNodeRenderer(private val context: KotlinxHtmlNodeRendererContext) : NodeRenderer {
    private val output = context.output

    override fun getNodeTypes() = setOf<Class<out Node>>(Strikethrough::class.java)

    override fun render(node: Node) = output.del { renderChildren(node) }

    private fun renderChildren(parent: Node) {
        var node = parent.firstChild
        while (node != null) {
            val next = node.next
            context.render(node)
            node = next
        }
    }
}