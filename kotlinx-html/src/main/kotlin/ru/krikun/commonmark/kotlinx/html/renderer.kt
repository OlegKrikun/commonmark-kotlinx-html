package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.FlowContent
import kotlinx.html.HTMLTag
import org.commonmark.Extension
import org.commonmark.internal.renderer.NodeRendererMap
import org.commonmark.node.Node

class KotlinxHtmlRenderer private constructor(builder: Builder) {
    private val coreRendererFactory: KotlinxHtmlNodeRendererFactory = { KotlinxHtmlCoreNodeRenderer(it) }
    private val nodeRendererFactories = builder.nodeRendererFactories + coreRendererFactory
    private val attributeProviderFactories = builder.attributeProviderFactories.toList()

    fun render(node: Node, output: FlowContent) = RendererContext(
        nodeRendererFactories,
        attributeProviderFactories,
        output
    ).render(node)

    interface KotlinxHtmlRendererExtension : Extension {
        fun extend(rendererBuilder: Builder)
    }

    class Builder {
        internal val nodeRendererFactories = mutableListOf<KotlinxHtmlNodeRendererFactory>()
        internal val attributeProviderFactories = mutableListOf<KotlinxHtmlAttributeProviderFactory>()

        fun nodeRendererFactory(nodeRendererFactory: KotlinxHtmlNodeRendererFactory) = apply {
            nodeRendererFactories.add(nodeRendererFactory)
        }

        fun attributeProviderFactory(attributeProviderFactory: KotlinxHtmlAttributeProviderFactory) = apply {
            attributeProviderFactories.add(attributeProviderFactory)
        }

        fun extensions(extensions: Iterable<Extension>) = apply {
            extensions.filterIsInstance<KotlinxHtmlRendererExtension>().forEach { it.extend(this) }
        }

        fun build() = KotlinxHtmlRenderer(this)
    }

    private class RendererContext(
        nodeRendererFactories: List<KotlinxHtmlNodeRendererFactory>,
        attributeProviderFactories: List<KotlinxHtmlAttributeProviderFactory>,
        override val output: FlowContent
    ) : KotlinxHtmlNodeRendererContext, KotlinxHtmlAttributeProviderContext {
        private val nodeRendererMap = NodeRendererMap()
        private val attributeProviderList = attributeProviderFactories.map { it(this) }

        init {
            nodeRendererFactories.reversed().forEach { nodeRendererMap.add(it(this)) }
        }

        override fun render(node: Node) = nodeRendererMap.render(node)

        override fun extendAttributes(node: Node, tag: HTMLTag) = attributeProviderList.forEach { it(node, tag) }
    }
}

fun FlowContent.render(
    node: Node,
    renderer: KotlinxHtmlRenderer = KotlinxHtmlRenderer.Builder().build()
) = renderer.render(node, this)