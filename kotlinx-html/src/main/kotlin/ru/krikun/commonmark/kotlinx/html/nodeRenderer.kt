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
import kotlinx.html.visit
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

typealias KotlinxHtmlNodeRendererFactory = (context: KotlinxHtmlNodeRendererContext) -> NodeRenderer

interface KotlinxHtmlNodeRendererContext {
    val output: FlowContent
    fun render(node: Node)
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

    override fun visit(blockQuote: BlockQuote) = BLOCKQUOTE(attributesMapOf(), output.consumer).visit {
        visitChildren(blockQuote)
    }

    override fun visit(bulletList: BulletList) = UL(attributesMapOf(), output.consumer).visit {
        visitChildren(bulletList)
    }

    override fun visit(code: Code) = CODE(attributesMapOf(), output.consumer).visit { +code.literal }

    override fun visit(document: Document) = visitChildren(document)

    override fun visit(emphasis: Emphasis) = EM(attributesMapOf(), output.consumer).visit { visitChildren(emphasis) }

    override fun visit(fencedCodeBlock: FencedCodeBlock) = PRE(attributesMapOf(), output.consumer).visit {
        when (val lang = fencedCodeBlock.language()) {
            null -> CODE(attributesMapOf(), output.consumer)
            else -> CODE(attributesMapOf("class", lang), output.consumer)
        }.visit { +fencedCodeBlock.literal }
    }

    override fun visit(hardLineBreak: HardLineBreak) = BR(attributesMapOf(), output.consumer).visit {}

    override fun visit(heading: Heading) = when (heading.level) {
        1 -> H1(attributesMapOf(), output.consumer)
        2 -> H2(attributesMapOf(), output.consumer)
        3 -> H3(attributesMapOf(), output.consumer)
        4 -> H4(attributesMapOf(), output.consumer)
        5 -> H5(attributesMapOf(), output.consumer)
        6 -> H6(attributesMapOf(), output.consumer)
        else -> error("unsupported heading: ${heading.level}")
    }.visit { visitChildren(heading) }

    override fun visit(thematicBreak: ThematicBreak) = HR(attributesMapOf(), output.consumer).visit {}

    override fun visit(htmlInline: HtmlInline) = output.text(htmlInline.literal)

    override fun visit(htmlBlock: HtmlBlock) = output.text(htmlBlock.literal)

    override fun visit(image: Image) {
        val altTextVisitor = AltTextVisitor()
        image.accept(altTextVisitor)
        val alt = altTextVisitor.altText
        val attrs = when {
            image.title != null -> attributesMapOf("alt", alt, "src", image.destination, "title", image.title)
            else -> attributesMapOf("alt", alt, "src", image.destination)
        }

        IMG(attrs, output.consumer).visit {}
    }

    override fun visit(indentedCodeBlock: IndentedCodeBlock) = PRE(attributesMapOf(), output.consumer).visit {
        CODE(attributesMapOf(), output.consumer).visit { +indentedCodeBlock.literal }
    }

    override fun visit(link: Link) {
        val attrs = when {
            link.title != null -> attributesMapOf("href", link.destination, "title", link.title)
            else -> attributesMapOf("href", link.destination)
        }

        A(attrs, output.consumer).visit { visitChildren(link) }
    }

    override fun visit(listItem: ListItem) = LI(attributesMapOf(), output.consumer).visit { visitChildren(listItem) }

    override fun visit(orderedList: OrderedList) {
        val attrs = when {
            orderedList.startNumber != 1 -> attributesMapOf("start", orderedList.startNumber.toString())
            else -> attributesMapOf()
        }

        OL(attrs, output.consumer).visit { visitChildren(orderedList) }
    }

    override fun visit(paragraph: Paragraph) = P(attributesMapOf(), output.consumer).visit { visitChildren(paragraph) }

    override fun visit(softLineBreak: SoftLineBreak) = output.text("\n")

    override fun visit(strongEmphasis: StrongEmphasis) = STRONG(attributesMapOf(), output.consumer).visit {
        visitChildren(strongEmphasis)
    }

    override fun visit(text: Text) = output.text(text.literal)

    override fun visitChildren(parent: Node) {
        var node = parent.firstChild
        while (node != null) {
            val next = node.next
            context.render(node)
            node = next
        }
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