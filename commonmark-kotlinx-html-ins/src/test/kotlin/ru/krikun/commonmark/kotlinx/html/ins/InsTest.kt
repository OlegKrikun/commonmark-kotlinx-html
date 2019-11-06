package ru.krikun.commonmark.kotlinx.html.ins

import kotlinx.html.TagConsumer
import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.junit.Test
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlRenderer
import ru.krikun.commonmark.kotlinx.html.RenderingTestCase

class InsTest : RenderingTestCase() {
    private val extensions: Set<Extension> = setOf(InsKotlinxHtmlExtension())
    private val parser: Parser = Parser.builder().extensions(extensions).build()
    private val renderer = KotlinxHtmlRenderer.Builder().extensions(extensions).build()

    @Test
    fun ins() {
        assertRendering("++foo++", "<p><ins>foo</ins></p>")
    }

    override fun render(output: TagConsumer<*>, source: String) {
        renderer.render(parser.parse(source), output)
    }
}