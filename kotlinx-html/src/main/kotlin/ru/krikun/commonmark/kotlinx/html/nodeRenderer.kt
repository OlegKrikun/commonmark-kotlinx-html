package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.A
import kotlinx.html.FlowContent
import kotlinx.html.IMG
import kotlinx.html.LI
import kotlinx.html.OL
import kotlinx.html.attributesMapOf
import kotlinx.html.blockQuote
import kotlinx.html.br
import kotlinx.html.code
import kotlinx.html.em
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.h5
import kotlinx.html.h6
import kotlinx.html.hr
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.strong
import kotlinx.html.ul
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

    override fun visit(blockQuote: BlockQuote) = output.blockQuote { visitChildren(blockQuote) }

    override fun visit(bulletList: BulletList) = output.ul { visitChildren(bulletList) }

    override fun visit(code: Code) = output.code { +code.literal }

    override fun visit(document: Document) = visitChildren(document)

    override fun visit(emphasis: Emphasis) = output.em { visitChildren(emphasis) }

    override fun visit(fencedCodeBlock: FencedCodeBlock) = output.pre {
        val lang = fencedCodeBlock.language()
        if (lang != null) {
            code(classes = lang) { +fencedCodeBlock.literal }
        } else {
            code { +fencedCodeBlock.literal }
        }
    }

    override fun visit(hardLineBreak: HardLineBreak) = output.br()

    override fun visit(heading: Heading) = when (heading.level) {
        1 -> output.h1 { visitChildren(heading) }
        2 -> output.h2 { visitChildren(heading) }
        3 -> output.h3 { visitChildren(heading) }
        4 -> output.h4 { visitChildren(heading) }
        5 -> output.h5 { visitChildren(heading) }
        6 -> output.h6 { visitChildren(heading) }
        else -> error("unsupported heading: ${heading.level}")
    }

    override fun visit(thematicBreak: ThematicBreak) = output.hr()

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

    override fun visit(indentedCodeBlock: IndentedCodeBlock) = output.pre { code { +indentedCodeBlock.literal } }

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

    override fun visit(paragraph: Paragraph) = output.p { visitChildren(paragraph) }

    override fun visit(softLineBreak: SoftLineBreak) = output.text("\n")

    override fun visit(strongEmphasis: StrongEmphasis) = output.strong { visitChildren(strongEmphasis) }

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