plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.12")

    implementation("com.atlassian.commonmark:commonmark:0.13.0")

    testImplementation(project(":commonmark-kotlinx-html-test"))
}