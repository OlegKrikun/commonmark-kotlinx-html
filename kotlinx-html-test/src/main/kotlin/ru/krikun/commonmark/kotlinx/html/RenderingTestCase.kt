package ru.krikun.commonmark.kotlinx.html

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.stream.createHTML
import kotlin.test.assertEquals

abstract class RenderingTestCase {

    protected abstract fun render(flowContent: FlowContent, source: String)

    protected fun assertRendering(source: String, expectedResult: String) {
        assertEquals(expectedResult, createHTML(false).div { render(this, source) })
    }
}