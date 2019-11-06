package ru.krikun.commonmark.kotlinx.html.tables

import org.commonmark.ext.gfm.tables.internal.TableBlockParser
import org.commonmark.parser.Parser
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlRenderer

class TablesKotlinxHtmlExtension : Parser.ParserExtension, KotlinxHtmlRenderer.KotlinxHtmlRendererExtension {
    override fun extend(parserBuilder: Parser.Builder) {
        parserBuilder.customBlockParserFactory(TableBlockParser.Factory())
    }

    override fun extend(rendererBuilder: KotlinxHtmlRenderer.Builder) {
        rendererBuilder.nodeRendererFactory { TablesKotlinxHtmlNodeRenderer(it) }
    }
}