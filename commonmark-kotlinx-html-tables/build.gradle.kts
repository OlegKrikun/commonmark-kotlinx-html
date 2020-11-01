plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(project(":commonmark-kotlinx-html"))

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")

    api("com.atlassian.commonmark:commonmark-ext-gfm-tables:0.15.2")

    testImplementation(project(":commonmark-kotlinx-html-test"))
}
