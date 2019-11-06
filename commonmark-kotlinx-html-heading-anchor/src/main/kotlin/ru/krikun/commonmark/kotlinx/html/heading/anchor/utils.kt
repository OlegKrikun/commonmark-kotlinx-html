package ru.krikun.commonmark.kotlinx.html.heading.anchor

inline fun buildHeadingAnchorKotlinxHtmlExtension(
    block: HeadingAnchorKotlinxHtmlExtension.Builder.() -> Unit = {}
) = HeadingAnchorKotlinxHtmlExtension.Builder().apply(block).build()
