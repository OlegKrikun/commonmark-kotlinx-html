package ru.krikun.commonmark.kotlinx.html.ins

import kotlinx.html.INS
import kotlinx.html.attributesMapOf
import kotlinx.html.visit
import org.commonmark.ext.ins.Ins
import org.commonmark.node.Node
import org.commonmark.renderer.NodeRenderer
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlNodeRendererContext

class InsKotlinxHtmlNodeRenderer(private val context: KotlinxHtmlNodeRendererContext) : NodeRenderer {
    private val consumer = context.output

    override fun getNodeTypes() = setOf<Class<out Node>>(Ins::class.java)

    override fun render(node: Node) = INS(attributesMapOf(), consumer).visit {
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