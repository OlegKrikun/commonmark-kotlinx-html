package ru.krikun.commonmark.kotlinx.html.ins

import kotlinx.html.FlowContent
import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.junit.Test
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlRenderer
import ru.krikun.commonmark.kotlinx.html.RenderingTestCase
import ru.krikun.commonmark.kotlinx.html.heading.anchor.HeadingAnchorKotlinxHtmlExtension

class HeadingAnchorTest : RenderingTestCase() {
    private val extensions: Set<Extension> = setOf(HeadingAnchorKotlinxHtmlExtension.Builder().build())
    private val parser: Parser = Parser.builder().extensions(extensions).build()
    private val renderer = KotlinxHtmlRenderer.Builder().extensions(extensions).build()

    @Test
    fun `heading anchor`() {
        assertRendering("# Heading", "<div><h1 id=\"heading\">Heading</h1></div>")
    }

    override fun render(flowContent: FlowContent, source: String) {
        renderer.render(parser.parse(source), flowContent)
    }
}