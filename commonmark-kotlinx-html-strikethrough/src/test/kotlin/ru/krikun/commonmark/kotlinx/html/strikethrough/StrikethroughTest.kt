package ru.krikun.commonmark.kotlinx.html.strikethrough

import kotlinx.html.TagConsumer
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
        assertRendering("~~foo~~", "<p><del>foo</del></p>")
    }

    override fun render(output: TagConsumer<*>, source: String) {
        renderer.render(parser.parse(source), output)
    }
}