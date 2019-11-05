package ru.krikun.commonmark.kotlinx.html.ext.gfm.tables

import kotlinx.html.TBODY
import kotlinx.html.TD
import kotlinx.html.TH
import kotlinx.html.THEAD
import kotlinx.html.TR
import kotlinx.html.attributesMapOf
import kotlinx.html.table
import kotlinx.html.visit
import org.commonmark.ext.gfm.tables.TableBlock
import org.commonmark.ext.gfm.tables.TableBody
import org.commonmark.ext.gfm.tables.TableCell
import org.commonmark.ext.gfm.tables.TableHead
import org.commonmark.ext.gfm.tables.TableRow
import org.commonmark.node.Node
import org.commonmark.renderer.NodeRenderer
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlNodeRendererContext

class TablesKotlinxHtmlNodeRenderer(private val context: KotlinxHtmlNodeRendererContext) : NodeRenderer {
    private val output = context.output

    override fun getNodeTypes() = setOf(
        TableBlock::class.java,
        TableHead::class.java,
        TableBody::class.java,
        TableRow::class.java,
        TableCell::class.java
    )

    override fun render(node: Node) {
        when (node) {
            is TableBlock -> table(node)
            is TableHead -> head(node)
            is TableBody -> body(node)
            is TableRow -> row(node)
            is TableCell -> cell(node)
            else -> error("unsupported node type: ${node::class.java}")
        }
    }

    private fun table(node: TableBlock) = output.table { renderChildren(node) }

    private fun head(node: TableHead) = THEAD(attributesMapOf(), output.consumer).visit { renderChildren(node) }

    private fun body(node: TableBody) = TBODY(attributesMapOf(), output.consumer).visit { renderChildren(node) }

    private fun row(node: TableRow) = TR(attributesMapOf(), output.consumer).visit { renderChildren(node) }

    private fun cell(node: TableCell) {
        val attrs = when {
            node.alignment != null -> attributesMapOf("align", node.alignment.value)
            else -> attributesMapOf()
        }
        when {
            node.isHeader -> TH(attrs, output.consumer).visit { renderChildren(node) }
            else -> TD(attrs, output.consumer).visit { renderChildren(node) }
        }
    }

    private val TableCell.Alignment.value
        get() = when (this) {
            TableCell.Alignment.LEFT -> "left"
            TableCell.Alignment.CENTER -> "center"
            TableCell.Alignment.RIGHT -> "right"
            else -> error("unknown alignment: $this")
        }

    private fun renderChildren(parent: Node) {
        var node = parent.firstChild
        while (node != null) {
            val next = node.next
            context.render(node)
            node = next
        }
    }
}