package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.Tag
import org.commonmark.node.Node

inline fun buildKotlinxHtmlRenderer(
    block: KotlinxHtmlRenderer.Builder.() -> Unit = {}
) = KotlinxHtmlRenderer.Builder().apply(block).build()

fun Tag.render(
    node: Node,
    renderer: KotlinxHtmlRenderer = buildKotlinxHtmlRenderer()
) = renderer.render(node, consumer)
