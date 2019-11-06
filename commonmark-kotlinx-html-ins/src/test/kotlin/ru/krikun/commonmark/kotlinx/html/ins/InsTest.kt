package ru.krikun.commonmark.kotlinx.html.ins

import kotlinx.html.TagConsumer
import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.junit.Test
import ru.krikun.commonmark.kotlinx.html.RenderingTestCase
import ru.krikun.commonmark.kotlinx.html.buildKotlinxHtmlRenderer

class InsTest : RenderingTestCase() {
    private val extensions: Set<Extension> = setOf(InsKotlinxHtmlExtension())
    private val parser: Parser = Parser.builder().extensions(extensions).build()
    private val renderer = buildKotlinxHtmlRenderer { extensions(extensions) }

    @Test
    fun ins() {
        assertRendering("++foo++", "<p><ins>foo</ins></p>")
    }

    override fun render(output: TagConsumer<*>, source: String) {
        renderer.render(parser.parse(source), output)
    }
}