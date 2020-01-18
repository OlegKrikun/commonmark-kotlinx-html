plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(project(":commonmark-kotlinx-html"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.12")

    api("com.atlassian.commonmark:commonmark-ext-heading-anchor:0.13.1")

    testImplementation(project(":commonmark-kotlinx-html-test"))
}
