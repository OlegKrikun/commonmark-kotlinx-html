package ru.krikun.commonmark.kotlinx.html.ext.gfm.strikethrough

import org.commonmark.ext.gfm.strikethrough.internal.StrikethroughDelimiterProcessor
import org.commonmark.parser.Parser
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlRenderer

class StrikethroughKotlinxHtmlExtension : Parser.ParserExtension, KotlinxHtmlRenderer.KotlinxHtmlRendererExtension {
    override fun extend(parserBuilder: Parser.Builder) {
        parserBuilder.customDelimiterProcessor(StrikethroughDelimiterProcessor())
    }

    override fun extend(rendererBuilder: KotlinxHtmlRenderer.Builder) {
        rendererBuilder.nodeRendererFactory { StrikethroughKotlinxHtmlNodeRenderer(it) }
    }
}