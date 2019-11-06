package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.HTMLTag
import org.commonmark.node.Node

typealias KotlinxHtmlAttributeProviderFactory = (context: KotlinxHtmlAttributeProviderContext) -> KotlinxHtmlAttributeProvider

interface KotlinxHtmlAttributeProviderContext

interface KotlinxHtmlAttributeProvider {
    fun extend(node: Node, tag: HTMLTag)
}
