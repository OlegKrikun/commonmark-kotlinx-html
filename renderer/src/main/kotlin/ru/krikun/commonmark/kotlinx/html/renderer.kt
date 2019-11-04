package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.FlowContent
import org.commonmark.Extension
import org.commonmark.internal.renderer.NodeRendererMap
import org.commonmark.node.Node

class KotlinxHtmlRenderer private constructor(builder: Builder) {
    private val coreRendererFactory: KotlinxHtmlNodeRendererFactory = { KotlinxHtmlCoreNodeRenderer(it) }
    private val nodeRendererFactories = builder.nodeRendererFactories + coreRendererFactory

    fun render(node: Node, output: FlowContent) = RendererContext(nodeRendererFactories, output).render(node)

    interface KotlinxHtmlRendererExtension {
        fun extend(rendererBuilder: Builder)
    }

    class Builder {
        internal val nodeRendererFactories = mutableListOf<KotlinxHtmlNodeRendererFactory>()

        fun nodeRendererFactory(nodeRendererFactory: KotlinxHtmlNodeRendererFactory) = apply {
            nodeRendererFactories.add(nodeRendererFactory)
        }

        fun extensions(extensions: Iterable<Extension>) = apply {
            extensions.filterIsInstance<KotlinxHtmlRendererExtension>().forEach { it.extend(this) }
        }

        fun build() = KotlinxHtmlRenderer(this)
    }

    private class RendererContext(
        nodeRendererFactories: List<KotlinxHtmlNodeRendererFactory>,
        override val output: FlowContent
    ) : KotlinxHtmlNodeRendererContext {
        private val nodeRendererMap = NodeRendererMap()

        init {
            nodeRendererFactories.reversed().forEach { nodeRendererMap.add(it(this)) }
        }

        override fun render(node: Node) = nodeRendererMap.render(node)
    }
}

fun FlowContent.render(
    node: Node,
    renderer: KotlinxHtmlRenderer = KotlinxHtmlRenderer.Builder().build()
) = renderer.render(node, this)