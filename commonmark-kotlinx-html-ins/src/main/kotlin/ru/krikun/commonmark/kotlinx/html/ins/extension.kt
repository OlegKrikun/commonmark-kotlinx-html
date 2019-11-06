package ru.krikun.commonmark.kotlinx.html.ins

import org.commonmark.ext.ins.internal.InsDelimiterProcessor
import org.commonmark.parser.Parser
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlRenderer

class InsKotlinxHtmlExtension : Parser.ParserExtension, KotlinxHtmlRenderer.KotlinxHtmlRendererExtension {
    override fun extend(parserBuilder: Parser.Builder) {
        parserBuilder.customDelimiterProcessor(InsDelimiterProcessor())
    }

    override fun extend(rendererBuilder: KotlinxHtmlRenderer.Builder) {
        rendererBuilder.nodeRendererFactory { InsKotlinxHtmlNodeRenderer(it) }
    }
}