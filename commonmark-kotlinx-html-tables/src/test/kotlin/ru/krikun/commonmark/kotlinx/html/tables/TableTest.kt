package ru.krikun.commonmark.kotlinx.html.tables

import kotlinx.html.TagConsumer
import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.junit.Test
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlRenderer
import ru.krikun.commonmark.kotlinx.html.RenderingTestCase

class TablesTest : RenderingTestCase() {
    private val extensions: Set<Extension> = setOf(TablesKotlinxHtmlExtension())
    private val parser: Parser = Parser.builder().extensions(extensions).build()
    private val renderer = KotlinxHtmlRenderer.Builder().extensions(extensions).build()

    @Test
    fun tables() {
        assertRendering(
            """
            Abc|Def 
            ---|---
             1 | 2
            """.trimIndent(),
            "<table><thead><tr><th>Abc</th><th>Def</th></tr></thead><tbody><tr><td>1</td><td>2</td></tr></tbody></table>"
        )
    }

    override fun render(output: TagConsumer<*>, source: String) {
        renderer.render(parser.parse(source), output)
    }
}