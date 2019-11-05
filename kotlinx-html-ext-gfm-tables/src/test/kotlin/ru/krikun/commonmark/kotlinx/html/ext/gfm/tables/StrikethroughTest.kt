package ru.krikun.commonmark.kotlinx.html.ext.gfm.tables

import kotlinx.html.FlowContent
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
            "<div><table><thead><tr><th>Abc</th><th>Def</th></tr></thead><tbody><tr><td>1</td><td>2</td></tr></tbody></table></div>"
        );
    }

    override fun render(flowContent: FlowContent, source: String) {
        renderer.render(parser.parse(source), flowContent)
    }
}