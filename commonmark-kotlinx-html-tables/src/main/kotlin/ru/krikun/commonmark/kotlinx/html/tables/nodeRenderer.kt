package ru.krikun.commonmark.kotlinx.html.tables

import kotlinx.html.HTMLTag
import kotlinx.html.TABLE
import kotlinx.html.TBODY
import kotlinx.html.TD
import kotlinx.html.TH
import kotlinx.html.THEAD
import kotlinx.html.TR
import kotlinx.html.attributesMapOf
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
    private val consumer = context.output

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
        }
    }

    private fun table(node: TableBlock) = TABLE(attributesMapOf(), consumer).visit {
        context.extendAttributes(node, this)
        context.renderChildren(node)
    }

    private fun head(node: TableHead) = THEAD(attributesMapOf(), consumer).visit {
        context.extendAttributes(node, this)
        context.renderChildren(node)
    }

    private fun body(node: TableBody) = TBODY(attributesMapOf(), consumer).visit {
        context.extendAttributes(node, this)
        context.renderChildren(node)
    }

    private fun row(node: TableRow) = TR(attributesMapOf(), consumer).visit {
        context.extendAttributes(node, this)
        context.renderChildren(node)
    }

    private fun cell(node: TableCell) = when {
        node.isHeader -> TH(attributesMapOf(), consumer)
        else -> TD(attributesMapOf(), consumer)
    }.visit {
        node.alignment?.let { attributes["align"] = node.alignment.value }
        context.extendAttributes(node, this as HTMLTag)
        context.renderChildren(node)
    }

    private val TableCell.Alignment.value
        get() = when (this) {
            TableCell.Alignment.LEFT -> "left"
            TableCell.Alignment.CENTER -> "center"
            TableCell.Alignment.RIGHT -> "right"
            else -> error("unknown alignment: $this")
        }
}