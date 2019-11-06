package ru.krikun.commonmark.kotlinx.html.strikethrough

import kotlinx.html.DEL
import kotlinx.html.attributesMapOf
import kotlinx.html.visit
import org.commonmark.ext.gfm.strikethrough.Strikethrough
import org.commonmark.node.Node
import org.commonmark.renderer.NodeRenderer
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlNodeRendererContext

class StrikethroughKotlinxHtmlNodeRenderer(private val context: KotlinxHtmlNodeRendererContext) : NodeRenderer {
    private val consumer = context.output

    override fun getNodeTypes() = setOf<Class<out Node>>(Strikethrough::class.java)

    override fun render(node: Node) = DEL(attributesMapOf(), consumer).visit {
        context.extendAttributes(node, this)
        renderChildren(node)
    }

    private fun renderChildren(parent: Node) {
        var node = parent.firstChild
        while (node != null) {
            val next = node.next
            context.render(node)
            node = next
        }
    }
}