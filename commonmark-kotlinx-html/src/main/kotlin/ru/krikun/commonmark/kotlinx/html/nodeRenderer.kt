package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.A
import kotlinx.html.BLOCKQUOTE
import kotlinx.html.BR
import kotlinx.html.CODE
import kotlinx.html.EM
import kotlinx.html.FlowContent
import kotlinx.html.H1
import kotlinx.html.H2
import kotlinx.html.H3
import kotlinx.html.H4
import kotlinx.html.H5
import kotlinx.html.H6
import kotlinx.html.HR
import kotlinx.html.HTMLTag
import kotlinx.html.IMG
import kotlinx.html.LI
import kotlinx.html.OL
import kotlinx.html.P
import kotlinx.html.PRE
import kotlinx.html.STRONG
import kotlinx.html.UL
import kotlinx.html.attributesMapOf
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.BlockQuote
import org.commonmark.node.BulletList
import org.commonmark.node.Code
import org.commonmark.node.Document
import org.commonmark.node.Emphasis
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.HardLineBreak
import org.commonmark.node.Heading
import org.commonmark.node.HtmlBlock
import org.commonmark.node.HtmlInline
import org.commonmark.node.Image
import org.commonmark.node.IndentedCodeBlock
import org.commonmark.node.Link
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.OrderedList
import org.commonmark.node.Paragraph
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text
import org.commonmark.node.ThematicBreak
import org.commonmark.renderer.NodeRenderer
import kotlinx.html.visit as kotlinxVisit

typealias KotlinxHtmlNodeRendererFactory = (context: KotlinxHtmlNodeRendererContext) -> NodeRenderer

interface KotlinxHtmlNodeRendererContext {
    val output: FlowContent
    fun render(node: Node)
    fun extendAttributes(node: Node, tag: HTMLTag)
}

class KotlinxHtmlCoreNodeRenderer(
    private val context: KotlinxHtmlNodeRendererContext
) : AbstractVisitor(), NodeRenderer {
    private val output = context.output

    override fun getNodeTypes() = setOf(
        BlockQuote::class.java,
        BulletList::class.java,
        Code::class.java,
        Document::class.java,
        Emphasis::class.java,
        FencedCodeBlock::class.java,
        HardLineBreak::class.java,
        Heading::class.java,
        HtmlBlock::class.java,
        HtmlInline::class.java,
        Image::class.java,
        IndentedCodeBlock::class.java,
        Link::class.java,
        ListItem::class.java,
        OrderedList::class.java,
        Paragraph::class.java,
        SoftLineBreak::class.java,
        StrongEmphasis::class.java,
        Text::class.java,
        ThematicBreak::class.java
    )

    override fun render(node: Node) = node.accept(this)

    override fun visit(node: BlockQuote) = BLOCKQUOTE(attributesMapOf(), output.consumer).visitChildren(node)

    override fun visit(node: BulletList) = UL(attributesMapOf(), output.consumer).visitChildren(node)

    override fun visit(node: Code) = CODE(attributesMapOf(), output.consumer).visit(node) { +node.literal }

    override fun visit(document: Document) = visitChildren(document)

    override fun visit(node: Emphasis) = EM(attributesMapOf(), output.consumer).visitChildren(node)

    override fun visit(node: FencedCodeBlock) = PRE(attributesMapOf(), output.consumer).visit(node) {
        CODE(attributesMapOf(), output.consumer).visit(node) {
            node.language()?.let { attributes["class"] = it }
            +node.literal
        }
    }

    override fun visit(node: HardLineBreak) = BR(attributesMapOf(), output.consumer).visit(node)

    override fun visit(node: Heading) = when (node.level) {
        1 -> H1(attributesMapOf(), output.consumer).visitChildren(node)
        2 -> H2(attributesMapOf(), output.consumer).visitChildren(node)
        3 -> H3(attributesMapOf(), output.consumer).visitChildren(node)
        4 -> H4(attributesMapOf(), output.consumer).visitChildren(node)
        5 -> H5(attributesMapOf(), output.consumer).visitChildren(node)
        6 -> H6(attributesMapOf(), output.consumer).visitChildren(node)
        else -> error("unsupported heading: ${node.level}")
    }

    override fun visit(node: ThematicBreak) = HR(attributesMapOf(), output.consumer).visit(node)

    override fun visit(node: HtmlInline) = output.text(node.literal)

    override fun visit(node: HtmlBlock) = output.text(node.literal)

    override fun visit(node: Image) {
        val altTextVisitor = AltTextVisitor()
        node.accept(altTextVisitor)
        val alt = altTextVisitor.altText

        IMG(attributesMapOf("alt", alt, "src", node.destination), output.consumer).visit(node) {
            node.title?.let { attributes["title"] = node.title }
        }
    }

    override fun visit(node: IndentedCodeBlock) = PRE(attributesMapOf(), output.consumer).visit(node) {
        CODE(attributesMapOf(), output.consumer).visit(node) { +node.literal }
    }

    override fun visit(node: Link) = A(attributesMapOf("href", node.destination), output.consumer).visitChildren(node) {
        node.title?.let { attributes["title"] = node.title }
    }

    override fun visit(node: ListItem) = LI(attributesMapOf(), output.consumer).visitChildren(node)

    override fun visit(node: OrderedList) = OL(attributesMapOf(), output.consumer).visitChildren(node) {
        node.startNumber.takeIf { it != -1 }?.let { attributes["start"] = it.toString() }
    }

    override fun visit(node: Paragraph) = P(attributesMapOf(), output.consumer).visitChildren(node)

    override fun visit(node: SoftLineBreak) = output.text("\n")

    override fun visit(node: StrongEmphasis) = STRONG(attributesMapOf(), output.consumer).visitChildren(node)

    override fun visit(node: Text) = output.text(node.literal)

    override fun visitChildren(parent: Node) {
        var node = parent.firstChild
        while (node != null) {
            val next = node.next
            context.render(node)
            node = next
        }
    }

    private inline fun <T : HTMLTag> T.visit(node: Node, crossinline block: T.() -> Unit = {}) = kotlinxVisit {
        block()
        context.extendAttributes(node, this)
    }

    private inline fun <T : HTMLTag> T.visitChildren(node: Node, crossinline block: T.() -> Unit = {}) = kotlinxVisit {
        block()
        context.extendAttributes(node, this)
        this@KotlinxHtmlCoreNodeRenderer.visitChildren(node)
    }

    private fun FencedCodeBlock.language() = info.takeIf { !it.isNullOrEmpty() }?.let {
        when (val space = it.indexOf(" ")) {
            -1 -> it
            else -> it.substring(0, space)
        }
    }?.let { "language-$it" }

    private class AltTextVisitor : AbstractVisitor() {
        private val sb = StringBuilder()
        val altText: String get() = sb.toString()

        override fun visit(text: Text) {
            sb.append(text.literal)
        }

        override fun visit(softLineBreak: SoftLineBreak) {
            sb.append('\n')
        }

        override fun visit(hardLineBreak: HardLineBreak) {
            sb.append('\n')
        }
    }
}