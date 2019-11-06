package ru.krikun.commonmark.kotlinx.html.heading.anchor

import kotlinx.html.HTMLTag
import org.commonmark.ext.heading.anchor.IdGenerator
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.Code
import org.commonmark.node.Heading
import org.commonmark.node.Node
import org.commonmark.node.Text
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlAttributeProvider

internal class HeadingAnchorKotlinxHtmlAttributeProvider(
    defaultId: String,
    prefix: String,
    suffix: String
) : KotlinxHtmlAttributeProvider {
    private val idGenerator = IdGenerator.builder()
        .defaultId(defaultId)
        .prefix(prefix)
        .suffix(suffix)
        .build()

    override fun extend(node: Node, tag: HTMLTag) {
        val headingNode = node as? Heading ?: return

        val visitor = HeadingAnchorVisitor()
        headingNode.accept(visitor)

        tag.attributes["id"] = idGenerator.generateId(visitor.text())
    }

    class HeadingAnchorVisitor : AbstractVisitor() {
        private val wordList = mutableListOf<String>()

        override fun visit(text: Text) {
            wordList += text.literal
        }

        override fun visit(code: Code) {
            wordList += code.literal
        }

        fun text() = wordList.joinToString(separator = "").trim { it <= ' ' }.toLowerCase()
    }
}
