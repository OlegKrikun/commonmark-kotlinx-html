package ru.krikun.commonmark.kotlinx.html.heading.anchor

import kotlinx.html.TagConsumer
import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.junit.Test
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlRenderer
import ru.krikun.commonmark.kotlinx.html.RenderingTestCase

class HeadingAnchorTest : RenderingTestCase() {
    private val extensions: Set<Extension> = setOf(HeadingAnchorKotlinxHtmlExtension.Builder().build())
    private val parser: Parser = Parser.builder().extensions(extensions).build()
    private val renderer = KotlinxHtmlRenderer.Builder().extensions(extensions).build()

    @Test
    fun `heading anchor`() {
        assertRendering("# Heading", "<h1 id=\"heading\">Heading</h1>")
    }

    override fun render(output: TagConsumer<*>, source: String) {
        renderer.render(parser.parse(source), output)
    }
}