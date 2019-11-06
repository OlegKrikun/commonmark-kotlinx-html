package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.HTMLTag
import org.commonmark.node.Node

typealias KotlinxHtmlAttributeProviderFactory = (context: KotlinxHtmlAttributeProviderContext) -> KotlinxHtmlAttributeProvider
typealias KotlinxHtmlAttributeProvider = (node: Node, tag: HTMLTag) -> Unit

interface KotlinxHtmlAttributeProviderContext
