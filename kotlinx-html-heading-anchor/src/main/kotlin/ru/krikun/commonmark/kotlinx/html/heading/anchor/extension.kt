package ru.krikun.commonmark.kotlinx.html.heading.anchor

import org.commonmark.Extension
import ru.krikun.commonmark.kotlinx.html.KotlinxHtmlRenderer

class HeadingAnchorKotlinxHtmlExtension private constructor(builder: Builder) : KotlinxHtmlRenderer.KotlinxHtmlRendererExtension {
    private val defaultId = builder.defaultId
    private val idPrefix = builder.idPrefix
    private val idSuffix = builder.idSuffix

    override fun extend(rendererBuilder: KotlinxHtmlRenderer.Builder) {
        rendererBuilder.attributeProviderFactory {
            HeadingAnchorKotlinxHtmlAttributeProvider(defaultId, idPrefix, idSuffix)
        }
    }

    class Builder {
        internal var defaultId = "id"
        internal var idPrefix = ""
        internal var idSuffix = ""

        fun defaultId(value: String) = apply { defaultId = value }

        fun idPrefix(value: String) = apply { idPrefix = value }

        fun idSuffix(value: String) = apply { idSuffix = value }

        fun build(): Extension = HeadingAnchorKotlinxHtmlExtension(this)
    }
}
