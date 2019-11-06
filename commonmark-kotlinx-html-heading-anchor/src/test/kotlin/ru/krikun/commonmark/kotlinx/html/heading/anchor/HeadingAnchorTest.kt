package ru.krikun.commonmark.kotlinx.html.heading.anchor

import kotlinx.html.TagConsumer
import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.junit.Test
import ru.krikun.commonmark.kotlinx.html.RenderingTestCase
import ru.krikun.commonmark.kotlinx.html.buildKotlinxHtmlRenderer

class HeadingAnchorTest : RenderingTestCase() {
    private val extensions: Set<Extension> = setOf(buildHeadingAnchorKotlinxHtmlExtension())
    private val parser: Parser = Parser.builder().extensions(extensions).build()
    private val renderer = buildKotlinxHtmlRenderer { extensions(extensions) }

    @Test
    fun `heading anchor`() {
        assertRendering("# Heading", "<h1 id=\"heading\">Heading</h1>")
    }

    override fun render(output: TagConsumer<*>, source: String) {
        renderer.render(parser.parse(source), output)
    }
}