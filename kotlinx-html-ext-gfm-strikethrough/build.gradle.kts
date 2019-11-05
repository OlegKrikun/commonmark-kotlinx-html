plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":kotlinx-html"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.12")

    implementation("com.atlassian.commonmark:commonmark-ext-gfm-strikethrough:0.13.0")

    testImplementation(project(":kotlinx-html-test"))
}
