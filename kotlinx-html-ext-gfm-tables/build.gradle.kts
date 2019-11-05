plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":kotlinx-html"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm")

    implementation("com.atlassian.commonmark:commonmark-ext-gfm-tables")

    testImplementation(project(":kotlinx-html-test"))
}
