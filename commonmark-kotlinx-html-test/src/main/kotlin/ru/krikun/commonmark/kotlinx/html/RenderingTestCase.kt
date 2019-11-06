package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.TagConsumer
import kotlinx.html.stream.createHTML
import kotlin.test.assertEquals

abstract class RenderingTestCase {

    protected abstract fun render(output: TagConsumer<*>, source: String)

    protected fun assertRendering(source: String, expectedResult: String) {
        assertEquals(expectedResult, createHTML(false).apply { render(this, source) }.finalize())
    }
}