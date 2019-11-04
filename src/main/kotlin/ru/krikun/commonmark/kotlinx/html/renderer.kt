package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.A
import kotlinx.html.FlowContent
import kotlinx.html.IMG
import kotlinx.html.OL
import kotlinx.html.UL
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
import kotlinx.html.li
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

fun FlowContent.render(node: Node) = node.accept(object : AbstractVisitor() {
    private var currentOrderedList: OL? = null
    private var currentUnorderedList: UL? = null

    override fun visit(blockQuote: BlockQuote) = blockQuote { visitChildren(blockQuote) }

    override fun visit(bulletList: BulletList) = ul {
        currentUnorderedList = this
        visitChildren(bulletList)
        currentUnorderedList = null
    }

    override fun visit(code: Code) = code { +code.literal }

    override fun visit(document: Document) = visitChildren(document)

    override fun visit(emphasis: Emphasis) = em { visitChildren(emphasis) }

    override fun visit(fencedCodeBlock: FencedCodeBlock) = pre {
        val lang = fencedCodeBlock.language
        if (lang != null) {
            code(classes = lang) { +fencedCodeBlock.literal }
        } else {
            code { +fencedCodeBlock.literal }
        }
    }

    override fun visit(hardLineBreak: HardLineBreak) = br()

    override fun visit(heading: Heading) = when (heading.level) {
        1 -> h1 { visitChildren(heading) }
        2 -> h2 { visitChildren(heading) }
        3 -> h3 { visitChildren(heading) }
        4 -> h4 { visitChildren(heading) }
        5 -> h5 { visitChildren(heading) }
        6 -> h6 { visitChildren(heading) }
        else -> error("unsupported heading: ${heading.level}")
    }

    override fun visit(thematicBreak: ThematicBreak) = hr()

    override fun visit(htmlInline: HtmlInline) = +htmlInline.literal

    override fun visit(htmlBlock: HtmlBlock) = +htmlBlock.literal

    override fun visit(image: Image) {
        val altTextVisitor = AltTextVisitor()
        image.accept(altTextVisitor)
        val alt = altTextVisitor.altText
        val attrs = when {
            image.title != null -> attributesMapOf("alt", alt, "src", image.destination, "title", image.title)
            else -> attributesMapOf("alt", alt, "src", image.destination)
        }

        IMG(attrs, consumer).visit {}
    }

    override fun visit(indentedCodeBlock: IndentedCodeBlock) = pre { code { +indentedCodeBlock.literal } }

    override fun visit(link: Link) {
        val attrs = when {
            link.title != null -> attributesMapOf("href", link.destination, "title", link.title)
            else -> attributesMapOf("href", link.destination)
        }

        A(attrs, consumer).visit { visitChildren(link) }
    }

    override fun visit(listItem: ListItem) {
        currentOrderedList?.apply { li { visitChildren(listItem) } }
        currentUnorderedList?.apply { li { visitChildren(listItem) } }
    }

    override fun visit(orderedList: OrderedList) {
        val attrs = when {
            orderedList.startNumber != 1 -> attributesMapOf("start", orderedList.startNumber.toString())
            else -> attributesMapOf()
        }

        OL(attrs, consumer).visit {
            currentOrderedList = this
            visitChildren(orderedList)
            currentOrderedList = null
        }
    }

    override fun visit(paragraph: Paragraph) = p { visitChildren(paragraph) }

    override fun visit(softLineBreak: SoftLineBreak) = +"\n"

    override fun visit(strongEmphasis: StrongEmphasis) = strong { visitChildren(strongEmphasis) }

    override fun visit(text: Text) = +text.literal
})

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

private val FencedCodeBlock.language
    get() = info.takeIf { !it.isNullOrEmpty() }?.let {
        val space = it.indexOf(" ")
        if (space == -1) {
            it
        } else {
            it.substring(0, space)
        }
    }?.let { "language-$it" }