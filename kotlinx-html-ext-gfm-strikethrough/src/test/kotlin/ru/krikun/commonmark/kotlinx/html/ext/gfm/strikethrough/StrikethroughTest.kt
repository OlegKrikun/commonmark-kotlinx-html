package ru.krikun.commonmark.kotlinx.html.ext.gfm.strikethrough

import kotlinx.html.FlowContent
import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.junit.Test
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlRenderer

import ru.krikun.commonmark.kotlinx.html.RenderingTestCase

class StrikethroughTest : RenderingTestCase() {
    private val extensions: Set<Extension> = setOf(StrikethroughKotlinxHtmlExtension())
    private val parser: Parser = Parser.builder().extensions(extensions).build()
    private val renderer = KotlinxHtmlRenderer.Builder().extensions(extensions).build()

    @Test
    fun strikethrough() {
        assertRendering("~~foo~~", "<div><p><del>foo</del></p></div>")
    }

    override fun render(flowContent: FlowContent, source: String) {
        renderer.render(parser.parse(source), flowContent)
    }
}